package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.net.Messages.Message;

import java.rmi.RemoteException;

public class MockClient implements ClientInterface {

    public MockClient() {

    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void update(Notify notify, Message message) throws RemoteException {

    }
}
