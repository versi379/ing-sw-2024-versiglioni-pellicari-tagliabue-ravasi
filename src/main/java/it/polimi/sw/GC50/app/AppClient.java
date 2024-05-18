package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.TypeOfConnection;
import it.polimi.sw.GC50.net.socket.ClientSCK;

import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.TUI.TuiView;
import it.polimi.sw.GC50.view.ViewType;
import it.polimi.sw.GC50.view.View;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class AppClient {
    public static void main(String[] args) throws InterruptedException {
        TypeOfConnection connection;
        View view;
        ViewType viewType;

        printBanner();

        if (readBinaryChoice("1 for Tui , 2 for Gui") == 1) {
            view = new TuiView();
            viewType = ViewType.TUI;
        } else {
            view = new GuiView();
            viewType = ViewType.GUI;
            launchGui();
        }

        // setup connection
        if (viewType == ViewType.TUI) {
            if (readBinaryChoice("1 for SCK connection, 2 for RMI") == 1) {
                setupSocket(view, viewType);
            } else {
                setupRMI(view, viewType);
            }
        } else {
            // Via GUI
        }
        System.out.println("ciao");
    }

    private static void setupSocket(View view, ViewType viewType) {
        try {
            ClientSCK client = new ClientSCK(2012, "localhost");
            new Thread(client).start();
            client.addView(view, viewType);
            client.lobby();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void setupRMI(View view, ViewType viewType) {
        try {
            System.out.println("Connecting to server...");
            String name = "rmi://localhost:1099/server";
            ClientRmi client = new ClientRmi(name);
            client.addView(view, viewType);
            client.lobby();
        } catch (RemoteException e) {
            System.out.println("Error in connection");
        }
    }

    private static int readBinaryChoice(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);

        int read;
        try {
            read = scanner.nextInt();
        } catch (InputMismatchException e) {
            read = 0;
            scanner.nextLine();
        }
        while (read != 1 && read != 2) {
            System.out.println("Invalid input. Please enter 1 or 2.");
            try {
                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
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
