package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.observ.Observer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote,Observer{
    public void ping() throws RemoteException;

    @Override
    public void update(Observable o, Object arg) throws RemoteException;

    @Override
    public void onUpdate(Message message) throws RemoteException;
}
