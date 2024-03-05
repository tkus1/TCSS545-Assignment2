package genericnode.server;

import genericnode.DataStorage;
import genericnode.GetOtherServersStrategy;
import genericnode.KeyLockManager;
import genericnode.ServerConnection;
import genericnode.handler.ClientHandler;
import genericnode.handler.ServerHandler;
import genericnode.handler.TcpClientHandler;
import genericnode.handler.TcpServerHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServer implements Server {

    private final int ATTEMPT_LIMIT = 10;
    private ServerSocket tcpClientServer = null;
    private ServerSocket tcpServerServer = null;
    private List<ServerConnection> otherServers = new ArrayList<>();
    private final KeyLockManager keyLockManager = new KeyLockManager();
    private final DataStorage dataStorage = new DataStorage();
    private final GetOtherServersStrategy getOtherServersStrategy;

    @Override
    public void startServer(int clientPort, int serverPort) throws IOException {
        tcpClientServer = new ServerSocket(clientPort);
        new Thread(() -> {
            try {
                while (!tcpClientServer.isClosed()) {
                    System.out.println(tcpClientServer.isClosed());
                    Socket connectionSocket = tcpClientServer.accept();
                    ClientHandler tcpClientHandler = new TcpClientHandler(connectionSocket, this);
                    new Thread(tcpClientHandler).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        tcpServerServer = new ServerSocket(serverPort);
        new Thread(() -> {
            try {
                while (!tcpServerServer.isClosed()) {
                    System.out.println(tcpServerServer.isClosed());
                    Socket connectionSocket = tcpServerServer.accept();
                    ServerHandler tcpServerHandler = new TcpServerHandler(connectionSocket, this);
                    new Thread(tcpServerHandler).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void put (String key, String value) throws IOException {
        boolean success = true;
        for (ServerConnection serverConnection : otherServers) {
            if (!dput1(key, value, serverConnection)) {
                System.out.println("dput1 failed");
                success = false;
                break;
            }
        }
        if(success) {
            dataStorage.put(key, value);
            for (ServerConnection serverConnection : otherServers) {
                dput2(key, value, serverConnection);
            }
        } else {
            for (ServerConnection serverConnection : otherServers) {
                dputabort(key, value, serverConnection);
            }
        }
    }

    @Override
    public String get (String key) throws IOException {
        return dataStorage.get(key);
    }

    @Override
    public void del (String key) throws IOException {
        boolean success = true;
        for (ServerConnection serverConnection : otherServers) {
            if (!ddel1(key, serverConnection)) {
                success = false;
                break;
            }
        }
        if(success) {
            dataStorage.del(key);
            for (ServerConnection serverConnection : otherServers) {
                ddel2(key, serverConnection);
            }
        } else {
            for (ServerConnection serverConnection : otherServers) {
                ddelabort(key, serverConnection);
            }
        }
        dataStorage.del(key);
    }

    @Override
    public ArrayList<String> store () throws IOException {
        return dataStorage.store();
    }

    @Override
    public synchronized void exit () throws IOException {
        tcpClientServer.close();
    }

    public TCPServer(GetOtherServersStrategy getOtherServersStrategy) {
        super();
        this.getOtherServersStrategy = getOtherServersStrategy;
        otherServers = getOtherServersStrategy.getOtherServers();
        System.out.println("Server started");
        System.out.println("Other servers: " );
        for (ServerConnection serverConnection : otherServers) {
            System.out.println(serverConnection);
        }

    }

    private boolean notifyOtherServer(String operation, String key, String value, ServerConnection serverConnection) throws IOException {
        otherServers = getOtherServersStrategy.getOtherServers();
        int attemptCount = 0;
        while (attemptCount < ATTEMPT_LIMIT) {
            try {
                if(!serverConnection.connect()){
                    System.out.println("connecting to self server");
                    return true;
                }
                DataOutputStream out = serverConnection.getOutToServer();
                System.out.println("Sending operation: " + operation);
                out.writeUTF(operation);
                System.out.println("operation sent");
                if(operation.equals("dput1")) {
                    out.writeUTF(key);
                    out.writeUTF(value);
                }else if(operation.equals("dput2")) {
                    out.writeUTF(key);
                    out.writeUTF(value);
                }else if(operation.equals("dputabort")) {
                    out.writeUTF(key);
                    out.writeUTF(value);
                }else if(operation.equals("ddel1")) {
                    out.writeUTF(key);
                }else if(operation.equals("ddel2")) {
                    out.writeUTF(key);
                }else if(operation.equals("ddelabort")){
                    out.writeUTF(key);
                }

                DataInputStream in = serverConnection.getInFromServer();
                String response = in.readUTF();
                System.out.println("Response: " + response);
                if (response.equals("Accept")) {
                    System.out.println("Operation successful");
                    serverConnection.close();
                    return true;
                } else {
                    System.out.println("Operation failed (attempt count = "+ attemptCount + "). Retrying...");
                    attemptCount++;
                }

            } catch (IOException e) {
                System.out.println("Connection attempt failed: " + e.getMessage());
                attemptCount++;
            }
        }
        System.out.println("Failed to connect after " + ATTEMPT_LIMIT + " attempts.");
        serverConnection.close();
        return false;
    }
    public void respondToServer(String operation, String key, String value, DataOutputStream outToServer) throws IOException {
        System.out.println("respondToServer:: Operation: " + operation + " Key: " + key + " Value: " + value);
        if(operation.equals("dput1")) {
            if(keyLockManager.isKeyLocked(key)) {
                System.out.println("Key is locked");
                outToServer.writeUTF("Locked");
            }
            else {
                System.out.println("Key is not locked");
                keyLockManager.lockKey(key);
                outToServer.writeUTF("Accept");
            }
        }
        else if(operation.equals("dput2")) {
            dataStorage.put(key, value);
            keyLockManager.unlockKey(key);
        }
        else if(operation.equals("dputabort")) {
            keyLockManager.unlockKey(key);
        }
        else if(operation.equals("ddel1")) {
            if(keyLockManager.isKeyLocked(key)) {
                outToServer.writeUTF("Locked");
            }
            else {
                keyLockManager.lockKey(key);
                outToServer.writeUTF("Accept");
            }
        }
        else if(operation.equals("ddel2")) {
            dataStorage.del(key);
            keyLockManager.unlockKey(key);
        }
        else if(operation.equals("ddelabort")) {
            keyLockManager.unlockKey(key);
        }
    }

    private boolean dput1(String key, String value, ServerConnection serverConnection) throws IOException {
        System.out.println("dput1"+ " key:" + key+" value:"+ value);
        return notifyOtherServer("dput1", key, value, serverConnection);
    }

    private void dput2(String key, String value, ServerConnection serverConnection) throws IOException {
        notifyOtherServer("dput2", key, value, serverConnection);
    }

    private void dputabort(String key,String value, ServerConnection serverConnection) throws IOException {
        notifyOtherServer("dputabort", key, value, serverConnection);
    }
    private boolean ddel1(String key, ServerConnection serverConnection) throws IOException {
        return notifyOtherServer("ddel1", key, null, serverConnection);
    }
    private void ddel2(String key, ServerConnection serverConnection) throws IOException {
        notifyOtherServer("ddel2", key, null, serverConnection);
    }
    private void ddelabort(String key, ServerConnection serverConnection) throws IOException {
        notifyOtherServer("ddelabort", key, null, serverConnection);
    }

}
