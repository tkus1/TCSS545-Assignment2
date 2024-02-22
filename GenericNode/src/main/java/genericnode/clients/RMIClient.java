package genericnode.clients;

import genericnode.servers.Store;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class RMIClient implements Client{
    private final Store stub;
    public RMIClient(String addr) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(addr);
        try {
            this.stub = (Store) registry.lookup("Store");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
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

    public void exit() {
        try {
            stub.exit();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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
