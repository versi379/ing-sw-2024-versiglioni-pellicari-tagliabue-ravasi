package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.observ.Observer;
import it.polimi.sw.GC50.net.util.*;
import it.polimi.sw.GC50.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler implements Runnable, ClientInterface , Observer {
    private final Socket socketClient;
    private final ServerSCK serverSCK;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private boolean alive;
    private boolean send;
    private Message.MessageSCK messageout;
    private final Server server;
    private Match match;
    private String nickName;
    private Object lock;
    private Object lock2;


    public ClientHandler(Socket socketClient, ServerSCK serverSCK, Server server) {
        this.socketClient = socketClient;
        this.serverSCK = serverSCK;
        this.alive = true;
        this.send = false;
        this.server = server;
        try {
            this.output = new ObjectOutputStream(socketClient.getOutputStream());
            this.input = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void inputThread() {
        System.out.println("server socket listener client");
        while (alive) {
            try {
                Object object = input.readObject();
                Message.MessageSCK message = (Message.MessageSCK) object;
                switchmex(message);
            } catch (IOException | ClassNotFoundException e) {

            }
        }
    }

    private void outputThread() {
        System.out.println("server out started");
        while (alive) {
            while (send) {
                try {
                    output.writeObject(messageout);
                    output.flush();
                    output.reset();
                    send = false;
                } catch (IOException e) {
                    send = false;
                }
            }
        }
    }

    private void switchmex(Message.MessageSCK message) {
        System.out.println(message.getRequest());
        switch (message.getRequest()) {

            case JOINGAME:
                break;
            case CREATEGAME:
                break;
            case QUITGAME:
                break;
            case GETGAME:
                break;
            case GETNUMBEROFPLAYER:
                break;
            case GETMODEL:
                break;
            case GETSTARTERCARD:
                break;
            case GETCOMMONOBJECTIVE:
                break;
            case GETSECRETOBJECTIVE:
                break;
            case SELECTOBJECTIVE:
                break;
            case PLACECARD:
                break;
            case DRAWCARDGOLD0:
                break;
            case DRAWCARDGOLD1:
                break;
            case DRAWCARDGOLD2:
                break;
            case DRAWRESOURCE0:
                break;
            case DRAWRESOURCE1:
                break;
            case DRAWRESOURCE2:
                break;
            case MEXCHAT:
                break;
            case STARTERFACE:
                break;
            case GETTURN:
                break;
            case GAMENOTFOUND:
                break;
            case Request.CREATE_GAME:
                //this.match = server.createMatch(this, message.getNumOfPl(), message.getGameName(), new View());
                break;
            case Request.ENTER_GAME:
                //enterGame(message.getGameName(), this, message.getNickName());
                break;
            case Request.GET_FREE_MATCH:
                //getFreeMatch();
                break;
            case Request.SET_NAME:
                //setName(message.getNickName());
                break;
            case null:
                //test();
                break;
        }
    }


    @Override
    public void ping() throws RemoteException {

    }


    @Override
    public void run() {
        Thread thread1 = new Thread(() -> {
            inputThread();
        });
        thread1.start();
        Thread thread2 = new Thread(() -> {
            outputThread();
        });
          thread2.start();
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
