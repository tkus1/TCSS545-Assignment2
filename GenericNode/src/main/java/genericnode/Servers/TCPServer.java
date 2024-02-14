package genericnode.Servers;

import genericnode.DataStorage;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer implements Server {
    ServerSocket tcpServer = null;
    Socket connectionSocket = null;
    DataInputStream inFromClient = null;
    DataOutputStream outToClient = null;
    @Override
    public void startServer(int port) throws IOException {
        tcpServer = new ServerSocket(port);
        String operation = "";
        while(!operation.equals("exit")) {
            connectionSocket = tcpServer.accept();
            inFromClient = new DataInputStream(connectionSocket.getInputStream());
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            operation = inFromClient.readUTF();
            executeOperation(operation);
        }
    }

    @Override
    public void put(String key, String value) throws IOException {
        DataStorage.put(key, value);
    }

    @Override
    public String get(String key) throws IOException {
        String value = DataStorage.get(key);
        outToClient.writeUTF(value);
        return null;
    }

    @Override
    public void del(String key) {
        DataStorage.del(key);
    }

    @Override
    public void store() throws IOException {
        ArrayList<String> entries = DataStorage.store();
        for(String entry : entries) {
            outToClient.writeUTF(entry);
        }
        outToClient.writeUTF("Finished");
    }

    @Override
    public void exit() throws IOException {
        inFromClient = null;
        outToClient = null;
        connectionSocket = null;
        tcpServer.close();
    }

    @Override
    public void executeOperation(String operation) throws IOException {
        String key;
        String value;
        System.out.println(operation);
        if (operation.equals("put")) {
            key = inFromClient.readUTF();
            value = inFromClient.readUTF();
            put(key, value);
        } else if (operation.equals("get")) {
            key = inFromClient.readUTF();
            get(key);
        } else if (operation.equals("del")) {
            key = inFromClient.readUTF();
            del(key);
        } else if (operation.equals("store")) {
            store();
        } else {
            exit();
        }
    }

    public TCPServer() {
        super();

    }
}
