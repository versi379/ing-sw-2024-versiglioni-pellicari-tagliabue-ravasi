package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.model.lobby.Lobby;
import it.polimi.sw.GC50.net.RMI.ServerRmi;
import it.polimi.sw.GC50.net.socket.ServerSCK;

import java.io.IOException;
import java.util.Scanner;

public abstract class AppServer {
    public static final int sckPort = 2012;
    public static final int rmiPort = 1099;
    public static String baseTxt = "\u001B[0m";
    public static String blueTxt = "\u001B[34m";

    public static void main(String[] args) throws IOException {
        Lobby lobby = new Lobby();
        System.setProperty("server", "localhost");

        // server socket
        new Thread(new ServerSCK(lobby, sckPort), "serverSocket").start();

        // server RMI
        new ServerRmi(lobby, rmiPort).run();

        Scanner scanner = new Scanner(System.in);
        String read;
        do {
            System.out.println(blueTxt + "Type \"exit\" to end current session" + baseTxt);
            read = scanner.nextLine();
        } while (!read.equals("exit"));
        System.exit(0);
    }
}
