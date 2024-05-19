package it.polimi.sw.GC50.net.socket;


import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.model.lobby.Lobby;
import it.polimi.sw.GC50.net.observ.GameObservable;
import it.polimi.sw.GC50.net.util.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler implements Runnable, ClientInterface {
    private final Socket socketClient;
    private final ServerSCK serverSCK;
    //////////////////////////////////////////
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    //////////////////////////////////////////
    private boolean alive;
    //////////////////////////////////////////
    //////////////////////////////////////////
    private final Lobby lobby;
    private GameController match;
    private String nickname;
    //////////////////////////////////////////
    private final ExecutorService executorService;
    //////////////////////////////////////////


    public ClientHandler(Socket socketClient, ServerSCK serverSCK, Lobby lobby) {
        this.socketClient = socketClient;
        this.serverSCK = serverSCK;
        this.alive = true;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.lobby = lobby;
        try {
            this.output = new ObjectOutputStream(socketClient.getOutputStream());
            this.input = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void inputThread() {
        System.out.println("server socket listener client");
        executorService.execute(() -> {
            while (!executorService.isShutdown()) {
                try {
                    Object object = input.readObject();
                    Message.MessageClientToServer message = (Message.MessageClientToServer) object;
                    switchmex(message);
                } catch (IOException | ClassNotFoundException e) {

                }
            }
        });
    }

    private synchronized void switchmex(Message.MessageClientToServer message) {
        //System.out.println(message.getRequest());

        switch (message.getRequest()) {
            case Request.MEX_CHAT:
                match.updateChat((String) message.getObject(), this);
                break;
            case Request.GET_MODEL: {
                Object object = match.getModel(this);
                System.out.println(object.toString());
                setMessageout(new Message(Request.GET_MODEL_RESPONSE, object));
                break;
            }
            case Request.PLACE_CARD:
                match.updateController(Request.PLACE_CARD, message.getObject(), this);
                break;
            case Request.SELECT_STARTER_FACE:
                match.updateController(Request.SELECT_STARTER_FACE, message.getObject(), this);
                break;
            case Request.SELECT_OBJECTIVE_CARD:
                match.updateController(Request.SELECT_OBJECTIVE_CARD, message.getObject(), this);
                break;
            case Request.DRAW_CARD:
                match.updateController(Request.DRAW_CARD, message.getObject(), this);
                break;
            case Request.CREATE_GAME:
                System.out.println(message.getMatchName());
                System.out.println(message.getNickName());
                this.match = lobby.createMatch(this, message.getMatchName(), (int) message.getObject(), message.getNickName());
                if (match != null) {
                    setMessageout(new Message(Request.CREATE_GAME_RESPONSE, true));
                } else {
                    setMessageout(new Message(Request.CREATE_GAME_RESPONSE, false));
                }
                break;
            case Request.ENTER_GAME:
                this.match = lobby.joinMatch(this, message.getMatchName(), message.getNickName());
                if (match != null) {
                    setMessageout(new Message(Request.ENTER_GAME_RESPONSE, true));
                } else {
                    setMessageout(new Message(Request.ENTER_GAME_RESPONSE, false));
                }
                break;
            case Request.GET_FREE_MATCH:
                setMessageout(new Message(Request.GET_FREE_MATCH_RESPONSE, lobby.getFreeMatches()));
                break;
            case Request.SET_NAME:
                boolean resp = lobby.addPlayer(this, message.getNickName());
                setMessageout(new Message(Request.SET_NAME_RESPONSE, resp));
                break;
            case null:

                break;
            default:
                setMessageout(new Message(Request.REQUEST_NOT_AVAILABLE, null));
        }
    }

    synchronized private void setMessageout(Message messageout) {
        try {
            if (messageout != null) {
                // System.out.println(messageout.getRequest());
                output.writeObject(messageout);
                output.flush();
                output.reset();
            }
        } catch (IOException e) {
            System.out.println("error");
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
        //  Thread thread2 = new Thread(() -> {
        //    outputThread();
        // });
        //thread2.start();
    }

    //////////////////////////////////////////
    //OBSERVER
    ///////////////////////////////////////////
    @Override
    public void update(GameObservable o, Request request, Object arg) {

    }

    @Override
    synchronized public void onUpdate(Message message) throws RemoteException {
        setMessageout(message);
    }
}
