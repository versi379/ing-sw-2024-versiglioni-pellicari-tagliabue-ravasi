package it.polimi.sw.GC50.net.observ;

import it.polimi.sw.GC50.net.util.Message;

import java.rmi.RemoteException;

public interface Observer {
    void update(Observable o, Object arg) throws RemoteException;
    void onUpdate(Message message) throws RemoteException;
}
