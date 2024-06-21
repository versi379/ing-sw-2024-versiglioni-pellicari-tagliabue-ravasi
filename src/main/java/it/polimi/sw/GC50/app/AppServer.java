package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.net.RMI.ServerRmi;
import it.polimi.sw.GC50.model.lobby.Lobby;
import it.polimi.sw.GC50.net.socket.ServerSCK;
//import it.polimi.sw.GC50.net.socket.ServerSCK;

import java.io.IOException;

public class AppServer {
    public static final int sckPort = 2012;
    public static final int rmiPort = 1099;
    public static void main(String[] args) throws IOException {
        Lobby lobby = new Lobby();
        System.setProperty("server", "localhost");

        // server socket
        new Thread(new ServerSCK(lobby, sckPort), "serverSocket").start();

        // server RMI
        new ServerRmi(lobby, rmiPort).run();
    }
}
