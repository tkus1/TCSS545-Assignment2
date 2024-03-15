package genericnode;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerRequestProcessor {

    private LockableDataStorage dataStorage;

    public ServerRequestProcessor(LockableDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public void processServerRequest(String operation, String key, String value, DataOutputStream outToServer) throws IOException {
        switch (operation) {
            case "dput1":
                handleDput1(key, outToServer);
                break;
            case "dput2":
                handleDput2(key, value, outToServer);
                break;
            case "dputabort":
                handleDputAbort(key, outToServer);
                break;
            case "ddel1":
                handleDdel1(key, outToServer);
                break;
            case "ddel2":
                handleDdel2(key, outToServer);
                break;
            case "ddelabort":
                handleDdelAbort(key, outToServer);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }

    private void handleDput1(String key, DataOutputStream outToServer) throws IOException {
        if(dataStorage.isKeyLocked(key)) {
            outToServer.writeUTF("Locked");
        } else {
            dataStorage.lockKey(key);
            outToServer.writeUTF("Accept");
        }
    }

    private void handleDput2(String key, String value, DataOutputStream outToServer) throws IOException {
        dataStorage.put(key, value);
        dataStorage.unlockKey(key);
        outToServer.writeUTF("Accept");
    }

    private void handleDputAbort(String key, DataOutputStream outToServer) throws IOException {
        dataStorage.unlockKey(key);
        outToServer.writeUTF("Accept");
    }

    private void handleDdel1(String key, DataOutputStream outToServer) throws IOException {
        if(dataStorage.isKeyLocked(key)) {
            outToServer.writeUTF("Locked");
        } else {
            dataStorage.lockKey(key);
            outToServer.writeUTF("Accept");
        }
    }

    private void handleDdel2(String key, DataOutputStream outToServer) throws IOException {
        dataStorage.del(key);
        dataStorage.unlockKey(key);
        outToServer.writeUTF("Accept");
    }

    private void handleDdelAbort(String key, DataOutputStream outToServer) throws IOException {
        dataStorage.unlockKey(key);
        outToServer.writeUTF("Accept");
    }
}