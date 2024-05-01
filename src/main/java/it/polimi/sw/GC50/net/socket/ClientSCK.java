package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientSCK implements Runnable {
    private View view;
    private TypeOfView typeOfView;
    private final int port;
    private final String address;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    //match
    private int codeMatch;
    private int id;
    private String nickName;
    private boolean alive;
    private boolean send;
    private Message.MessageSCK messageout;


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


    }

    private void inputThread() {
        while (alive) {

            try {
                Object object = input.readObject();
                Message.MessageSCK message = (Message.MessageSCK) object;
                switchmex(message);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("error");
            }
        }
    }


    private void outputThread() {
        while (alive) {
            while (send) {
                System.out.println("send");
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
    }

    public void setView(View view, TypeOfView typeOfView) {
        this.view = view;
        this.typeOfView = typeOfView;
    }

    public void lobby() {
        messageout = new Message.MessageSCK(Request.CREATE_GAME, "ciao", "ciao", "ciao");
        //send = true;
        System.out.println("mando?");

    }

    public void myTurn() {

    }

    public void placeCard() {

    }

    public void sendMessage() {

    }

    public void receiveMessage() {

    }

    public void getModel() {

    }

    @Override
    public void run() {
        inputThread();
        outputThread();
    }


}

