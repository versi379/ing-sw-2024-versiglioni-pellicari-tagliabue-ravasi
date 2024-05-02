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
    ///////////////////////////////////////////
    private boolean notify;
    private boolean alive;
    private boolean send;
    private Message.MessageSCK messageout;
    private int test=0;


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

    }

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

    private synchronized void setMessageout(Message.MessageSCK messageout) {
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
                notify = false;
                System.out.println(test);
                test++;
                System.out.println("Recived responce from the server");
                boolean response = (boolean) mex.getObject();
                if (!response) {
                    nickName = null;
                } else {
                    System.out.println("name set");
                }
                break;
            }
            default: {
                break;
            }
        }

    }

    public void setView(View view, TypeOfView typeOfView) {
        this.view = view;
        this.typeOfView = typeOfView;
    }

    public void lobby() {
        //messageout = new Message.MessageSCK(Request.CREATE_GAME, "ciao", "ciao", "ciao");
        //send = true;
        for (int i = 0; i < 100; i++) {
            setName("luca");
        }

    }
    private void waitNoifyfromServer() {
        {
            notify=true;
            while (notify) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {

                }
            }

        }
    }


    private synchronized void setSend(boolean send) {
        this.send = send;
    }

    public void setName(String name) {
        setMessageout(new Message.MessageSCK(Request.SET_NAME, null, null, name));
        waitNoifyfromServer();
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

