package genericnode.handler;

import genericnode.server.UDPServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class UdpClientHandler extends ClientHandler {

    private final DatagramSocket socket;
    private final DatagramPacket packet;

    public UdpClientHandler(DatagramSocket socket, DatagramPacket packet, UDPServer udpServer) {
        super(udpServer);
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        try {
            String data = new String(packet.getData(), 0, packet.getLength());

            String[] parsedData = parseData(data);
            String operation = parsedData[0];
            String key = parsedData[1];
            String value = parsedData[2];
            executeOperation(operation, key, value);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                sendResponse("Error: " + e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void sendResponse(String response) throws IOException {
        byte[] responseData = response.getBytes();
        DatagramPacket responsePacket = new DatagramPacket(
                responseData, responseData.length, packet.getAddress(), packet.getPort());
        socket.send(responsePacket);
    }

    private String receivePacketFromClient() throws IOException {
        byte[] buffer = new byte[65535];
        DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(incomingPacket);
        return new String(incomingPacket.getData(), 0, incomingPacket.getLength());
    }

    private String[] parseData(String data) {
        String[] parts = data.split(" ");
        String operation = parts[0];
        String key = parts.length > 1 ? parts[1] : null;
        String value = parts.length > 2 ? parts[2] : null;
        return new String[]{operation, key, value};
    }

    private void executeOperation(String operation, String key, String value) throws IOException {
        if (operation.equals("put")) {
            server.put(key, value);

        } else if (operation.equals("get")) {
            value = server.get(key);
            sendResponse(value);
        } else if (operation.equals("del")) {
            server.del(key);
        } else if (operation.equals("store")) {
            ArrayList<String> entries = server.store();
            for(String entry : entries) {
                sendResponse(entry);
            }
            sendResponse("Finished");
        } else {
            server.exit();
        }
    }
}