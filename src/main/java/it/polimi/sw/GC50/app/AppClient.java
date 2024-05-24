package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.TUI.TuiView;
import it.polimi.sw.GC50.view.View;
import it.polimi.sw.GC50.view.ViewType;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class AppClient {
    private static View view;
    private static ViewType viewType;
    private static Thread clientThread;

    public static void main(String[] args) {
        printBanner();

        System.out.println(TuiView.blueTxt + "Select the desired interface style:" + TuiView.baseTxt);
        System.out.println("1) command line interface");
        System.out.println("2) graphic interface");

        if (readBinaryChoice() == 1) {
            view = new TuiView();
            viewType = ViewType.TUI;
        } else {
            view = new GuiView();
            launchGui((GuiView) view);
            viewType = ViewType.GUI;
        }

        // setup connection (only TUI)
        if (viewType.equals(ViewType.TUI)) {
            System.out.println(TuiView.blueTxt + "Select the desired connection technology:" + TuiView.baseTxt);
            System.out.println("1) socket");
            System.out.println("2) RMI");

            if (readBinaryChoice() == 1) {
                setupSocket(view, viewType);
            } else {
                setupRMI(view, viewType);
            }
        } else { // GUI
            while (((GuiView) view).getNetController() == null) {
                System.out.println("ATTENDO SCHERMATA NET");
            }
            while(!((GuiView) view).getNetController().isnetSetted()) {
                System.out.println("ATTENDO SCELTA NET");
            }
            if (((GuiView) view).getNetController().getNetSelected() == 1) {
                System.out.println("LANCIO SOCKET");
                setupSocket(view, viewType);
            } else {
                System.out.println("LANCIO RMI");
                setupRMI(view, viewType);
            }
        }
        System.err.println("Bye");
    }

    public static void setupSocket(View view, ViewType viewType) {
        /*
        try {
            ClientSCK client = new ClientSCK(2012, "localhost");
            Thread clientThread = new Thread(client);
            clientThread.start();
            client.addView(view);
            client.lobby();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
         */
    }

    public static void setupRMI(View view, ViewType viewType) {
        try {
            System.out.println("Connecting to server...");
            String name = "rmi://localhost:1099";
            ClientRmi client = new ClientRmi(name, view);
            client.run();
        } catch (RemoteException e) {
            System.err.println("Error in connection");
        }
    }

    private static int readBinaryChoice() {
        Scanner scanner = new Scanner(System.in);

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

    private static void launchGui(GuiView view) {
        new Thread(() -> {
            try {
                // Ensure JavaFX is initialized
                Platform.startup(() -> {
                    try {
                        // Launch the JavaFX Application
                        Stage stage = new Stage();
                        view.start(stage);
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

    public static Thread getClientThread() {
        return clientThread;
    }
}
