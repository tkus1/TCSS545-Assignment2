package genericnode.Clients;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.AbstractMap.SimpleEntry;

public class UDPClient implements Client {

    private DatagramSocket clientSocket;

    private InetAddress serverAddress;
    private int serverPort;

    private final String serverResponse = "server response:";
    private final String exitStatement = "<the server then exits>";
    public UDPClient() throws UnknownHostException, SocketException {
        super();
        clientSocket = new DatagramSocket();
    }
    @Override
    public void put (String key, String value) throws IOException {
        sendPacketToServer("put" + " " + key + " " + value);
        System.out.println(serverResponse + "put key=" + key);

    }

    @Override
    public String get (String key) throws IOException {
        sendPacketToServer("get" + " " + key);
        System.out.println(serverResponse + "get key=" + key + " get value=" + receivePacketFromServer());
        return null;
    }

    @Override
    public void del (String key) throws IOException {
        sendPacketToServer("del" + " " + key);
        System.out.println(serverResponse + "delete key="+ key);
    }

    @Override
    public void store () throws IOException {
        String entry = receivePacketFromServer();
        System.out.println(serverResponse);
        while(!entry.equals("Finished")) {
            System.out.println(entry);
            entry = receivePacketFromServer();
        }
    }

    @Override
    public void exit () {
        clientSocket.close();
        System.out.println(exitStatement);

    }

    @Override
    public void executeOperation (String operation, SimpleEntry<String, String> entry) throws IOException {
        sendPacketToServer(operation);
        if (operation.equals("put")) {
            put(entry.getKey(), entry.getValue());
        } else if (operation.equals("get")) {
            get(entry.getKey());
        } else if (operation.equals("del")) {
            del(entry.getKey());
        } else if (operation.equals("store")) {
            store();
        } else {
            exit();
        }
    }

    @Override
    public void connect (String host, int port) throws IOException {
        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
    }
    public void sendPacketToServer(String data) throws IOException {
        byte[] buffer = data.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
        clientSocket.send(packet);
    }
    // Java
    public String receivePacketFromServer() throws IOException {

        byte[] buffer = new byte[65535];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        clientSocket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

}
