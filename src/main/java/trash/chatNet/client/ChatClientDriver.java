package trash.chatNet.client;

import trash.chatNet.server.ChatServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChatClientDriver {

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        String chatServerURL = "rmi://localhost/RMIChatServer";
        ChatServerInterface chatServer = (ChatServerInterface) Naming.lookup(chatServerURL);
        new Thread(new ChatClient(args[0], chatServer)).start();
    }

}
