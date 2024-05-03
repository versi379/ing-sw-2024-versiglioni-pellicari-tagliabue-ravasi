package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.TypeOfConnection;
import it.polimi.sw.GC50.net.socket.ClientSCK;

import it.polimi.sw.GC50.net.util.RequestFromClietToServer;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.TUI.TuiView;
import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Observer;
import java.util.Scanner;


public class AppClient {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        TypeOfConnection connection;
        View view = null;
        TypeOfView typeview = null;
        RequestFromClietToServer requestFromClietToServer = null;

        int read;

        do {
            System.out.println("1 for Tui , 2 for Gui");
            try {
                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                read = 0;
                scanner.nextLine();
            }
        } while (read != 1 && read != 2);

        if (read == 1) {
            view = new TuiView();
            typeview = TypeOfView.TUI;
        } else if (read == 2) {
            view = new GuiView();
            typeview = TypeOfView.GUI;
        }


        do {
            System.out.println("1 for SCK connection, 2 for RMI");
            try {
                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                read = 0;
                scanner.nextLine();
            }
        } while (read != 1 && read != 2);

        if (read == 1) {
            connection = TypeOfConnection.SOCKET;

            try {

                requestFromClietToServer = new ClientSCK(2012, "localhost");
                Thread thread = new Thread((ClientSCK) requestFromClietToServer);
                thread.start();
                requestFromClietToServer.addView(view, typeview);
                System.out.println(requestFromClietToServer.setName("luca"));
                System.out.println(requestFromClietToServer.createGame("game3", 2));
                System.out.println(requestFromClietToServer.setName("luca2"));
                System.out.println(requestFromClietToServer.createGame("game4", 2));


            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        } else if (read == 2) {
            connection = TypeOfConnection.RMI;
            try {
                System.out.println("Connecting to server...");
                ClientRmi clientRmi = new ClientRmi("rmi://localhost:1099//server");

                //requestFromClietToServer = new ClientRmi("default");

                requestFromClietToServer.addView(view, typeview);
                System.out.println(requestFromClietToServer.setName("luca3"));
                System.out.println(requestFromClietToServer.createGame("game1", 3));
                System.out.println(requestFromClietToServer.setName("luca4"));
                System.out.println(requestFromClietToServer.createGame("game2", 3));


            } catch (RemoteException e) {
                System.out.println("Error in connection");
            }
        }


    }


}
