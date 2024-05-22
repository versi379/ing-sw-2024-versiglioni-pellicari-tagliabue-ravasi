package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.objective.*;
import it.polimi.sw.GC50.net.util.PlaceCardRequest;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.View;

import java.util.*;

public class TuiView implements View {
    private GameView gameView;
    public static String baseTxt = "\u001B[0m";
    public static String redTxt = "\u001B[31m";
    public static String yellowTxt = "\u001B[33m";
    public static String blueTxt = "\u001B[34m";

    public TuiView() {
    }

    public void showEndSession() {
        System.out.println();
        System.out.println(yellowTxt + "Session ended" + baseTxt);
    }

    @Override
    public String selectName() {
        return readString("Insert your player's name:");
    }

    @Override
    public int selectJoinOrCreate() {
        return readInt("Do you want to create a new game or join an existing one?" +
                "\n1) create a new game" +
                "\n2) join a game" +
                "\n3) quit",
                1, 3);
    }

    @Override
    public void showFreeGames(Map<String, List<String>> freeGames) {
        if (freeGames.isEmpty()) {
            System.out.println(redTxt + "No free games" + baseTxt);
        } else {
            System.out.println(yellowTxt + "Free games:" + baseTxt);
            for (String game : freeGames.keySet()) {
                System.out.print("Game " + game + " -> current players:");
                for (String nickname : freeGames.get(game)) {
                    System.out.print(" " + nickname);
                }
                System.out.println();
            }
        }
    }

    @Override
    public String selectGameName() {
        return readString("Insert the game's name:");
    }

    @Override
    public int selectNumberOfPlayers() {
        return readInt("Insert the number of players:",
                2, 3);
    }

    @Override
    public void showPlayerJoined(String nickname) {
        System.out.println("Player " + nickname + " joined the game");
    }

    @Override
    public void showPlayerLeft(String nickname) {
        System.out.println("Player " + nickname + " left the game");
    }

    @Override
    public void showWaitPlayers() {
        System.out.println();
        System.out.println(yellowTxt + "Waiting for other players to join the game..." + baseTxt);
    }

    @Override
    public void showSetup() {
        System.out.println();
        System.out.println(yellowTxt + "All players joined, beginning game setup" + baseTxt);
        showCommonObjectives();
    }

    @Override
    public void showCommonObjectives() {
        System.out.println(yellowTxt + "Common objective cards:" + baseTxt);
        List<ObjectiveCard> commonObjectives = gameView.getCommonObjectives();
        for (int i = 0; i < commonObjectives.size(); i++) {
            System.out.println((commonObjectives.get(i).toStringTUI()));
            System.out.println();
        }
    }

    @Override
    public int selectObjectiveCard() {
        System.out.println(yellowTxt + "Secret objective cards:" + baseTxt);
        printSecretObjectiveChoice();

        return readInt("Select the secret objective card you want to play with:",
                1, gameView.getSecreteObjectivesList().size()) - 1;
    }

    private void printSecretObjectiveChoice() {
        List<ObjectiveCard> objectiveCards = gameView.getSecreteObjectivesList();
        for (int i = 0; i < objectiveCards.size(); i++) {
            System.out.println((i + 1) + ") " + objectiveCards.get(i).toStringTUI());
            System.out.println();
        }
    }

    @Override
    public boolean selectStarterFace() {
        System.out.println(yellowTxt + "Starter card:" + baseTxt);
        printStarterCardChoice(gameView.getStarterCard());

        return readInt("Select the face of the starter card you want to begin with:",
                1, 2) == 1;
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
    public void showPlayerReady(String nickname) {
        System.out.println("Player " + nickname + " ready!");
    }

    @Override
    public void showStart() {
        System.out.println();
        System.out.println(yellowTxt + "Game started" + baseTxt);
    }

    @Override
    public PlaceCardRequest selectPlaceCard() {
        System.out.println();
        System.out.println(yellowTxt + "Placing phase" + baseTxt);
        showPlayerArea(gameView.getNickname());
        ModelPrinter.printHand(gameView.getHand());
        return new PlaceCardRequest(
                readInt("Select the card you want to place:",
                        1, gameView.getHand().size()) - 1,
                readInt("Select the card's face:",
                        1, 2) == 1,
                readInt("Insert the value of the x coordinate:",
                        1, gameView.getPlayerArea(gameView.getNickname())
                        .getCardsMatrix().length()) - 1,
                readInt("Insert the value of the y coordinate:",
                        1,gameView.getPlayerArea(gameView.getNickname())
                        .getCardsMatrix().length()) - 1);
    }

    @Override
    public void showPlayerArea(String nickname) {
        ModelPrinter.printPlayerArea(nickname, gameView.getPlayerArea(nickname));
    }

    @Override
    public DrawingPosition selectDrawingPosition() {
        System.out.println();
        System.out.println(yellowTxt + "Drawing phase" + baseTxt);
        showDecks();
        return DrawingPosition.values()[readInt("Select the card to draw:",
                1, DrawingPosition.values().length) - 1];
    }

    @Override
    public void showDecks() {
        ModelPrinter.printDecks(gameView.getDecks());
    }

    @Override
    public void showScores() {
        Map<String, Integer> scores = new HashMap<>();
        for (String nickname : gameView.getPlayerList()) {
            scores.put(nickname, gameView.getPlayerArea(nickname).getTotalScore());
        }
        ModelPrinter.printScores(scores);
    }

    @Override
    public void showEnd() {
        System.out.println();
        System.out.println(yellowTxt + "Game ended!" + baseTxt);

        if (gameView.getWinnerList().size() == 1) {
            System.out.println("winner -> " + gameView.getWinnerList().getFirst());
        } else {
            System.out.print("winners ->");
            for (String nickname : gameView.getWinnerList()) {
                System.out.print(" " + nickname);
            }
        }
        showScores();
    }

    @Override
    public void setModel(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void updateChat(Chat chat) {
    }

    @Override
    public void showError(String content) {
        System.out.println();
        System.out.println(redTxt + "Error: " + content + baseTxt);
    }

    private static String readString(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(blueTxt + message + baseTxt);

        String read;
        try {
            System.out.print("> ");
            read = scanner.nextLine();
        } catch (InputMismatchException e) {
            read = null;
            scanner.nextLine();
        }
        while (read == null) {
            System.out.println(redTxt + "Invalid input. Please retry" + baseTxt);
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
        System.out.println(blueTxt + message + baseTxt);

        int read;
        try {
            System.out.print("> ");
            read = scanner.nextInt();
        } catch (InputMismatchException e) {
            read = min - 1;
            scanner.nextLine();
        }
        while (read < min || read >= min + range) {
            System.out.println(redTxt + "Invalid input. Please enter a number between " + min + " and " + (min + range - 1) + baseTxt);
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
    public void showMessage(String message) {
        System.out.println();
        System.out.println(message);
    }
}
