package genericnode.Servers;

import java.io.IOException;
import java.util.ArrayList;

public interface Server {

    void startServer(int port) throws IOException;
    void put(String key, String value) throws IOException;
    String get(String key) throws IOException;
    void del(String key);
    ArrayList<String> store() throws IOException;
    void exit() throws IOException;

}
