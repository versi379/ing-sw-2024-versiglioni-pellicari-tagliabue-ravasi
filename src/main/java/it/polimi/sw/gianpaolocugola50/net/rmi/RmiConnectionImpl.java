package it.polimi.sw.gianpaolocugola50.net.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiConnectionImpl extends UnicastRemoteObject implements RmiConnection{
    public RmiConnectionImpl() throws RemoteException {
        super();
    }

    @Override
    public RmiServerInterface registerClient(RmiClientInterface rmiClientInterface) throws RemoteException {

        RmiServerImplementation server =new RmiServerImplementation(rmiClientInterface);
        return server;
    }
}
