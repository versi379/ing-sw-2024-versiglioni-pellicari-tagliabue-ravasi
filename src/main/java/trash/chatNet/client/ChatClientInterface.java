package trash.chatNet.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientInterface extends Remote {

    void retrieveMessage(String message) throws RemoteException;

}
