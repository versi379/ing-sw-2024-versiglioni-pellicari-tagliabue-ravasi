package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.net.util.RequestFromClietToServer;
import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ClientSCK implements Runnable, RequestFromClietToServer {
    private View view;
    private TypeOfView typeOfView;
    private final int port;
    private final String address;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private ArrayList<String> freeMatch;

    //match
    private String matchName;
    private String nickName;
    ///////////////////////////////////////////
    private boolean notyfyUpdateModel;
    private boolean notifyUpdateChat;
    private boolean notify;
    private boolean alive;
    private boolean send;
    private Message.MessageClientToServer messageout;


    public ClientSCK(int port, String address) throws IOException {
        this.port = port;
        this.address = address;
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(address, port), 1000);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        view = null;
        this.alive = true;
        this.send = false;
        this.notify = false;
        this.matchName = null;
        this.nickName = null;
        this.freeMatch = new ArrayList<>();
        this.notifyUpdateChat = false;
        this.notyfyUpdateModel = false;
    }

    //////////////////////////////////////////
    //COMUNICATION WITH SERVER
    ///////////////////////////////////////////

    private void inputThread() {
        while (alive) {
            try {
                Object object = input.readObject();
                Message message = (Message) object;
                Thread thread = new Thread(() -> {
                    switchmex(message);
                });
                thread.start();

            } catch (IOException | ClassNotFoundException e) {

            }
        }
    }

    private void outputThread() {
        while (alive) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
            if (send) {
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

    private synchronized void setMessageout(Message.MessageClientToServer messageout) {
        while (send) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
        }
        this.messageout = messageout;
        send = true;
    }

    private synchronized void switchmex(Message mex) {
        switch (mex.getRequest()) {
            case SET_NAME_RESPONSE: {
                boolean response = (boolean) mex.getObject();
                if (!response) {

                    nickName = null;
                }
                notify = false;
                break;
            }
            case CREATE_GAME_RESPONSE, ENTER_GAME_RESPONSE: {
                boolean response = (boolean) mex.getObject();
                if (!response) {
                    this.matchName = null;
                }
                notify = false;
                break;
            }
            case GET_FREE_MATCH_RESPONSE: {
                freeMatch = (ArrayList<String>) mex.getObject();

                notify = false;
                break;
            }
            default: {
                break;
            }
        }

    }


    private void waitNoifyfromServer() {
        notify = true;
        while (notify) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
        }

    }


    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////


    @Override
    public void addView(View view, TypeOfView typeOfView) {
        this.view = view;
        this.typeOfView = typeOfView;
    }

    public String createGame(String matchName, int numberOfPlayer) {
        this.matchName = matchName;
        setMessageout(new Message.MessageClientToServer(Request.CREATE_GAME, numberOfPlayer, matchName, nickName));
        waitNoifyfromServer();
        return this.matchName;

    }

    public String enterGame(String matchName) {
        this.matchName = matchName;
        setMessageout(new Message.MessageClientToServer(Request.ENTER_GAME, null, matchName, nickName));
        waitNoifyfromServer();
        return this.matchName;
    }

    public String setName(String name) {
        this.nickName = name;
        setMessageout(new Message.MessageClientToServer(Request.SET_NAME, null, null, name));
        waitNoifyfromServer();
        return this.nickName;
    }

    public ArrayList<String> getFreeMatch() {
        setMessageout(new Message.MessageClientToServer(Request.GET_FREE_MATCH, null, null, null));
        waitNoifyfromServer();
        return this.freeMatch;
    }


    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    @Override
    public void placeCard() {

    }


    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void selectStarterFace() {

    }

    @Override
    public void selectObjectiveCard() {

    }

    @Override
    public void drawCard() {

    }

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public void waitNotifyModelChangedFromServer() {
        while (alive) {
            this.notyfyUpdateModel = true;
            while (this.notyfyUpdateModel) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {

                }
            }
            updateView();

        }

    }

    private void updateView() {
    }

    public void sendMessage() {

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


}

