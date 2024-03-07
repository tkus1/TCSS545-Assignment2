package genericnode.handler;

import genericnode.server.TCPServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TcpServerHandler extends ServerHandler{
    private final Socket connectionSocket;
    public TcpServerHandler(Socket connectionSocket, TCPServer server) {
        super(server);
        this.connectionSocket = connectionSocket;
    }

    public void run() {
        try {
            System.out.println("TCPServerHandler :"+Thread.currentThread().getId());
            DataInputStream inFromServer = new DataInputStream(connectionSocket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
            String operation = inFromServer.readUTF();
            String key = null;
            String value = null;
            if (operation.equals("dput1")) {
                key = inFromServer.readUTF();
                value = inFromServer.readUTF();
            } else if (operation.equals("dput2")) {
                key = inFromServer.readUTF();
                value = inFromServer.readUTF();
            } else if (operation.equals("dputabort")) {
                key = inFromServer.readUTF();
                value = inFromServer.readUTF();
            } else if (operation.equals("ddel1")) {
                key = inFromServer.readUTF();
            } else if (operation.equals("ddel2")) {
                key = inFromServer.readUTF();
            } else if (operation.equals("ddelabort")){
                key = inFromServer.readUTF();
            }
            System.out.println("Receipt : Operation: " + operation + " Key: " + key + " Value: " + value);
            System.out.println("Responding to server");
            server.processServerRequest(operation, key, value, outToServer);
            connectionSocket.close();
        }catch (IOException e) {
            System.out.println("Error in TcpServerHandler");
            System.out.println(e);
            throw new RuntimeException(e);
        }

    }
}

