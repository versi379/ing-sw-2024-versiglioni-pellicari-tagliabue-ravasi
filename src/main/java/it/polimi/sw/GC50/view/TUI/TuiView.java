package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import it.polimi.sw.GC50.net.util.Client;
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

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void showConnected() {
        System.out.println("Connected to server");
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String selectName() {
        System.out.println(blueTxt + "Insert your player's name:" + baseTxt);

        return readString();
    }

    @Override
    public int selectJoinOrCreate() {
        System.out.println(blueTxt + "Do you want to create a new game or join an existing one?" + baseTxt);
        System.out.println("1) create a new game");
        System.out.println("2) join a game");
        System.out.println("3) quit");

        return readInt(1, 3);
    }

    @Override
    public void showFreeGames(Map<String, List<String>> freeGames) {
        if (freeGames.isEmpty()) {
            System.out.println(redTxt + "No free games" + baseTxt);
        } else {
            System.out.println(yellowTxt + "Free games:" + baseTxt);
            for (String game : freeGames.keySet()) {
                System.out.print("Game \"" + game + "\" -> current players:");
                for (String nickname : freeGames.get(game)) {
                    System.out.print(" " + nickname);
                }
                System.out.println();
            }
        }
    }

    @Override
    public String selectGameName() {
        System.out.println(blueTxt + "Insert the game's name:" + baseTxt);
        return readString();
    }

    @Override
    public int selectNumberOfPlayers() {
        System.out.println(blueTxt + "Insert the number of players:" + baseTxt);
        return readInt(2, 3);
    }

    @Override
    public int selectEndScore() {
        System.out.println(blueTxt + "Insert the score needed for triggering the game's ending:" + baseTxt);
        return readInt(0, 51);
    }

    private static String readString() {
        Scanner scanner = new Scanner(System.in);

        String read;
        try {
            read = scanner.nextLine();
        } catch (InputMismatchException e) {
            read = null;
            scanner.nextLine();
        }
        while (read == null || read.trim().isEmpty()) {
            System.out.println(redTxt + "Invalid input. Please retry" + baseTxt);
            try {
                read = scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
        return read;
    }

    private static int readInt(int min, int range) {
        Scanner scanner = new Scanner(System.in);

        int read;
        try {
            read = scanner.nextInt();
        } catch (InputMismatchException e) {
            read = min - 1;
            scanner.nextLine();
        }
        while (read < min || read >= min + range) {
            System.out.println(redTxt + "Invalid input. Please enter a number between " + min + " and " + (min + range - 1) + baseTxt);
            try {
                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
        return read;
    }

    // GAME ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private GameView getGameView() {
        return client.getGameView();
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
        showHelp();

        System.out.println();
        System.out.println(yellowTxt + "All players joined, beginning game setup!" + baseTxt);
        showCommonObjectives();
        showSecretObjectiveSelection();
        showStarterCardSelection();

        System.out.println();
        System.out.println(blueTxt + "Select the secret objective card and starter card face you want to play with:" + baseTxt);
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

    private void showSecretObjectiveSelection() {
        System.out.println(yellowTxt + "Secret objective cards selection:" + baseTxt);
        List<ObjectiveCard> objectiveCards = getGameView().getSecreteObjectivesList();
        for (int i = 0; i < objectiveCards.size(); i++) {
            System.out.println((i + 1) + ") " + objectiveCards.get(i).toStringTUI());
            System.out.println();
        }
    }

    private void showStarterCardSelection() {
        System.out.println(yellowTxt + "Starter card:" + baseTxt);
        TuiModelPrinter.printStarterCard(getGameView().getStarterCard());
    }

    @Override
    public void showPlayerReady(String nickname) {
        System.out.println("Player " + nickname + " ready!");
    }

    @Override
    public void showStart() {
        System.out.println();
        System.out.println(yellowTxt + "Game started" + baseTxt);
        showCurrentPlayer();
    }

    @Override
    public void showCurrentPlayer() {
        System.out.println();
        System.out.println(yellowTxt + "Player \"" + getGameView().getCurrentPlayer() + "\" turn:" + baseTxt);
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
        TuiModelPrinter.printPlayerArea(nickname, getGameView().getPlayerArea(nickname));
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
        TuiModelPrinter.printDecks(getGameView().getDecks());
    }

    @Override
    public void showScores() {
        Map<String, Integer> scores = new HashMap<>();
        for (String nickname : getGameView().getPlayerList()) {
            scores.put(nickname, getGameView().getPlayerArea(nickname).getTotalScore());
        }
        TuiModelPrinter.printScores(scores);
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
    public void showChatMessage(Chat chat) {
    }

    @Override
    public void showEndSession() {
        System.out.println();
        System.out.println(yellowTxt + "Session ended" + baseTxt);
    }

    @Override
    public void showHelp() {
        System.out.println();
        System.out.println(yellowTxt + "Commands:" + baseTxt);
        System.out.println("Select secret objective card -> \"-choose_objective\", \"-co\" [index]");
        System.out.println("Select starter card face -> \"-choose_starter_face\", \"-cs\" [index]");
        System.out.println("Place card -> \"-place_card\", \"-p\" [card index] [face index] [x] [y]");
        System.out.println("Draw card -> \"-draw_card\", \"-d\" [index]");
        System.out.println("Send message in chat -> \"-chat\", \"-c\" [message]");
        System.out.println("Help -> \"-help\", \"-h\"");
    }

    @Override
    public void showError(String content) {
        System.out.println(redTxt + "Error: " + content + baseTxt);
    }

    @Override
    public void showMessage(String message) {
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

            case "-chat", "-c" -> {
                List<String> args = getWords(read);
                args.removeFirst();
                try {
                    return new Pair<>(Command.CHAT, args.stream()
                            .map(Integer::valueOf)
                            .toList());
                } catch (NumberFormatException ignored) {
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
