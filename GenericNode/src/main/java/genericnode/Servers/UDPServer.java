package genericnode.Servers;

import genericnode.DataStorage;
import genericnode.Handlers.ClientHandler;
import genericnode.Handlers.UdpClientHandler;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class UDPServer implements Server{
    private DatagramSocket udpServer = null;
    @Override
    public void startServer(int port) throws IOException {
        udpServer = new DatagramSocket(port);
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
}
