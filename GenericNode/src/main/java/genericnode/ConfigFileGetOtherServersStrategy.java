package genericnode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigFileGetOtherServersStrategy implements GetOtherServersStrategy{
    @Override
    public List<ServerConnection> getOtherServers() {
        List<ServerConnection> serverConnections = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("serverList.cfg"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) { // Ignore comments
                    String[] parts = line.split(",");
                    String serverName = parts[0];
                    int port = Integer.parseInt(parts[1]);
                    serverConnections.add(new ServerConnection(serverName, port));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverConnections;
    }
}