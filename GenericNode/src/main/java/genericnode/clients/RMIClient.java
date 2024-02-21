package genericnode.clients;

import genericnode.servers.Store;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class RMIClient {
    private final Store stub;
    public RMIClient(Store stub) {
        this.stub = stub;
    }


    public void put(String key, String value) throws RemoteException {
        stub.put(key, value);
        System.out.print("server response:");
        System.out.println("put key=" + key);
    }


    public void get(String key) throws RemoteException {
        String val = stub.get(key);
        System.out.print("server response:");
        System.out.printf("get key=%s get val=%s%n", key, val);
    }


    public void del(String key) throws RemoteException {
        stub.del(key);
        System.out.print("server response:");
        System.out.println("delete key=" + key);
    }


    public void store() throws RemoteException {
        ArrayList<String> result = stub.store();
        System.out.println("server response:");
        for (String item : result) {
            System.out.println(item);
        }
    }

    public void exit() throws RemoteException {
        stub.exit();
        System.out.println("Closing client...");
    }


    public void executeTask(String cmd, String key, String val) throws RemoteException {

        switch (cmd) {
            case "put":
                put(key, val);
                break;
            case "get":
                get(key);
                break;
            case "del":
                del(key);
                break;
            case "store":
                store();
                break;
            case "exit":
                exit();
                break;
        }
    }
}
