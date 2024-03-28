package it.polimi.sw.gianpaolocugola50.net.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClientImplementation extends UnicastRemoteObject implements RmiClientInterface {
    public RmiClientImplementation() throws RemoteException {
        super();
    }
}

