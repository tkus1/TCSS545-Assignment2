package genericnode;

import genericnode.server.Server;
import genericnode.server.TCPServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CentralizeMembershipGetOtherServersStrategy implements GetOtherServersStrategy{
    private TCPServer server;

    public void setServer(TCPServer server) {
        this.server = server;
    }
    @Override
    public List<ServerConnection> getOtherServers() {
        //todo: implement
        List<ServerConnection> serverConnections = new ArrayList<>();
        ConcurrentHashMap<String, Integer> map = server.getServerList();

        for (String serverName : map.keySet()) {
            int port = map.get(serverName);
            serverConnections.add(new ServerConnection(serverName, port));
        }

        return serverConnections;
    }

}
