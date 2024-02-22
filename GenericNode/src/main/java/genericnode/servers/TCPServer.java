package genericnode.servers;

import genericnode.DataStorage;
import genericnode.handlers.ClientHandler;
import genericnode.handlers.TcpClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer implements Server {
    private ServerSocket tcpServer = null;

    @Override
    public void startServer (int port) throws IOException {
        tcpServer = new ServerSocket(port);
        while (!tcpServer.isClosed()) {
            System.out.println(tcpServer.isClosed());
            Socket connectionSocket = tcpServer.accept();
            ClientHandler tcpClientHandler = new TcpClientHandler(connectionSocket, this);
            new Thread(tcpClientHandler).start();
        }
    }

    @Override
    public void put (String key, String value) throws IOException {
        DataStorage.put(key, value);
    }

    @Override
    public String get (String key) throws IOException {
        return DataStorage.get(key);
    }

    @Override
    public void del (String key) {
        DataStorage.del(key);
    }

    @Override
    public ArrayList<String> store () throws IOException {
        return DataStorage.store();
    }

    @Override
    public synchronized void exit () throws IOException {
        tcpServer.close();
    }

    public TCPServer() {
        super();
    }
}
