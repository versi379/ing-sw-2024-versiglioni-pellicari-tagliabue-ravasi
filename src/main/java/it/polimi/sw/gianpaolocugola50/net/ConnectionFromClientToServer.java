package it.polimi.sw.gianpaolocugola50.net;

import it.polimi.sw.gianpaolocugola50.net.rMi.RmiClientImplementation;
import it.polimi.sw.gianpaolocugola50.net.rMi.RmiServerInterface;
import it.polimi.sw.gianpaolocugola50.net.socket.SocketServerImplementation;
import it.polimi.sw.gianpaolocugola50.net.socket.SocketServerInterface;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectionFromClientToServer {
    static public SocketServerInterface createSocketConnection(int port, String host) throws IOException {
        Socket socket = new Socket(host, port);
        socket.setSoTimeout(10000);
        SocketServerImplementation serverSocket = new SocketServerImplementation(socket);
        new Thread(serverSocket).start();
        return serverSocket;
    }


    static public RmiServerInterface createRmiConnection(int port, String host) throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(host,port);
        RmiServerInterface server = (RmiServerInterface) registry.lookup("default");
        RmiClientImplementation client = new RmiClientImplementation();
        registry.rebind("Client", client);
        //server.inviaMessaggioAlClient("Ciao dal server!");
        client.inviaMessaggioAlServer("Ciao dal client!");
        return server;
    }
}
