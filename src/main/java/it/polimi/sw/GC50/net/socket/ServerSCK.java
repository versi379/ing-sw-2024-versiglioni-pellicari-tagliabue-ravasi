package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.model.lobby.Lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * class that represents the server for the socket connection
 * it extends UnicastRemoteObject and implements Runnable
 * it implements the run method that waits for the connection of a client
 */
public class ServerSCK extends UnicastRemoteObject implements Runnable {
    private final Lobby lobby;
    private final int port;
    private final List<ClientHandler> clients;

    /**
     * constructor of the class
     *
     * @param lobby is the lobby of the game
     * @param port  is the port number
     * @throws IOException if an error occurs
     */
    public ServerSCK(Lobby lobby, int port) throws IOException {
        this.lobby = lobby;
        this.port = port;
        this.clients = new ArrayList<>();
    }

    /**
     * method that waits for the connection of a client
     * this method starts the server and waits the connection of a client
     * it creates a new client handler for each client that connects
     * and starts a new thread for each client handler
     * the client handler is added to the list of clients
     * the method prints a message when a client connects
     * if an error occurs the method returns
     * the method is an infinite loop
     */
    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server socket ready");
        } catch (IOException e) {
            System.out.println("Error to start server socket ");
            return;
        }

        while (true) {
            try {
                Socket socketClient = serverSocket.accept();
                socketClient.setSoTimeout(0);
                ClientHandler clientHandler = new ClientHandler(socketClient, lobby);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
                System.out.println("Client connected");
            } catch (IOException ignored) {
            }
        }
    }
}
