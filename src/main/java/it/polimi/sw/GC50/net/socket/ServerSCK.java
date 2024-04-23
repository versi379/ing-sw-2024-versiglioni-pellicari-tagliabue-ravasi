package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerSCK extends UnicastRemoteObject implements Runnable {
    private final Server server;
    private final int port;
    private List<ClientHandler> client;

    public ServerSCK(Server server, int port) throws IOException{
        this.server = server;
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(port);

        } catch (IOException e){
            return;
        }
        while(true){
            try{
                Socket socketClient = serverSocket.accept();
                socketClient.setSoTimeout(0);
                ClientHandler clientHandler = new ClientHandler(socketClient,server);
                client.add(clientHandler);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
                System.out.println("client connected");
            } catch (IOException e){
            }
        }

    }
    public synchronized Object getListOpenGame(){}
}
