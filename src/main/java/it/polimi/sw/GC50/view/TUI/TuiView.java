package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import it.polimi.sw.GC50.net.util.Client;
import it.polimi.sw.GC50.net.util.PlaceCardRequest;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.View;
import javafx.util.Pair;

import java.util.*;

public class TuiView implements View {
    private Client client;
    public static String baseTxt = "\u001B[0m";
    public static String redTxt = "\u001B[31m";
    public static String yellowTxt = "\u001B[33m";
    public static String blueTxt = "\u001B[34m";

    public TuiView() {
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    private GameView getGameView() {
        return client.getGameView();
    }

    @Override
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
    public int selectEndScore() {
        return readInt("Insert the score needed for triggering the game's ending:",
                0, 51);
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
        showSecretObjectiveSelection();
        showStarterCardSelection();
    }

    @Override
    public void showCommonObjectives() {
        System.out.println(yellowTxt + "Common objective cards:" + baseTxt);
        List<ObjectiveCard> commonObjectives = getGameView().getCommonObjectives();
        for (ObjectiveCard commonObjective : commonObjectives) {
            System.out.println((commonObjective.toStringTUI()));
            System.out.println();
        }
    }

    public void showSecretObjectiveSelection() {
        System.out.println(yellowTxt + "Secret objective cards:" + baseTxt);
        List<ObjectiveCard> objectiveCards = getGameView().getSecreteObjectivesList();
        for (int i = 0; i < objectiveCards.size(); i++) {
            System.out.println((i + 1) + ") " + objectiveCards.get(i).toStringTUI());
            System.out.println();
        }
    }

    public void showStarterCardSelection() {
        System.out.println(yellowTxt + "Starter card:" + baseTxt);
        String[][] cardTUI;

        System.out.println("1) Front");
        cardTUI = getGameView().getStarterCard().getFront().toStringTUI();
        for (int i = cardTUI[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < cardTUI.length; j++) {
                System.out.print(cardTUI[j][i]);
            }
            System.out.println();
        }

        System.out.println("2) Back");
        cardTUI = getGameView().getStarterCard().getBack().toStringTUI();
        for (int i = cardTUI[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < cardTUI.length; j++) {
                System.out.print(cardTUI[j][i]);
            }
            System.out.println();
        }
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
    public void showCurrentPlayer() {
        System.out.println(yellowTxt + "Player " + getGameView().getCurrentPlayer() + " turn");
    }

    /*
    @Override
    public PlaceCardRequest selectPlaceCard() {
        System.out.println();
        System.out.println(yellowTxt + "Placing phase" + baseTxt);
        showPlayerArea(getGameView().getNickname());
        ModelPrinter.printHand(getGameView().getHand());
        return new PlaceCardRequest(
                readInt("Select the card you want to place:",
                        1, getGameView().getHand().size()) - 1,
                readInt("Select the card's face:",
                        1, 2) - 1,
                readInt("Insert the value of the x coordinate:",
                        1, getGameView().getPlayerArea(getGameView().getNickname())
                                .getCardsMatrix().length()) - 1,
                readInt("Insert the value of the y coordinate:",
                        1, getGameView().getPlayerArea(getGameView().getNickname())
                                .getCardsMatrix().length()) - 1);
    }

     */

    @Override
    public void showPlayerArea(String nickname) {
        ModelPrinter.printPlayerArea(nickname, getGameView().getPlayerArea(nickname));
    }

    /*
    @Override
    public int selectDrawingPosition() {
        System.out.println();
        System.out.println(yellowTxt + "Drawing phase" + baseTxt);
        showDecks();
        return readInt("Select the card to draw:",
                1, DrawingPosition.values().length) - 1;
    }

     */

    @Override
    public void showDecks() {
        ModelPrinter.printDecks(getGameView().getDecks());
    }

    @Override
    public void showScores() {
        Map<String, Integer> scores = new HashMap<>();
        for (String nickname : getGameView().getPlayerList()) {
            scores.put(nickname, getGameView().getPlayerArea(nickname).getTotalScore());
        }
        ModelPrinter.printScores(scores);
    }

    @Override
    public void showEnd() {
        System.out.println();
        System.out.println(yellowTxt + "Game ended!" + baseTxt);

        if (getGameView().getWinnerList().size() == 1) {
            System.out.println("winner -> " + getGameView().getWinnerList().getFirst());
        } else {
            System.out.print("winners ->");
            for (String nickname : getGameView().getWinnerList()) {
                System.out.print(" " + nickname);
            }
        }
        showScores();
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

    @Override
    public void listen() {
        Pair<Command, List<Integer>> command = readCommand();
        client.addCommand(command.getKey(), command.getValue());
    }


    public Pair<Command, List<Integer>> readCommand() {
        Scanner scanner = new Scanner(System.in);
        String read = scanner.nextLine();
        switch (getFirstWord(read)) {
            case "-choose_objective", "-co" -> {
                List<String> args = getWords(read);
                args.removeFirst();
                if (args.size() == 1) {
                    try {
                        return new Pair<>(Command.CHOOSE_OBJECTIVE, args.stream()
                                .map(Integer::valueOf)
                                .map(x -> x - 1)
                                .toList());
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            case "-choose_starter_face", "-cs" -> {
                List<String> args = getWords(read);
                args.removeFirst();
                if (args.size() == 1) {
                    try {
                        return new Pair<>(Command.CHOOSE_STARTER_FACE, args.stream()
                                .map(Integer::valueOf)
                                .map(x -> x - 1)
                                .toList());
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            case "-place_card", "-p" -> {
                List<String> args = getWords(read);
                args.removeFirst();
                if (args.size() == 4) {
                    try {
                        return new Pair<>(Command.PLACE_CARD, args.stream()
                                .map(Integer::valueOf)
                                .map(x -> x - 1)
                                .toList());
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            case "-draw_card", "-d" -> {
                List<String> args = getWords(read);
                args.removeFirst();
                if (args.size() == 1) {
                    try {
                        return new Pair<>(Command.DRAW_CARD, args.stream()
                                .map(Integer::valueOf)
                                .map(x -> x - 1)
                                .toList());
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            case "-help", "-h" -> {
                List<String> args = getWords(read);
                args.removeFirst();
                if (args.isEmpty()) {
                    return new Pair<>(Command.HELP, null);
                }
            }

            default -> {
            }
        }

        return new Pair<>(Command.NOT_A_COMMAND, null);
    }

    private static String getFirstWord(String read) {
        int index = read.indexOf(' ');
        if (index > -1) {
            return read.substring(0, index).trim();
        } else {
            return read;
        }
    }

    private static List<String> getWords(String read) {
        List<String> words = new ArrayList<>();

        int index = read.indexOf(' ');
        words.add(getFirstWord(read));
        if (index > -1) {
            words.addAll(getWords(read.substring(index).trim()));
        }
        return words;
    }
}
