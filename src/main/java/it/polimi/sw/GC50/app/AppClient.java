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
import javafx.application.Platform;
import javafx.stage.Stage;

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

        printBanner();

        if (readBinaryChoice("1 for Tui , 2 for Gui") == 1) {
            view = new TuiView();
            typeview = TypeOfView.TUI;
        } else {
            typeview = TypeOfView.GUI;
            launchGui();
        }


        if (readBinaryChoice("1 for SCK connection, 2 for RMI") == 1) {
            connection = TypeOfConnection.SOCKET;
            try {
                ClientSCK client = new ClientSCK(2012, "localhost");
                Thread thread = new Thread(client);
                thread.start();
                client.addView(view, typeview);
                client.lobby();

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } else {
            connection = TypeOfConnection.RMI;
            try {
                System.out.println("Connecting to server...");
                String name = "rmi://IP:1099/server";
                ClientRmi clientRmi = new ClientRmi(name);
                clientRmi.addView(view, typeview);
                clientRmi.lobby();

            } catch (RemoteException e) {
                System.out.println("Error in connection");
            }
        }
    }

    private static int readBinaryChoice(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);

        int read;
        do {
            try {
                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                read = 0;
                scanner.nextLine();
            }
        } while (read != 1 && read != 2);
        return read;
    }

    private static void printBanner() {
        System.out.print("\u001B[33m");
        System.out.println("   ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗");
        System.out.println("  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝");
        System.out.println("  ██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗");
        System.out.println("  ██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║");
        System.out.println("  ╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║");
        System.out.println("   ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝");
        System.out.print("\u001B[0m");
    }

    private static void launchGui() {
        new Thread(() -> {
            try {
                // Ensure JavaFX is initialized
                Platform.startup(() -> {
                    try {
                        // Launch the JavaFX Application
                        GuiView guiView = new GuiView();
                        Stage stage = new Stage();
                        guiView.start(stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
