package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.view.GameView;

import java.rmi.RemoteException;

public class MockClient implements ClientInterface {

    public MockClient() {

    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void update(Request request, Object arg, GameView gameView) throws RemoteException {

    }
}
