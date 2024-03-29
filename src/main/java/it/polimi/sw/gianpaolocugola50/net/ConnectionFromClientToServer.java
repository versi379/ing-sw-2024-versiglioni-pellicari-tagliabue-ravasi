package it.polimi.sw.gianpaolocugola50.net;

import it.polimi.sw.gianpaolocugola50.net.rMi.*;
import it.polimi.sw.gianpaolocugola50.net.socket.ServerInterface;
import it.polimi.sw.gianpaolocugola50.net.socket.SocketServerImplementation;


import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectionFromClientToServer {
    static public ServerInterface createSocketConnection(int port, String host) throws IOException {
        Socket socket = new Socket(host, port);
        socket.setSoTimeout(10000);
        SocketServerImplementation serverSocket = new SocketServerImplementation(socket);
        new Thread(serverSocket).start();
        return serverSocket;
    }


    static public RmiServerInterface createRmiConnection(int port, String host) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host,port);
        RmiConnection rmiConnection= (RmiConnection) registry.lookup("default");
        RmiClientInterface rmiClient = new RmiClientImplementation();
        RmiServerInterface rmiServerInterface = rmiConnection.registerClient(rmiClient);
        return rmiServerInterface;
    }
}
