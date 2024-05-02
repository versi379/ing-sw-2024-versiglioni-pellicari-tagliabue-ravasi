package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.observ.Observer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote, Observer {
    public void ping() throws RemoteException;

    @Override
    void update(Observable o, Object arg);

    @Override
    void onUpdate(Message message);
}
