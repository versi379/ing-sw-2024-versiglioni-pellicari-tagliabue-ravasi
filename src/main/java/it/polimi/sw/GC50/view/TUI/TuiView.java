package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.objective.*;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.View;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TuiView implements View {
    GameView gameView;

    public TuiView() {
    }

    public void start() {
    }

    @Override
    public String selectName() {
        return readString("Insert your nickname");
    }

    @Override
    public int selectJoinOrCreate() {
        return readInt("Do you want to join or create a game?" +
                "\n1) create a game" +
                "\n2) join a game" +
                "\n3) quit", 1, 3);
    }

    @Override
    public String selectGameName() {
        return readString("Insert the game name");
    }

    @Override
    public int selectNumberOfPlayers() {
        return readInt("Insert the number of players", 2, 3);
    }

    @Override
    public int selectObjectiveCard() {
        System.out.println();
        System.out.println("Select the objective card you want to play with");
        printSecretObjectiveChoice();

        return readInt("Insert the number of the objective card you want to play with",
                1, gameView.getSecreteObjectivesList().size()) - 1;
    }

    private void printSecretObjectiveChoice() {
        List<ObjectiveCard> objectiveCards = gameView.getSecreteObjectivesList();
        for (int i = 0; i < objectiveCards.size(); i++) {
            System.out.println();
            System.out.println((i + 1) + ") " + objectiveCards.get(i).toStringTUI());
        }
        System.out.println();
    }

    @Override
    public boolean selectStarterFace() {
        System.out.println();
        System.out.println("Select the face of the Starter card");
        printStarterCardChoice(gameView.getStarterCard());

        return readInt("Insert the index of the face you want to play with", 1, 2) == 1;
    }

    private void printStarterCardChoice(PhysicalCard card) {
        String[][] cardTUI;

        System.out.println();
        System.out.println("1) Front");
        cardTUI = card.getFront().toStringTUI();
        for (int i = cardTUI[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < cardTUI.length; j++) {
                System.out.print(cardTUI[j][i]);
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("2) Back");
        cardTUI = card.getBack().toStringTUI();
        for (int i = cardTUI[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < cardTUI.length; j++) {
                System.out.print(cardTUI[j][i]);
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public PlaceCardMex selectPlaceCard() {
        System.out.println();
        printPlayerArea(gameView.getPlayerNickname());
        ModelPrinter.printHand(gameView.getHand());
        return new PlaceCardMex(
                readInt("Select the card you want to place",
                        1, gameView.getHand().size()) - 1,
                readInt("Select the face of the card", 1, 2) == 1,
                readInt("Select the x coordinate", 1, gameView.getPlayerArea(gameView.getPlayerNickname())
                        .getCardsMatrix().length()) - 1,
                readInt("Select the y coordinate", 1,gameView.getPlayerArea(gameView.getPlayerNickname())
                        .getCardsMatrix().length()) - 1);
    }

    @Override
    public void printPlayerArea(String nickname) {
        ModelPrinter.printPlayerArea(nickname, gameView.getPlayerArea(nickname));
    }

    @Override
    public DrawingPosition selectDrawingPosition() {
        ModelPrinter.printDecks(gameView.getDecks());
        return DrawingPosition.values()[readInt("Select the card you want to draw",
                1, DrawingPosition.values().length) - 1];
    }

    @Override
    public void waitPlayers() {
        System.out.println("Waiting for other players to join the game");
    }

    @Override
    public void setup() {
        System.out.println("All players are ready, the game is starting");
    }

    @Override
    public void addModel(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void updateChat(Chat chat) {
    }

    @Override
    public int game() {
        return 0;
    }

    @Override
    public void updateBoard() {
    }

    private static String readString(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);

        String read;
        try {
            System.out.print("> ");
            read = scanner.nextLine();
        } catch (InputMismatchException e) {
            read = null;
            scanner.nextLine();
        }
        while (read == null) {
            System.out.println("Invalid input. Please retry");
            try {
                System.out.print("> ");
                read = scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
        return read;
    }

    private static int readInt(String message, int min, int range) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);

        int read;
        try {
            System.out.print("> ");
            read = scanner.nextInt();
        } catch (InputMismatchException e) {
            read = min - 1;
            scanner.nextLine();
        }
        while (read < min || read >= min + range) {
            System.out.println("Invalid input. Please enter a number between " + min + " and " + (min + range - 1));
            try {
                System.out.print("> ");
                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
        return read;
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }
}
