package genericnode.server;

import genericnode.*;
import genericnode.handler.ClientHandler;
import genericnode.handler.ServerHandler;
import genericnode.handler.TcpClientHandler;
import genericnode.handler.TcpServerHandler;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TCPServer implements Server {

    private final int ATTEMPT_LIMIT = 10;
    private ServerSocket tcpClientServer = null;
    private ServerSocket tcpServerServer = null;

    private final LockableDataStorage dataStorage = new LockableDataStorage();
    private ServerNotifier serverNotifier;
    private ServerRequestProcessor requestProcessor;
    private final ConcurrentHashMap<String, Integer> serverList = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    public String dirServerAddr;
    public int port;

    public TCPServer() {
        super();
    }

    public TCPServer(GetOtherServersStrategy getOtherServersStrategy) {
        super();
//        this.getOtherServersStrategy = getOtherServersStrategy;
        if (getOtherServersStrategy instanceof CentralizeMembershipGetOtherServersStrategy) {
            ((CentralizeMembershipGetOtherServersStrategy) getOtherServersStrategy).setServer(this);
        }
        this.serverNotifier = new ServerNotifier(getOtherServersStrategy, ATTEMPT_LIMIT);
        this.requestProcessor = new ServerRequestProcessor(dataStorage);
        System.out.println("Server started");
        System.out.println("Other servers: " );
        for (ServerConnection serverConnection : getOtherServersStrategy.getOtherServers()) {
            System.out.println(serverConnection);
        }
    }

    @Override
    public void startServer(int clientPort, int serverPort) throws IOException {
        startClientServer(clientPort);
        startServerServer(serverPort);
    }

    public void startServerWithCMT(int clientPort, int serverPort) throws IOException {
        // For regular TCP server, send heartbeat to centralized membership server every 5 seconds
        executor.scheduleAtFixedRate(this::sendHeartbeat, 0, 5, TimeUnit.SECONDS);
        startClientServer(clientPort);
        startServerServer(serverPort);
    }

    public void startMembershipServer(int port) throws IOException {
        startClientServer(port);
        // Check the list of active servers every 10 seconds
        executor.scheduleAtFixedRate(this::removeDeadServers, 0, 10, TimeUnit.SECONDS);
    }

    private void removeDeadServers() {
        long threshold = System.currentTimeMillis() - 10000; // 10 seconds ago
        DataStorage.remove(threshold);
    }

    private void startClientServer(int clientPort) throws IOException {
        tcpClientServer = new ServerSocket(clientPort);
        new Thread(() -> {
            try {
                while (!tcpClientServer.isClosed()) {
                    Socket connectionSocket = tcpClientServer.accept();
                    ClientHandler tcpClientHandler = new TcpClientHandler(connectionSocket, this);
                    new Thread(tcpClientHandler).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startServerServer(int serverPort) throws IOException {
        tcpServerServer = new ServerSocket(serverPort);
        new Thread(() -> {
            try {
                while (!tcpServerServer.isClosed()) {
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
    public String put (String key, String value) throws IOException {
        if (serverNotifier != null) {
            serverNotifier.put(key, value);
        }
        else {
            DataStorage.put(key, value);
        }
        return key;
    }

    @Override
    public String get (String key) throws IOException {
        return DataStorage.get(key);
    }

    @Override
    public String del (String key) throws IOException {
        if (serverNotifier != null) {
            serverNotifier.del(key);
        }
        else {
            DataStorage.del(key);
        }
        return key;
    }

    @Override
    public ArrayList<String> store () throws IOException {
        return DataStorage.store();
    }

    @Override
    public synchronized void exit () throws IOException {
        tcpClientServer.close();
    }

    public void processServerRequest(String operation, String key, String value, DataOutputStream outToServer) throws IOException {
        requestProcessor.processServerRequest(operation, key, value, outToServer);
    }

    public void sendHeartbeat() {
        try {
            Socket clientSocket = new Socket(dirServerAddr, 4410);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
            outToServer.writeUTF("heartbeat");

            InetAddress localhost = InetAddress.getLocalHost();
            String ipAddress = (localhost.getHostAddress()).trim();
            outToServer.writeUTF(ipAddress);
            outToServer.writeUTF(String.valueOf(port+1));

            serverList.clear();
            String entry = inFromServer.readUTF();
            while (!entry.equals("Done")) {
                String[] parts = entry.split(":");
                serverList.put(parts[1], Integer.valueOf(parts[2]));
                entry = inFromServer.readUTF();
            }
            clientSocket.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<String, Integer> getServerList() {
        return this.serverList;
    }
}