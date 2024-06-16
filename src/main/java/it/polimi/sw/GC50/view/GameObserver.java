package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.net.messages.Message;
import it.polimi.sw.GC50.net.messages.Notify;

import java.rmi.RemoteException;

public interface GameObserver {
    void update(Notify notify, Message message) throws RemoteException;
}
