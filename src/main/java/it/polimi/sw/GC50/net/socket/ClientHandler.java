package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Match;
import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClientHandler implements Runnable, ClientInterface {
    private final Socket socketClient;
    private final ServerSCK serverSCK;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private Match match;
    private String nickName;

    private Object lock;
    private Object lock2;


    public ClientHandler(Socket socketClient, ServerSCK serverSCK) {
        this.socketClient = socketClient;
        this.serverSCK = serverSCK;

        try {
            this.output = new ObjectOutputStream(socketClient.getOutputStream());
            this.input = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> getFreeMatch() {
        return null;
    }

    private void createGame(int numOfPl, String gameName, ClientInterface clientInterface, View view) {
    }

    private void enterGame(String gameName, ClientInterface clientInterface, String nickName) {
    }

    private void test() {
    }

    private boolean setName(String name) {
        return true;
    }

    @Override
    public void ping() throws RemoteException {

    }


    @Override
    public String getNickName() throws RemoteException {
        return null;
    }



    @Override
    public void run() {

    }
    //////////////////////////////////////////
    //OBSERVER
    ///////////////////////////////////////////
    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void onUpdate(Message message) {

    }
}
