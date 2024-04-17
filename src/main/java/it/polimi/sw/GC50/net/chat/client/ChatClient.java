package it.polimi.sw.GC50.net.chat.client;

import it.polimi.sw.GC50.net.chat.server.ChatServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ChatClient extends UnicastRemoteObject implements ChatClientInterface, Runnable {

    private ChatServerInterface chatServer;
    private String name = null;

    protected ChatClient(String name, ChatServerInterface chatServer) throws RemoteException {
        this.name = name;
        this.chatServer = chatServer;
        chatServer.registerChatClient(this);
    }

    @Override
    public void retrieveMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            message = scanner.nextLine();
            try {
                chatServer.broadcastMessage(name + " : " + message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
