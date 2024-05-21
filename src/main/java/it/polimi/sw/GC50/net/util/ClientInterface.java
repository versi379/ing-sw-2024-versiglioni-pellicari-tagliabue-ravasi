package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.net.observ.GameObserver;
import it.polimi.sw.GC50.view.GameView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote, GameObserver {
    public void ping() throws RemoteException;

    @Override
    public void update(Request request, Object arg, GameView gameView) throws RemoteException;

    /*
    @Override
    public void onUpdate(Message message) throws RemoteException;
     */
}
