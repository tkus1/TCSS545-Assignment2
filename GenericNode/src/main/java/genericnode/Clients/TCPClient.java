package genericnode.Clients;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.AbstractMap.SimpleEntry;

public class TCPClient implements Client {

    Socket clientSocket;
    DataOutputStream outToServer = null;
    DataInputStream inFromServer = null;
    @Override
    public void put(String key, String value) throws IOException {
        outToServer.writeUTF(key);
        outToServer.writeUTF(value);
        clientSocket.close();
    }

    @Override
    public String get(String key) throws IOException {
        outToServer.writeUTF(key);
        System.out.println(inFromServer.readUTF());
        return null;
    }

    @Override
    public void del(String key) throws IOException {
        outToServer.writeUTF(key);
    }

    @Override
    public void store() throws IOException {
        String entry = inFromServer.readUTF();
        while(!entry.equals("Finished")) {
            System.out.println(entry);
            entry = inFromServer.readUTF();
        }
    }

    @Override
    public void exit() {

    }

    @Override
    public void executeOperation(String operation, SimpleEntry<String, String> entry) throws IOException {
        outToServer.writeUTF(operation);
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
    public void connect(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        inFromServer = new DataInputStream(clientSocket.getInputStream());
    }


}
