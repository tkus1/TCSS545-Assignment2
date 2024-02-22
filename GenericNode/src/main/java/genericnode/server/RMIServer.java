package genericnode.server;

import genericnode.DataStorage;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RMIServer implements Store {

    public RMIServer() {
        super();
    }

    @Override
    public void put(String key, String val) {
        DataStorage.put(key, val);
    }

    @Override
    public String get(String key) {
        return DataStorage.get(key);
    }

    @Override
    public void del(String key) {
        DataStorage.del(key);
    }

    @Override
    public ArrayList<String> store() {
        return DataStorage.store();
    }

    @Override
    public void exit() throws RemoteException{
        try{
            Registry registry = LocateRegistry.getRegistry(); // Get the local registry
            registry.unbind("Store");
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupAndBindServer() {
        try {
            RMIServer obj = new RMIServer();
            Store stub = (Store) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry(); // This uses the default port 1099
            registry.bind("Store", stub);

            System.out.println("RMIServer bound in registry");
        } catch (RemoteException e) {
            System.err.println("Remote exception: " + e);
        } catch (java.rmi.AlreadyBoundException e) {
            System.err.println("AlreadyBoundException: " + e);
        }
    }

}