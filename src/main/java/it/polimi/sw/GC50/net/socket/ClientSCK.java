package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.view.TypeOfView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientSCK {
    private final int port;
    private final String address;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final ExecutorService executorService;
    private int codeMatch;
    private TypeOfView typeOfView;

    public ClientSCK(int port,String address) throws IOException {
        this.port=port;
        this.address=address;
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(address, port), 1000);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }
    public void lobby(/*user wiew*/){


    }
    public void myTurn(){

    }
    public void placeCard(){

    }
    public void sendMessage(){

    }
    public void receiveMessage(){

    }
    public void getModel(){

    }
}
