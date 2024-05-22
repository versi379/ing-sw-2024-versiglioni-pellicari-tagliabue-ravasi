package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.net.Messages.Message;
import it.polimi.sw.GC50.view.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote, GameObserver {
    public void ping() throws RemoteException;

    @Override
    public void update(Request request, Message message) throws RemoteException;

    /*
    @Override
    public void onUpdate(Message message) throws RemoteException;
     */
}
