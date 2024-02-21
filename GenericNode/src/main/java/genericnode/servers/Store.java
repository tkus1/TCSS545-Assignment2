package genericnode.servers;

import genericnode.clients.RMIClient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

// A remote interface for RMI server
public interface Store extends Remote {

    void put(String key, String value) throws RemoteException;

    String get(String key) throws RemoteException;

    void del(String key) throws RemoteException;

    ArrayList<String> store() throws RemoteException;

    void exit() throws RemoteException;
}