package genericnode.server;

import genericnode.DataStorage;
import genericnode.handler.ClientHandler;
import genericnode.handler.UdpClientHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class UDPServer implements Server{

    private DatagramSocket udpServer = null;
    private final DataStorage DataStorage = new DataStorage();

    @Override
    public void startServer(int clientPort, int serverPort) throws IOException {
        udpServer = new DatagramSocket(clientPort);
        while (!udpServer.isClosed()) {
            System.out.println(udpServer.isClosed());
            byte[] buffer = new byte[65535];
            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
            udpServer.receive(incomingPacket);
            ClientHandler udpClientHandler = new UdpClientHandler(udpServer, incomingPacket, this);
            new Thread(udpClientHandler).start();
        }

    }

    @Override
    public void put(String key, String value) {
        DataStorage.put(key, value);
    }

    @Override
    public String get(String key) {
        return DataStorage.get(key);
    }

    @Override
    public void del(String key) {
        DataStorage.del(key);
    }

    @Override
    public ArrayList<String> store() {
        return DataStorage.store();
    }

    @Override
    public void exit() {
        udpServer.close();
    }

    @Override
    public void respondToServer(String key, String value, String operation, DataOutputStream outToServer) throws IOException {
        return;
    }
}
