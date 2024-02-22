package genericnode.handlers;

import genericnode.servers.Server;

public abstract class ClientHandler implements Runnable{


    protected Server server;

    public ClientHandler (Server server)  {
        this.server = server;
    }

}
