package genericnode.servers;

import genericnode.DataStorage;

import java.rmi.NoSuchObjectException;
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

}