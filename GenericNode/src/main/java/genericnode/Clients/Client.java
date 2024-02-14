package genericnode.Clients;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

public abstract class Client {
    public abstract void put(String key, String value) throws IOException;
    public abstract String get(String key) throws IOException;
    public abstract void del(String key) throws IOException;
    public abstract void store() throws IOException;
    public abstract void exit();
    public abstract void executeOperation(String operation, SimpleEntry<String, String> entry) throws IOException;

    public abstract void connect(String host, int port) throws IOException;
}
