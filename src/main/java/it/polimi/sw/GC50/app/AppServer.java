package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.net.RMI.ServerRmi;
import it.polimi.sw.GC50.model.lobby.Lobby;
import it.polimi.sw.GC50.net.socket.ServerSCK;

import java.io.IOException;

public class AppServer {
    public static void main(String[] args) throws IOException {
        Lobby lobby = new Lobby();
        System.setProperty("server", "localhost");

        // server RMI
        new ServerRmi(lobby, 1099).start();

        // server socket
        new Thread(new ServerSCK(lobby, 2012), "serverSocket").start();
    }
}
