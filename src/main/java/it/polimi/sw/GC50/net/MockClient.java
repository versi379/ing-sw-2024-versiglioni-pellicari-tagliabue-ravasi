package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.net.ClientInterface;
import it.polimi.sw.GC50.net.messages.Message;
import it.polimi.sw.GC50.net.messages.Notify;
import javafx.util.Pair;

import java.rmi.RemoteException;

public class MockClient implements ClientInterface {
    private Pair<Notify, Message> notify;

    public MockClient() {
        notify = null;
    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void update(Notify notify, Message message) throws RemoteException {
        this.notify = new Pair<>(notify, message);
    }

    public Notify getNotify() {
        return notify.getKey();
    }

    public Message getMessage() {
        return notify.getValue();
    }
}
