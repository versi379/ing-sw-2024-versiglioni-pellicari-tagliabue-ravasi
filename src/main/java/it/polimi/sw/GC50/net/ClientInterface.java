package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.net.messages.Message;
import it.polimi.sw.GC50.net.messages.Notify;
import it.polimi.sw.GC50.view.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote, GameObserver {
    void ping() throws RemoteException;

    @Override
    void update(Notify notify, Message message) throws RemoteException;
}
