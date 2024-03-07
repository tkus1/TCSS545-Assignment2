package genericnode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class ServerNotifier {

    private List<ServerConnection> otherServers;
    private final int ATTEMPT_LIMIT;

    public ServerNotifier(List<ServerConnection> otherServers, int attemptLimit) {
        this.otherServers = otherServers;
        this.ATTEMPT_LIMIT = attemptLimit;
    }

    public void put(String key, String value) throws IOException {
        boolean success = true;
        for (ServerConnection serverConnection : otherServers) {
            if (!dput1(key, value, serverConnection)) {
                success = false;
                break;
            }
        }
        if(success) {
            for (ServerConnection serverConnection : otherServers) {
                dput2(key, value, serverConnection);
            }
        } else {
            for (ServerConnection serverConnection : otherServers) {
                dputabort(key, value, serverConnection);
            }
        }
    }

    public void del(String key) throws IOException {
        boolean success = true;
        for (ServerConnection serverConnection : otherServers) {
            if (!ddel1(key, serverConnection)) {
                success = false;
                break;
            }
        }
        if(success) {
            for (ServerConnection serverConnection : otherServers) {
                ddel2(key, serverConnection);
            }
        } else {
            for (ServerConnection serverConnection : otherServers) {
                ddelabort(key, serverConnection);
            }
        }
    }



    public boolean notifyOtherServer(String operation, String key, String value, ServerConnection serverConnection) throws IOException {
        int attemptCount = 0;
        while (attemptCount < ATTEMPT_LIMIT) {
            try {
                if(!serverConnection.connect()){
                    return true;
                }
                DataOutputStream out = serverConnection.getOutToServer();
                DataInputStream in = serverConnection.getInFromServer();

                sendOperationData(operation, key, value, out);

                String response = in.readUTF();
                if (response.equals("Accept")) {
                    serverConnection.close();
                    return true;
                } else {
                    attemptCount++;
                }

            } catch (IOException e) {
                attemptCount++;
            }
        }
        serverConnection.close();
        return false;
    }

    private void sendOperationData(String operation, String key, String value, DataOutputStream out) throws IOException {
        out.writeUTF(operation);
        switch (operation) {
            case "dput1":
            case "dput2":
            case "dputabort":
                out.writeUTF(key);
                out.writeUTF(value);
                break;
            case "ddel1":
            case "ddel2":
            case "ddelabort":
                out.writeUTF(key);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }

    private boolean dput1(String key, String value, ServerConnection serverConnection) throws IOException {
        return notifyOtherServer("dput1", key, value, serverConnection);
    }

    private void dput2(String key, String value, ServerConnection serverConnection) throws IOException {
        notifyOtherServer("dput2", key, value, serverConnection);
    }

    private void dputabort(String key,String value, ServerConnection serverConnection) throws IOException {
        notifyOtherServer("dputabort", key, value, serverConnection);
    }

    private boolean ddel1(String key, ServerConnection serverConnection) throws IOException {
        return notifyOtherServer("ddel1", key, null, serverConnection);
    }

    private void ddel2(String key, ServerConnection serverConnection) throws IOException {
        notifyOtherServer("ddel2", key, null, serverConnection);
    }

    private void ddelabort(String key, ServerConnection serverConnection) throws IOException {
        notifyOtherServer("ddelabort", key, null, serverConnection);
    }
}