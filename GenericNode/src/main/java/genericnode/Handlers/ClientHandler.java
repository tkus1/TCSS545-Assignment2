package genericnode.Handlers;

import genericnode.Servers.Server;
import genericnode.Servers.TCPServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class ClientHandler implements Runnable{


    protected Server server;

    public ClientHandler (Server server)  {
        this.server = server;
    }

}
