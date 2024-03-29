package it.polimi.sw.gianpaolocugola50.net;

import it.polimi.sw.gianpaolocugola50.net.rMi.RmiServerImplementation;
import it.polimi.sw.gianpaolocugola50.net.rMi.*;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException {

        try {
            RmiServerInterface serverInterface= new RmiServerImplementation();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("default", (Remote) serverInterface);

        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

    }
}
