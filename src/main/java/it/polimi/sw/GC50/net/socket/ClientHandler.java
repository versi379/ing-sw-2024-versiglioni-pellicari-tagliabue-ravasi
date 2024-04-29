package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.util.ClientInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientHandler implements Runnable, ClientInterface {
    private final Socket socketClient;
    private final ServerSCK serverSCK;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    private Object lock;
    private Object lock2;
    //////////////////////////////////////////
    //Questa classe è un thread connessa ad un client; gestisce un client!!!
    ///////////////////////////////////////////


    public ClientHandler(Socket socketClient, ServerSCK serverSCK) {
        this.socketClient = socketClient;
        this.serverSCK = serverSCK;

        try{
            this.output = new ObjectOutputStream(socketClient.getOutputStream());
            this.input = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void run() {

    }
}
