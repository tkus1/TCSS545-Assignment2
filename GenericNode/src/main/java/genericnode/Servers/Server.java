package genericnode.Servers;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.ConcurrentHashMap;

public interface Server {

    void startServer(int port) throws IOException;

    void put(String key, String value) throws IOException;
    String get(String key) throws IOException;
    void del(String key);
    void store() throws IOException;
    void exit() throws IOException;
    void executeOperation(String operation) throws IOException;

}
