package genericnode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnection {
    private Socket socket;
    private DataOutputStream outToServer;
    private DataInputStream inFromServer;
    private String host;
    private int port;

    public ServerConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() throws IOException {
        System.out.println("Connecting to " + host + " on port " + port);
        socket = new Socket(host, port);
        if (isSelfConnection()) {
            System.out.println("Connection to self is not allowed. Closing the connection.");
            socket.close();
            return false;
        }
        outToServer = new DataOutputStream(socket.getOutputStream());
        inFromServer = new DataInputStream(socket.getInputStream());
        return true;
    }

    private boolean isSelfConnection() throws IOException {
        InetAddress localAddress = InetAddress.getLocalHost();
        InetAddress remoteAddress = socket.getInetAddress();
        return remoteAddress.equals(localAddress);
    }

    public void close() throws IOException {
        // If the connection is already closed, do nothing
        if (socket == null || socket.isClosed()) {
            return;
        }
        if (outToServer != null) {
            outToServer.close();
        }
        if (inFromServer != null) {
            inFromServer.close();
        }
        socket.close();
    }
    public DataOutputStream getOutToServer() {
        return outToServer;
    }
    public DataInputStream getInFromServer() {
        return inFromServer;
    }

}