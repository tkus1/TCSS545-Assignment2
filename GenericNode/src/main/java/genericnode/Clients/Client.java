package genericnode.Clients;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

public interface Client {
    void put(String key, String value) throws IOException;
    String get(String key) throws IOException;
    void del(String key) throws IOException;
    void store() throws IOException;
    void exit();
    void executeOperation(String operation, SimpleEntry<String, String> entry) throws IOException;

    void connect(String host, int port) throws IOException;
}
