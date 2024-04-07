package it.polimi.sw.gianpaolocugola50.net;

import it.polimi.sw.gianpaolocugola50.net.rmi.*;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException {

        try {
            RmiConnectionImpl rmiConnection=new RmiConnectionImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("default", rmiConnection);

        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

    }
}
