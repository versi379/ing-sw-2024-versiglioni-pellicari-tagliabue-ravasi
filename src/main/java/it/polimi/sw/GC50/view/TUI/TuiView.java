package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.objective.*;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.view.View;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TuiView implements View {
    ModelMex modelMex;
    PrintGameArea printGameArea;

    public TuiView() {
        printGameArea = new PrintGameArea();
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
        printListObjectiveCard();

        return readInt("Insert the number of the objective card you want to play with", 1, 2) - 1;
    }

    @Override
    public boolean selectStarterFace() {
        System.out.println();
        System.out.println("Select the face of the Starter card");
        printFaces(modelMex.getPlayerdata().getStarterCard());

        return readInt("Insert the number of the face you want to play with", 1, 2) == 1;
    }

    @Override
    public PlaceCardMex selectPlaceCard() {
        System.out.println();
        printGameArea.update(modelMex.getOtherPlayersInfo(), modelMex.getDrawingCard(), modelMex.getPlayerdata().getHand());

        return new PlaceCardMex(
                readInt("Select the card you want to place",
                        1, modelMex.getPlayerdata().getHand().size()) - 1,
                readInt("Select the face of the card", 1, 2) == 1,
                readInt("Select the x coordinate", 1, modelMex.getPlayerdata().getCardsArea().length()),
                readInt("Select the y coordinate", 1, modelMex.getPlayerdata().getCardsArea().length()));
    }

    @Override
    public DrawingPosition selectDrawingPosition() {
        return DrawingPosition.values()[readInt("Select the card you want to draw",
                1, DrawingPosition.values().length) - 1];
    }

    @Override
    public void waitPlayers() {
        System.out.println("Waiting for other players to join the game");
    }

    @Override
    public void allPlayerReady() {
        System.out.println("All players are ready, the game is starting");
    }

    @Override
    public void addModel(ModelMex modelmex) {
        this.modelMex = modelmex;
        this.printGameArea.update(modelmex.getOtherPlayersInfo(), modelmex.getDrawingCard(), modelmex.getPlayerdata().getHand());
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
        printGameArea.update(modelMex.getOtherPlayersInfo(), modelMex.getDrawingCard(), modelMex.getPlayerdata().getHand());
        printGameArea.printAllBoard(true, 0);
    }

    private void printFaces(PhysicalCard card) {
        String[][] cardTUI;
        System.out.println("1) Front");
        cardTUI = card.getFront().toStringTUI();
        for (int i = cardTUI[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < cardTUI.length; j++) {
                System.out.print(cardTUI[j][i]);
            }
            System.out.println();
        }
        System.out.println("2) Back");
        cardTUI = card.getBack().toStringTUI();
        for (int i = 6; i >= 0; i--) {
            for (int j = 0; j < 3; j++) {
                System.out.print(cardTUI[j][i]);
            }
            System.out.println();
        }
    }

    private void printListObjectiveCard() {
        List<ObjectiveCard> objectiveCards = modelMex.getPlayerdata().getSecretObjectivesList();
        for (int i = 0; i < objectiveCards.size(); i++) {
            System.out.println();
            System.out.println((i + 1) + ") " + objectiveCards.get(i).toStringTUI());
        }
    }

    private static String readString(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);

        String read;
        try {
            read = scanner.nextLine();
        } catch (InputMismatchException e) {
            read = null;
            scanner.nextLine();
        }
        while (read == null) {
            System.out.println("Invalid input. Please retry");
            try {
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
            read = scanner.nextInt();
        } catch (InputMismatchException e) {
            read = min - 1;
            scanner.nextLine();
        }
        while (read < min || read >= min + range) {
            System.out.println("Invalid input. Please enter a number between " + min + " and " + (min + range - 1));
            try {
                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
        return read;
    }
}
