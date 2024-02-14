package genericnode.Servers;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Server {

    public abstract void startServer(int port) throws IOException;

    public abstract void put(String key, String value) throws IOException;
    public abstract String get(String key) throws IOException;
    public abstract void del(String key);
    public abstract void store() throws IOException;
    public abstract void exit() throws IOException;
    public abstract void executeOperation(String operation) throws IOException;

}
