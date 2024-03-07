package genericnode.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public interface Server {

    //void startServer(int port) throws IOException;

    void startServer(int clientPort, int serverPort) throws IOException;

    void put(String key, String value) throws IOException;
    String get(String key) throws IOException;
    void del(String key) throws IOException;
    ArrayList<String> store() throws IOException;
    void exit() throws IOException;
    void processServerRequest(String key, String value, String operation, DataOutputStream outToServer) throws IOException;
}
