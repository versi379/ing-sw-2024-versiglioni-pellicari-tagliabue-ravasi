package it.polimi.sw.GC50.app;

import it.polimi.sw.GC50.net.client.Client;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.TUI.TuiView;
import it.polimi.sw.GC50.view.View;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppClient {
    private static View view;
    public static final String serverSckPort = "2012";
    public static final String serverRmiPort = "1099";

    public static void main(String[] args) {
        printBanner();

        System.out.println(TuiView.blueTxt + "Select the desired interface style:" + TuiView.baseTxt);
        System.out.println("1) command line interface");
        System.out.println("2) graphic interface");

        if (readBinaryChoice() == 1) {
            view = new TuiView();
        } else {
            view = new GuiView();
        }

        System.exit(new Client(view).run());
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
            System.out.println(TuiView.redTxt + "Error: Invalid input, please enter 1 or 2." + TuiView.baseTxt);
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

    public static View getView() {
        return view;
    }
}
