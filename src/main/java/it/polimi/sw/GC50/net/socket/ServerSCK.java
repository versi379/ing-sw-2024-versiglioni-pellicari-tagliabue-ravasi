package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.util.Lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerSCK extends UnicastRemoteObject implements Runnable {
    private final Lobby lobby;
    private final int port;
    private List<ClientHandler> client;

    public ServerSCK(Lobby lobby, int port) throws IOException {
        this.lobby = lobby;
        this.port = port;
        this.client = new ArrayList<>();
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server socket ready");
        } catch (IOException e) {
            return;
        }

        while (true) {
            try {
                Socket socketClient = serverSocket.accept();
                socketClient.setSoTimeout(0);
                ClientHandler clientHandler = new ClientHandler(socketClient, this, lobby);
                client.add(clientHandler);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
                System.out.println("Client connected");
            } catch (IOException e) {
                //...
            }
        }
    }
}
