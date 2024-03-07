package genericnode;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerRequestProcessor {

    private DataStorage dataStorage;
    private KeyLockManager keyLockManager;

    public ServerRequestProcessor(DataStorage dataStorage, KeyLockManager keyLockManager) {
        this.dataStorage = dataStorage;
        this.keyLockManager = keyLockManager;
    }

    public void processServerRequest(String operation, String key, String value, DataOutputStream outToServer, DataStorage dataStorage, KeyLockManager keyLockManager) throws IOException {
        switch (operation) {
            case "dput1":
                handleDput1(key, outToServer, keyLockManager);
                break;
            case "dput2":
                handleDput2(key, value, outToServer, dataStorage, keyLockManager);
                break;
            case "dputabort":
                handleDputAbort(key, outToServer, keyLockManager);
                break;
            case "ddel1":
                handleDdel1(key, outToServer, keyLockManager);
                break;
            case "ddel2":
                handleDdel2(key, outToServer, dataStorage, keyLockManager);
                break;
            case "ddelabort":
                handleDdelAbort(key, outToServer, keyLockManager);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }

    private void handleDput1(String key, DataOutputStream outToServer, KeyLockManager keyLockManager) throws IOException {
        if(keyLockManager.isKeyLocked(key)) {
            outToServer.writeUTF("Locked");
        } else {
            keyLockManager.lockKey(key);
            outToServer.writeUTF("Accept");
        }
    }

    private void handleDput2(String key, String value, DataOutputStream outToServer, DataStorage dataStorage, KeyLockManager keyLockManager) throws IOException {
        dataStorage.put(key, value);
        keyLockManager.unlockKey(key);
        outToServer.writeUTF("Accept");
    }

    private void handleDputAbort(String key, DataOutputStream outToServer, KeyLockManager keyLockManager) throws IOException {
        keyLockManager.unlockKey(key);
        outToServer.writeUTF("Accept");
    }

    private void handleDdel1(String key, DataOutputStream outToServer, KeyLockManager keyLockManager) throws IOException {
        if(keyLockManager.isKeyLocked(key)) {
            outToServer.writeUTF("Locked");
        } else {
            keyLockManager.lockKey(key);
            outToServer.writeUTF("Accept");
        }
    }

    private void handleDdel2(String key, DataOutputStream outToServer, DataStorage dataStorage, KeyLockManager keyLockManager) throws IOException {
        dataStorage.del(key);
        keyLockManager.unlockKey(key);
        outToServer.writeUTF("Accept");
    }

    private void handleDdelAbort(String key, DataOutputStream outToServer, KeyLockManager keyLockManager) throws IOException {
        keyLockManager.unlockKey(key);
        outToServer.writeUTF("Accept");
    }
}