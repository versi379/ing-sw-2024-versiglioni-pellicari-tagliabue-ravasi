package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.net.observ.GameObservable;
import it.polimi.sw.GC50.net.observ.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote, GameObserver {
    public void ping() throws RemoteException;

    @Override
    public void update(GameObservable o, Request request, Object arg) throws RemoteException;

    @Override
    public void onUpdate(Message message) throws RemoteException;
}
