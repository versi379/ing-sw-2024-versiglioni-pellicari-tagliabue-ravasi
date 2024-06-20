package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.cards.Resource;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;
import it.polimi.sw.GC50.net.client.Client;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.View;
import javafx.util.Pair;

import java.util.*;

public class TuiView implements View {
    private Client client;
    public static String baseTxt = "\u001B[0m";
    public static String redTxt = "\u001B[31m";
    public static String blueTxt = "\u001B[34m";
    public static String yellowTxt = "\u001B[33m";
    public static String goldTxt = "\u001B[93m";

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void showConnected() {
        System.out.println("Connected to server");
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String selectServerIp() {
        System.out.println(blueTxt + "Insert the server's Ip address:" + baseTxt);

        return readString();
    }

    @Override
    public int selectConnectionType() {
        System.out.println(blueTxt + "Select the desired connection technology:" + baseTxt);
        System.out.println("1) socket");
        System.out.println("2) RMI");

        return readInt(1, 2);
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
                    System.out.print(" \"" + nickname + "\"");
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
        if (getGameView().getNickname().equals(nickname)) {
            System.out.println("You joined the game");
        } else {
            System.out.println("Player \"" + nickname + "\" joined the game");
        }
    }

    @Override
    public void showPlayerLeft(String nickname) {
        System.out.println("Player \"" + nickname + "\" left the game");
    }

    @Override
    public void showWaitPlayers() {
        System.out.println();
        System.out.println(goldTxt + "Waiting for other players to join the game..." + baseTxt);
        showHelp();
    }

    @Override
    public void showSetup() {
        System.out.println();
        System.out.println(goldTxt + "All players joined, beginning game setup!" + baseTxt);

        showObjectives();
        showSecretObjectiveSelection();
        showStarterCardSelection();

        System.out.println(blueTxt + "Select the secret objective card and starter card face you want to play with:" + baseTxt);
    }

    @Override
    public void showObjectives() {
        if (!getGameView().getGameStatus().equals(GameStatus.WAITING)) {
            System.out.println(yellowTxt + "Common objective cards:" + baseTxt);
            List<ObjectiveCard> commonObjectives = getGameView().getCommonObjectives();
            for (ObjectiveCard commonObjective : commonObjectives) {
                System.out.println((commonObjective.toStringTUI()));
                System.out.println();
            }
            if (getGameView().getSecretObjective() != null) {
                System.out.println(yellowTxt + "Secret objective card:" + baseTxt);
                ObjectiveCard secretObjective = getGameView().getSecretObjective();
                System.out.println((secretObjective.toStringTUI()));
                System.out.println();
            }

        } else {
            System.out.println(redTxt + "Objectives are yet to be chosen!" + baseTxt);
        }
    }

    private void showSecretObjectiveSelection() {
        System.out.println(yellowTxt + "Secret objective cards selection:" + baseTxt);
        List<ObjectiveCard> objectiveCards = getGameView().getSecreteObjectivesSelection();
        for (int i = 0; i < objectiveCards.size(); i++) {
            System.out.println((i + 1) + ") " + objectiveCards.get(i).toStringTUI());
            System.out.println();
        }
    }

    private void showStarterCardSelection() {
        System.out.println(yellowTxt + "Starter card:" + baseTxt);
        TuiModelPrinter.printStarterCard(getGameView().getStarterCard());
        System.out.println();
    }

    @Override
    public void showPlayerReady(String nickname) {
        System.out.println("Player \"" + nickname + "\" ready!");
    }

    @Override
    public void showStart() {
        System.out.println();
        System.out.println(goldTxt + "Game started!" + baseTxt);
    }

    @Override
    public void showCurrentPlayer() {
        if (getGameView().getNickname().equals(getGameView().getCurrentPlayer())) {
            System.out.println(goldTxt + "Your turn:" + baseTxt);
        } else {
            System.out.println(goldTxt + "Player \"" + getGameView().getCurrentPlayer() + "\" turn:" + baseTxt);
        }
    }

    @Override
    public void showPlacingPhase() {
        System.out.println(goldTxt + "Placing phase" + baseTxt);
        showCardsArea(getGameView().getNickname());
        showHand();
        System.out.println(blueTxt + "Place a card:" + baseTxt);
    }

    @Override
    public void showDrawingPhase() {
        System.out.println(goldTxt + "Drawing phase" + baseTxt);
        showDecks();
        System.out.println(blueTxt + "Draw a card:" + baseTxt);
    }

    @Override
    public void showCardsArea(String nickname) {
        if (getGameView().getNickname().equals(nickname)) {
            System.out.println(yellowTxt + "Your cards area:" + baseTxt);
        } else {
            System.out.println(yellowTxt + "Player \"" + getGameView().getCurrentPlayer() + "\" cards area:" + baseTxt);
        }
        TuiModelPrinter.printPlayerArea(getGameView().getPlayerArea(nickname).getCardsMatrix());
        System.out.println();
    }

    @Override
    public void showHand() {
        System.out.println(yellowTxt + "Your hand:" + baseTxt);
        TuiModelPrinter.printHand(getGameView().getHand());
        System.out.println();
    }

    @Override
    public void showDecks() {
        System.out.println(yellowTxt + "Cards in the center of the table:" + baseTxt);
        TuiModelPrinter.printDecks(getGameView().getDecks());
        System.out.println();
    }

    @Override
    public void showScores() {
        System.out.println(yellowTxt + "Scores:" + baseTxt);

        if (getGameView().getGameStatus().equals(GameStatus.ENDED)) {
            Map<String, Pair<Integer, Integer>> scores = new HashMap<>();
            for (String nickname : getGameView().getPlayerList()) {
                scores.put(nickname, new Pair(getGameView().getPlayerArea(nickname).getTotalScore(),
                        getGameView().getPlayerArea(nickname).getObjectivesScore()));
            }
            TuiModelPrinter.printScoresEnd(scores);

        } else {
            Map<String, Integer> scores = new HashMap<>();
            for (String nickname : getGameView().getPlayerList()) {
                scores.put(nickname, getGameView().getPlayerArea(nickname).getTotalScore());
            }
            TuiModelPrinter.printScoresPlaying(scores);
        }
        System.out.println();
    }

    @Override
    public void showEnd() {
        System.out.println();
        System.out.println(goldTxt + "Game ended!" + baseTxt);

        if (getGameView().getWinnerList().size() == 1) {
            System.out.println("Winner -> \"" + getGameView().getWinnerList().getFirst() + "\"");
        } else {
            System.out.print("Winners ->");
            for (String nickname : getGameView().getWinnerList()) {
                System.out.print(" \"" + nickname + "\"");
            }
            System.out.println();
        }
        showScores();
    }

    @Override
    public void showEndSession() {
        System.out.println();
        System.out.println(goldTxt + "Session ended" + baseTxt);
    }

    @Override
    public void showHelp() {
        System.out.println(yellowTxt + "Commands:" + baseTxt);
        System.out.println("Select secret objective card -> \"-choose_objective\", \"-co\" [index]");
        System.out.println("Select starter card face -> \"-choose_starter_face\", \"-cs\" [index]");
        System.out.println("Show objectives -> \"-objectives\", \"-o\"");
        System.out.println("Place card -> \"-place_card\", \"-p\" [card index] [face index] [x] [y]");
        System.out.println("Draw card -> \"-draw_card\", \"-d\" [index]");
        System.out.println("Send message in chat -> \"-chat\", \"-c\" [message]");
        System.out.println("Send private message in chat -> \"-chat_private\", \"-cp\" [receiver] [message]");
        System.out.println("Leave game -> \"-leave\", \"-l\"");
        System.out.println("Help -> \"-help\", \"-h\"");
        System.out.println();

        System.out.println(yellowTxt + "Legend:" + baseTxt);
        System.out.print("Resource types:");
        for (Resource resource : Resource.values()) {
            System.out.print(" " + resource.toStringTUI());
        }
        System.out.println();
        System.out.println("Card bonus types: blank, resource, covered corners (C)");
        System.out.println();
    }

    @Override
    public void showError(String content) {
        System.out.println(redTxt + "Error: " + content + baseTxt);
    }

    @Override
    public void showChatMessage(String sender, String content, String time) {
        if (getGameView().getNickname().equals(sender)) {
            System.out.print("Message sent: ");
        } else {
            System.out.print("Message received from player \"" + sender + "\": ");
        }
        System.out.println(content);
        System.out.println("Sent at time " + time);
    }

    @Override
    public void listen() {
        Pair<Command, String[]> command = readCommand();
        client.addCommand(command.getKey(), command.getValue());
    }

    public Pair<Command, String[]> readCommand() {
        Scanner scanner = new Scanner(System.in);
        String read = scanner.nextLine();
        switch (getFirstWord(read)) {
            case "-choose_objective", "-co" -> {
                String arg = removeFirstWord(read);
                try {
                    return new Pair<>(Command.CHOOSE_OBJECTIVE,
                            new String[]{String.valueOf(Integer.parseInt(arg) - 1)});
                } catch (NumberFormatException e) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-choose_starter_face", "-cs" -> {
                String arg = removeFirstWord(read);
                try {
                    return new Pair<>(Command.CHOOSE_STARTER_FACE,
                            new String[]{String.valueOf(Integer.parseInt(arg) - 1)});
                } catch (NumberFormatException e) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-objectives", "-o" -> {
                String arg = removeFirstWord(read);
                if (arg.isEmpty()) {
                    return new Pair<>(Command.SHOW_OBJECTIVES, null);
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-place_card", "-p" -> {
                String[] args = new String[4];
                read = removeFirstWord(read);
                for (int i = 0; i < args.length; i++) {
                    try {
                        args[i] = String.valueOf(Integer.parseInt(getFirstWord(read)) - 1);
                        read = removeFirstWord(read);
                    } catch (NumberFormatException e) {
                        return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                    }
                }
                if (read.isEmpty()) {
                    return new Pair<>(Command.PLACE_CARD, args);
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-draw_card", "-d" -> {
                String arg = removeFirstWord(read);
                try {
                    return new Pair<>(Command.DRAW_CARD,
                            new String[]{String.valueOf(Integer.parseInt(arg) - 1)});
                } catch (NumberFormatException e) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-chat", "-c" -> {
                String arg = removeFirstWord(read);
                if (!arg.isEmpty()) {
                    return new Pair<>(Command.CHAT, new String[]{arg});
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-chat_private", "-cp" -> {
                String[] args = new String[2];
                read = removeFirstWord(read);
                if (read.isEmpty()) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
                args[0] = getFirstWord(read);
                args[1] = removeFirstWord(read);
                if (!args[1].isEmpty()) {
                    return new Pair<>(Command.CHAT_PRIVATE, args);
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-leave", "-l" -> {
                String arg = removeFirstWord(read);
                if (arg.isEmpty()) {
                    return new Pair<>(Command.LEAVE, null);
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-help", "-h" -> {
                String arg = removeFirstWord(read);
                if (arg.isEmpty()) {
                    return new Pair<>(Command.HELP, null);
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }
        }

        return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid command"});
    }

    /**
     * To retrieve the name of the command (e.g. -co 1 returns -co)
     */
    private static String getFirstWord(String read) {
        int index = read.indexOf(' ');
        if (index > -1) {
            return read.substring(0, index).trim();
        } else {
            return read;
        }
    }

    /**
     * To retrieve the argument of the command (e.g. -co 1 returns 1)
     */
    private static String removeFirstWord(String read) {
        int index = read.indexOf(' ');
        if (index > -1) {
            return read.substring(index).trim();
        } else {
            return "";
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
