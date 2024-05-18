package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.net.RMI.ServerRmi;
import it.polimi.sw.GC50.net.RMI.ServerRmiImpl;
import it.polimi.sw.GC50.net.util.Server;
import it.polimi.sw.GC50.net.socket.ServerSCK;

import java.io.IOException;

public class AppServer {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        System.setProperty("server", "localhost");

        // server RMI
        new ServerRmiImpl(server, 1099).start();

        // server socket
        new Thread(new ServerSCK(server, 2012), "serverSocket").start();
    }
}
