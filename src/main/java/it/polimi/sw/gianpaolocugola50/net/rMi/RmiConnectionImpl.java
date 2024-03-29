package it.polimi.sw.gianpaolocugola50.net.rMi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiConnectionImpl extends UnicastRemoteObject implements RmiConnection{
    public RmiConnectionImpl() throws RemoteException {
        super();
    }

    @Override
    public RmiServerInterface registerClient(RmiClientInterface rmiClientInterface) throws RemoteException {
        RmiContainClientInterface rmiContainClientInterface= new RmiContainClientInterface(rmiClientInterface);
        RmiServerImplementation server =new RmiServerImplementation(rmiContainClientInterface);
        return server;
    }
}
