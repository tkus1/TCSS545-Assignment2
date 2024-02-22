package genericnode.clients;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.AbstractMap.SimpleEntry;

public class TCPClient implements Client {

    private Socket clientSocket;
    private DataOutputStream outToServer = null;
    private DataInputStream inFromServer = null;

    private final String serverResponse = "server response:";
    private final String exitStatement = "<the server then exits>";

    @Override
    public void put (String key, String value) throws IOException {
        outToServer.writeUTF(key);
        outToServer.writeUTF(value);
        System.out.println(serverResponse + "put key=" + key);
        clientSocket.close();
    }

    @Override
    public void get (String key) throws IOException {
        outToServer.writeUTF(key);
        System.out.println(serverResponse + "get key=" + key + " get value=" + inFromServer.readUTF());
    }

    @Override
    public void del (String key) throws IOException {
        outToServer.writeUTF(key);
        System.out.println(serverResponse + "delete key="+ key);
    }

    @Override
    public void store () throws IOException {
        String entry = inFromServer.readUTF();
        System.out.println(serverResponse);
        while(!entry.equals("Finished")) {
            System.out.println(entry);
            entry = inFromServer.readUTF();
        }
    }

    @Override
    public void exit () {
        System.out.println(exitStatement);
    }


    public void executeOperation (String operation, SimpleEntry<String, String> entry) throws IOException {
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


    public void connect (String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        inFromServer = new DataInputStream(clientSocket.getInputStream());
    }


}
