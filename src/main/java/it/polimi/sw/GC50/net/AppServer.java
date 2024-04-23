package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.net.RMI.ServerRmi;
import it.polimi.sw.GC50.net.RMI.ServerRmiImpl;
import it.polimi.sw.GC50.net.socket.ServerSCK;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppServer {
    public static void main(String[] args) throws IOException {

        Server server= new Server();
        //SERVER RMI
        System.setProperty("java.rmi.server.hostname", "localhost");
        ServerRmi serverR= new ServerRmiImpl(server,1099);
        serverR.start(serverR);


        //server socket
        ServerSCK serverSck=new ServerSCK(server,2012);

        Thread thread = new Thread (serverSck, "serverSocket");
        thread.start();

    }
}
