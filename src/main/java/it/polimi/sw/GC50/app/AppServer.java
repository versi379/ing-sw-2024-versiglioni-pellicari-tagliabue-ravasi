package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.net.RMI.ServerRmi;
import it.polimi.sw.GC50.net.RMI.ServerRmiImpl;
import it.polimi.sw.GC50.net.util.Server;
import it.polimi.sw.GC50.net.socket.ServerSCK;

import java.io.IOException;

public class AppServer {
    public static void main(String[] args) throws IOException {

        Server server = new Server();
        //SERVER RMI
        System.out.println("Server RMI");
        System.setProperty("server", "localhost");
        ServerRmi serverR = new ServerRmiImpl(server, 1099);
        serverR.start(serverR);


        //server socket
        ServerSCK serverSck = new ServerSCK(server, 2012);

        Thread thread = new Thread(serverSck, "serverSocket");
        thread.start();

    }
}
