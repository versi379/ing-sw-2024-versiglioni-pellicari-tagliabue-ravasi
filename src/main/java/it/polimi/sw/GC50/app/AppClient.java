package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.socket.ClientSCK;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.TUI.TuiView;
import it.polimi.sw.GC50.view.View;
import it.polimi.sw.GC50.view.ViewType;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class AppClient {
    private static View view;
    private static ViewType viewType;
    public static void main(String[] args) {
        printBanner();

        if (readBinaryChoice("Selezionare la tipologia di interfaccia:" +
                "\n1) terminale" +
                "\n2) interfaccia grafica") == 1) {
            view = new TuiView();
            viewType = ViewType.TUI;
        } else {
            view = new GuiView();
            viewType = ViewType.GUI;
            launchGui();
        }

        // setup connection (only TUI)
        if (viewType == ViewType.TUI) {
            if (readBinaryChoice("Selezionare la tipologia di connessione:" +
                    "\n1) Socket" +
                    "\n2) RMI") == 1) {
                setupSocket(view, viewType);
            } else {
                setupRMI(view, viewType);
            }
        }
        System.err.println("Bye");
    }

    public static void setupSocket(View view, ViewType viewType) {
        try {
            ClientSCK client = new ClientSCK(2012, "localhost");
            new Thread(client).start();
            client.addView(view);
            if (viewType == ViewType.TUI) {
                client.lobby();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void setupRMI(View view, ViewType viewType) {
        try {
            System.out.println("Connecting to server...");
            String name = "rmi://localhost:1099";
            ClientRmi client = new ClientRmi(name);
            client.addView(view, viewType);
            client.run();
        } catch (RemoteException e) {
            System.err.println("Error in connection");
        }
    }

    private static int readBinaryChoice(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);

        int read;
        try {
            System.out.print("> ");
            read = scanner.nextInt();
        } catch (InputMismatchException e) {
            read = 0;
            scanner.nextLine();
        }
        while (read != 1 && read != 2) {
            System.out.println("Invalid input. Please enter 1 or 2.");
            try {
                System.out.print("> ");
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

    public static View getView() {
        return view;
    }

    public static ViewType getViewType() {
        return viewType;
    }

}
