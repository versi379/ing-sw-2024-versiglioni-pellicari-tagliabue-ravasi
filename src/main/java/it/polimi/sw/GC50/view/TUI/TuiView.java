package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.objective.*;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.view.View;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TuiView implements View {
    ModelMex modelmex;

    public void start() {


    }

    @Override
    public String askName() {
        String name = null;
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
        String name = null;
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
                System.out.println("Invalid input. Please enter min2 or max4.");
                read = 0;
                scanner.nextLine();
            }
        } while (read <= 2 && read >= 4);
        return read;
    }

    @Override
    public void waitPlayer() {
        System.out.println("Waiting for other players to join the game");

    }

    @Override
    public int joinorcreate() {
        Scanner scanner = new Scanner(System.in);
        int read;
        do {

            System.out.println("Do you want to join or create a game?");
            System.out.println("1) to create a game");
            System.out.println("2) to join a game");

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

    @Override
    public void allPlayerReady() {
        System.out.println("All players are ready , the game is starting");
    }

    @Override
    public void addModel(ModelMex modelmex) {
        this.modelmex = modelmex;
    }

    @Override
    public int SelectObjectiveCard() {
        System.out.println();
        System.out.println("Select the objective card you want to play with");
        printlistObjectiveCard();
        Scanner scanner = new Scanner(System.in);
        int read;
        do {
            System.out.println();
            System.out.println("Insert the number of the objective card you want to play with");
            try {

                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                read = 0;
            }
        } while (read != 1 && read != 2);
        return read - 1;
    }

    @Override
    public Boolean selectStarterFace() {
        System.out.println("\n");

        System.out.println("Select the face of the Starter card");
        printface(modelmex.getPlayerdata().getStarterCard());
        Scanner scanner = new Scanner(System.in);
        int read;
        do {
            System.out.println("Insert the number of the face you want to play with");
            try {

                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                read = 0;
            }
        } while (read != 1 && read != 2);

        if (read == 1)
            return true;
        else
            return false;


    }

    private void printface(PhysicalCard card) {
        String[][] tmpString;
        System.out.println("Face 1");
        tmpString = card.getFront().toStringTui();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(tmpString[i][j]);
            }
            System.out.println();
        }
        System.out.println("Face 2");
        tmpString = card.getBack().toStringTui();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(tmpString[i][j]);
            }
            System.out.println();
        }

    }

    private void printlistObjectiveCard() {
        List<ObjectiveCard> objectiveCards = modelmex.getPlayerdata().getSecretObjectivesList();
        for (int i = 0; i < objectiveCards.size(); i++) {
            System.out.println();
            System.out.println(i + 1 + ") " + objectiveCards.get(i).getPointsPerCompletion() + " points for completing ");
            switch (objectiveCards.get(i).getObjective().getClass().getSimpleName()) {
                case "CaveObjective": {
                    System.out.println("Cave Objective");
                    System.out.println(((CaveObjective) objectiveCards.get(i).getObjective()).getOrientation().toString());
                    break;
                }
                case "DifferentResourcesObjective": {
                    System.out.println("Different Resources Objective");
                    // System.out.println(((DifferentResourcesObjective) objectiveCards.get(i).getObjective().));
                    break;
                }
                case "IdenticalResourcesObjective": {
                    System.out.println("Identical Resources Objective");
                    System.out.println(((IdenticalResourcesObjective) objectiveCards.get(i).getObjective()).getTargetResource().toString());
                    break;
                }
                case "MonolithObjective": {
                    System.out.println("Monolith Objective");
                    System.out.println(((MonolithObjective) objectiveCards.get(i).getObjective()).getOrientation().toString());
                    break;
                }

            }

        }
    }

}
