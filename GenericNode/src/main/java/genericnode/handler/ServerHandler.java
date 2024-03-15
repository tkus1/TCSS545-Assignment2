package genericnode.handler;

import genericnode.ServerConnection;
import genericnode.server.Server;

public abstract class ServerHandler implements Runnable{
    protected Server server;
    ServerHandler(Server server) {
        this.server = server;
    }
}
