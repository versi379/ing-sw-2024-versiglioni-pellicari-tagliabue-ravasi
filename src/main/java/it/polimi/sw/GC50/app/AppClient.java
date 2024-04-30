package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.TypeOfConnection;
import it.polimi.sw.GC50.net.socket.ClientSCK;

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

        int read;

        do {
            System.out.println("1 for Tui connection, 2 for Gui");
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
            ClientSCK clientSCK = null;
            try {
                clientSCK = new ClientSCK(2012, "localhost");
                clientSCK.setView(view, typeview);
                clientSCK.lobby();

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        } else if (read == 2) {
            connection = TypeOfConnection.RMI;
            try {
                ClientInterface client = new ClientRmi("server");
                ((ClientRmi) client).addView(view, typeview);
                ((ClientRmi) client).lobby();

            } catch (RemoteException e) {

            }
        }


    }


}
