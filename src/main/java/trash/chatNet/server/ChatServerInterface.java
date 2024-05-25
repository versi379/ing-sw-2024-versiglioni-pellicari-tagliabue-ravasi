package trash.chatNet.server;

import trash.chatNet.client.ChatClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServerInterface extends Remote {

    void registerChatClient(ChatClientInterface chatClient) throws RemoteException;

    void broadcastMessage(String message) throws RemoteException;

}
