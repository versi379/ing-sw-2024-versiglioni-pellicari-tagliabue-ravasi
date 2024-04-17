package it.polimi.sw.GC50.net.chat.server;

import it.polimi.sw.GC50.net.chat.client.ChatClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServerInterface extends Remote {

    void registerChatClient(ChatClientInterface chatClient) throws RemoteException;

    void broadcastMessage(String message) throws RemoteException;

}
