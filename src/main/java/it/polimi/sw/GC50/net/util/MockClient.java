package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.net.observ.GameObservable;

import java.rmi.RemoteException;

public class MockClient implements ClientInterface {

    public MockClient() {

    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void update(GameObservable o, Request request, Object arg) throws RemoteException {

    }

    @Override
    public void onUpdate(Message message) throws RemoteException {

    }
}
