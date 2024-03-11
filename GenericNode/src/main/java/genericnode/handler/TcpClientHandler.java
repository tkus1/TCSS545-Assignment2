package genericnode.handler;

import genericnode.server.TCPServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class TcpClientHandler extends ClientHandler {

    private final Socket connectionSocket;

    public TcpClientHandler(Socket connectionSocket, TCPServer tcpServer) {
        super(tcpServer);
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        try {
            System.out.println("TCPClientHandler :"+ Thread.currentThread().getId());
            DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            String operation = inFromClient.readUTF();
            String key;
            String value;
            if (operation.equals("put")) {
                key = inFromClient.readUTF();
                value = inFromClient.readUTF();
                server.put(key, value);
            } else if (operation.equals("get")) {
                key = inFromClient.readUTF();
                value = server.get(key);
                outToClient.writeUTF(value);
            } else if (operation.equals("del")) {
                key = inFromClient.readUTF();
                server.del(key);
            } else if (operation.equals("store")) {
                ArrayList<String> entries = server.store();
                for(String entry : entries) {
                    outToClient.writeUTF(entry);
                }
                outToClient.writeUTF("Finished");
            }  else if (operation.equals("heartbeat")) { // Handel heartbeat command received from other servers
                key = inFromClient.readUTF();
                value = inFromClient.readUTF();
                server.put(key+":"+value, String.valueOf(System.currentTimeMillis()));
                ArrayList<String> entries = server.store();
                for(String entry : entries) {
                    outToClient.writeUTF(entry);
                }
                outToClient.writeUTF("Done");
            } else {
                server.exit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
