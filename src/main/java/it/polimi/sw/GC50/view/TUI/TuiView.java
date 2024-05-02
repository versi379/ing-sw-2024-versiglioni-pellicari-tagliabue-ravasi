package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.view.View;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TuiView implements View {
    public void start() {


    }

    @Override
    public String askName() {
        String name=null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert ur nickName");
        try {
            name = scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please retry");
            scanner.nextLine();
        }
        return name;
    }

    @Override
    public String askGameName() {
        String name=null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the game name");
        try {
            name = scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please retry");
            scanner.nextLine();
        }
        return name;
    }

    @Override
    public int askNumberOfPlayer() {
        Scanner scanner = new Scanner(System.in);
        int read;
        do {

            System.out.println("Insert the number of player min 2 max 4");
            try {
                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                read = 0;
                scanner.nextLine();
            }
        } while (read < 1 && read >4);
        return read;
    }

    @Override
    public void waitPlayer() {

    }
}
