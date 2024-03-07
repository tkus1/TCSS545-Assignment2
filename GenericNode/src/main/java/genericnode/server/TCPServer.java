package genericnode.server;

import genericnode.*;
import genericnode.handler.ClientHandler;
import genericnode.handler.ServerHandler;
import genericnode.handler.TcpClientHandler;
import genericnode.handler.TcpServerHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer implements Server {

    private final int ATTEMPT_LIMIT = 10;
    private ServerSocket tcpClientServer = null;
    private ServerSocket tcpServerServer = null;

    private final LockableDataStorage dataStorage = new LockableDataStorage();
    private final GetOtherServersStrategy getOtherServersStrategy;
    private final ServerNotifier serverNotifier;
    private final ServerRequestProcessor requestProcessor;

    public TCPServer(GetOtherServersStrategy getOtherServersStrategy) {
        super();
        this.getOtherServersStrategy = getOtherServersStrategy;
        this.serverNotifier = new ServerNotifier(getOtherServersStrategy.getOtherServers(), ATTEMPT_LIMIT);
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
    public void put (String key, String value) throws IOException {
        serverNotifier.put(key, value);
    }

    @Override
    public String get (String key) throws IOException {
        return dataStorage.get(key);
    }

    @Override
    public void del (String key) throws IOException {
        serverNotifier.del(key);
    }

    @Override
    public ArrayList<String> store () throws IOException {
        return dataStorage.store();
    }

    @Override
    public synchronized void exit () throws IOException {
        tcpClientServer.close();
    }

    public void processServerRequest(String operation, String key, String value, DataOutputStream outToServer) throws IOException {
        requestProcessor.processServerRequest(operation, key, value, outToServer);
    }
}