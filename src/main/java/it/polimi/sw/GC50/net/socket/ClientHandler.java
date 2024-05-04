package it.polimi.sw.GC50.net.socket;


import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.util.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;

public class ClientHandler implements Runnable, ClientInterface {
    private final Socket socketClient;
    private final ServerSCK serverSCK;
    //////////////////////////////////////////
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    //////////////////////////////////////////
    private boolean alive;
    private boolean send;
    private Message messageout;
    //////////////////////////////////////////
    private final Semaphore semaphore;
    private final Semaphore semaphore2;
    //////////////////////////////////////////
    private final Server server;
    private Match match;
    private String nickName;
    //////////////////////////////////////////

    //////////////////////////////////////////


    public ClientHandler(Socket socketClient, ServerSCK serverSCK, Server server) {
        this.socketClient = socketClient;
        this.serverSCK = serverSCK;
        this.alive = true;
        this.send = false;
        semaphore = new Semaphore(1);
        semaphore2 = new Semaphore(1);
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
                Message.MessageClientToServer message = (Message.MessageClientToServer) object;
                Thread thread = new Thread(() -> {
                    switchmex(message);
                });
                thread.start();

            } catch (IOException | ClassNotFoundException e) {

            }
        }
    }

    private void outputThread() {
        System.out.println("server socket out ");
        while (this.alive) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
            if (send) {
                System.out.println("send");
                try {
                    if (messageout != null) {
                        output.writeObject(messageout);
                        output.flush();
                        output.reset();
                    }

                } catch (IOException e) {
                    System.out.println("error");

                }
                messageout = null;
                send = false;
            }
        }
    }

    private synchronized void switchmex(Message.MessageClientToServer message) {
        System.out.println(message.getRequest());

        switch (message.getRequest()) {
            case Request.MEX_CHAT:
                match.updateChat(this, message.getNickName(), (String) message.getObject());
                break;
            case Request.GET_MODEL: {
                Object object = match.getModel(message.getNickName(), this);
                setMessageout(new Message(Request.GET_MODEL_RESPONSE, object));
                break;
            }
            case Request.PLACE_CARD:
                match.updateController(Request.PLACE_CARD, this, message.getObject(), message.getNickName());
                break;
            case Request.SELECT_STARTER_FACE:
                match.updateController(Request.SELECT_STARTER_FACE, this, message.getObject(), message.getNickName());
                break;
            case Request.SELECT_OBJECTIVE_CARD:
                match.updateController(Request.SELECT_OBJECTIVE_CARD, this, message.getObject(), message.getNickName());
                break;
            case Request.DRAW_CARD:
                match.updateController(Request.DRAW_CARD, this, message.getObject(), message.getNickName());
                break;
            case Request.CREATE_GAME:
                System.out.println(message.getMatchName());
                System.out.println(message.getNickName());
                this.match = server.createMatch(this, message.getMatchName(), (int) message.getObject(), message.getNickName());
                if (match != null) {
                    setMessageout(new Message(Request.CREATE_GAME_RESPONSE, true));
                } else {
                    setMessageout(new Message(Request.CREATE_GAME_RESPONSE, false));
                }
                break;
            case Request.ENTER_GAME:
                this.match = server.enterMatch(message.getMatchName(), this, message.getNickName());
                if (match != null) {
                    setMessageout(new Message(Request.ENTER_GAME_RESPONSE, true));
                } else {
                    setMessageout(new Message(Request.ENTER_GAME_RESPONSE, false));
                }
                break;
            case Request.GET_FREE_MATCH:
                setMessageout(new Message(Request.GET_FREE_MATCH_RESPONSE, server.getFreeMatchesNames()));
                break;
            case Request.SET_NAME:
                boolean resp = server.addName(this, message.getNickName());
                setMessageout(new Message(Request.SET_NAME_RESPONSE, resp));
                break;
            case null:

                break;
            default:
                setMessageout(new Message(Request.REQUEST_NOT_AVAILABLE, null));
        }
    }


    private synchronized void setMessageout(Message messageout) {
        while (send) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
        }
        this.messageout = messageout;
        send = true;

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
    public void onUpdate(Message message) throws RemoteException {
        setMessageout(message);
    }


}
