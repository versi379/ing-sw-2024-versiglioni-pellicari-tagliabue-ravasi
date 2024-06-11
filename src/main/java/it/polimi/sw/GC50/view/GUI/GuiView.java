package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import it.polimi.sw.GC50.net.util.Client;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.GUI.controllers.*;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.PlayerDataView;
import it.polimi.sw.GC50.view.TUI.TuiModelPrinter;
import it.polimi.sw.GC50.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.polimi.sw.GC50.view.TUI.TuiView.yellowTxt;

public class GuiView extends Application implements View {

    private Stage primaryStage;

    // RIF client
    private Client client;

    private String submittedPlayerNickname;
    private int submittedGameChoice;
    private String submittedGameName;
    private int submittedNumPlayers;
    private int submittedEndPoints;
    private String submittedJoinGameName;

    // GUI Controllers
    private NetController netController;
    private UserController userController;
    private MenuController menuController;
    private SetupGameController setupGameController;
    private PlayGameController playGameController;
    private CreateGameController createGameController;
    private JoinGameController joinGameController;

    public String setupCommonObjectives;
    public String setupSecretObjectives;
    public String starterCardCode;

    public List<PhysicalCard> playerHand = new ArrayList<>();
    public PlayerDataView playerArea;

    public Label headerLabel;
    public Label headerLabel2;

    private Object lock = new Object(); // Object for synchronization
    private volatile boolean waitingForButton = false; // Flag to indicate if client thread is waiting for button press

    public String read = ""; // commands sent via GUI components

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;

        FXMLLoader netLoader = new FXMLLoader(getClass().getResource(ScenePath.NET.getPath()));
        Parent netRoot = netLoader.load();
        netController = netLoader.getController();

        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.USER.getPath()));
        Parent userRoot = userLoader.load();
        userController = userLoader.getController();

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource(ScenePath.MENU.getPath()));
        Parent menuRoot = menuLoader.load();
        menuController = menuLoader.getController();

//         FXMLLoader setupGameLoader = new FXMLLoader(getClass().getResource(ScenePath.SETUPGAME.getPath()));
//         Parent setupGameRoot = setupGameLoader.load();
//         setupGameController = setupGameLoader.getController();

        FXMLLoader createGameLoader = new FXMLLoader(getClass().getResource(ScenePath.CREATEGAME.getPath()));
        Parent createGameRoot = createGameLoader.load();
        createGameController = createGameLoader.getController();

        FXMLLoader joinGameLoader = new FXMLLoader(getClass().getResource(ScenePath.JOINGAME.getPath()));
        Parent joinGameRoot = joinGameLoader.load();
        joinGameController = joinGameLoader.getController();

//        FXMLLoader playGameLoader = new FXMLLoader(getClass().getResource(ScenePath.PLAYGAME.getPath()));
//        Parent playGameRoot = playGameLoader.load();
//        playGameController = playGameLoader.getController();

        Scene scene = new Scene(netRoot);
        scene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Codex Naturalis");
        stage.show();
    }

    public GuiView() {

    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String selectName() {
        while(userController == null) {
            System.out.println("Attendo caricamento user login page.");
        }
        waitForButtonPress();
        return submittedPlayerNickname;
    }

    @Override
    public int selectJoinOrCreate() {
        while(menuController == null) {
            System.out.println("Attendo caricamento menu page.");
        }
        waitForButtonPress();
        return submittedGameChoice;
    }

    public String selectedJoinGame() {
        waitForButtonPress();
        return submittedJoinGameName;
    }

    // map of game names with associated list of players
    @Override
    public void showFreeGames(Map<String, List<String>> freeGames) {
        while(joinGameController == null) {
            System.out.println("Attendo caricamento join game page.");
            try {
                // Sleep briefly to avoid busy-waiting
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                return; // Exit the method if interrupted
            }
        }

        if (!freeGames.isEmpty()) {
            for (String game : freeGames.keySet()) {
                StringBuilder gameItem = new StringBuilder(game + "\nPLAYERS: ");
                for (String nickname : freeGames.get(game)) {
                    gameItem.append(nickname).append(", ");
                }
                if (gameItem.length() > 0) {
                    gameItem.setLength(gameItem.length() - 2);
                }
                menuController.gameItems2.add(gameItem.toString());
            }
        }

    }

    public void waitGameParams() {
        while(createGameController == null) {
            System.out.println("Attendo caricamento create game page.");
        }
        waitForButtonPress();
    }

    @Override
    public String selectGameName() {
        return submittedGameName;
    }

    @Override
    public int selectNumberOfPlayers() {
        return submittedNumPlayers;
    }

    @Override
    public int selectEndScore() {
        return submittedEndPoints;
    }

    @Override
    public void showWaitPlayers() {

    }

    @Override
    public void showConnected() {

    }

    private GameView getGameView() {
        return client.getGameView();
    }

    @Override
    public void showSetup() {

        showCommonObjectives();
        showSecretObjectiveSelection();
        showStarterCardSelection();

    }

    @Override
    public void showCommonObjectives() {
        List<ObjectiveCard> commonObjectives = getGameView().getCommonObjectives();
        StringBuilder commonObjectiveStringBuilder = new StringBuilder("Common Objective Cards");
        for (ObjectiveCard commonObjective : commonObjectives) {
            commonObjectiveStringBuilder.append(commonObjective.toStringTUI()).append("\n");
        }
        setupCommonObjectives = commonObjectiveStringBuilder.toString();
    }

    private void showSecretObjectiveSelection() {
        List<ObjectiveCard> objectiveCards = getGameView().getSecreteObjectivesList();
        StringBuilder secretObjectiveStringBuilder = new StringBuilder("Secret Objective Cards");
        for (int i = 0; i < objectiveCards.size(); i++) {
            secretObjectiveStringBuilder.append((i + 1) + ") " + objectiveCards.get(i).toStringTUI());
        }
        setupSecretObjectives = secretObjectiveStringBuilder.toString();
    }

    private void showStarterCardSelection() {
        PhysicalCard starterCard = getGameView().getStarterCard();
        System.out.println(starterCard.getFront().getCode());
        TuiModelPrinter.printStarterCard(starterCard);
        starterCardCode = starterCard.getFront().getCode();
    }

    @Override
    public void showStart() {
        if (getGameView().getNickname().equals(getGameView().getCurrentPlayer())) {
            headerLabel = new Label("Your turn:");
        } else {
            headerLabel = new Label("Player " + getGameView().getCurrentPlayer() + " turn:");
        }
        showCardsArea(getGameView().getNickname());
        showHand();
    }

    @Override
    public void showCurrentPlayer() {

    }

    @Override
    public void showPlacingPhase() {

    }

    @Override
    public void showCardsArea(String nickname) {
        if (getGameView().getNickname().equals(nickname)) {
            headerLabel2 = new Label("Your cards area:");
        } else {
            headerLabel2 = new Label("Player \"" + getGameView().getCurrentPlayer() + "\" cards area:");
        }
        playerArea = getGameView().getPlayerArea(nickname);
        System.out.println("playerArea aggiornata in showcardsarea");
        if (playGameController != null) {
            playGameController.playerAreaGrid = playGameController.printPlayerArea(playerArea);
            playGameController.pane.getChildren().add(playGameController.playerAreaGrid);
        }
        // TuiModelPrinter.printPlayerArea(nickname, getGameView().getPlayerArea(nickname));

    }

    public void updatePlayerArea() {
        playerArea = getGameView().getPlayerArea(getGameView().getNickname());
    }

    @Override
    public void showHand() {
        System.out.println("mostro mano giocatore");
        playerHand = getGameView().getHand();
        TuiModelPrinter.printHand(getGameView().getHand());
    }

    @Override
    public void showChatMessage(Chat chat) {

    }

    @Override
    public void showChatMessage(String sender, String content, String time) {
    }

    @Override
    public void listen() {
        Pair<Command, String[]> command = readCommand();
        client.addCommand(command.getKey(), command.getValue());
    }

    // commands must be read via GUI rather than terminal
    public Pair<Command, String[]> readCommand() {

        switch (getFirstWord(read)) {
            case "-choose_objective", "-co" -> {
                System.out.println("Choose objective triggered");
                String arg = removeFirstWord(read);
                read = "";
                try {
                    return new Pair<>(Command.CHOOSE_OBJECTIVE,
                            new String[]{String.valueOf(Integer.parseInt(arg) - 1)});
                } catch (NumberFormatException e) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-choose_starter_face", "-cs" -> {
                System.out.println("Choose starter face triggered");
                String arg = removeFirstWord(read);
                read = "";
                try {
                    return new Pair<>(Command.CHOOSE_STARTER_FACE,
                            new String[]{String.valueOf(Integer.parseInt(arg) - 1)});
                } catch (NumberFormatException e) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-place_card", "-p" -> {
                System.out.println("Place card triggered");
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
//                    playerArea = getGameView().getPlayerArea(getGameView().getNickname());
                    return new Pair<>(Command.PLACE_CARD, args);
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-draw_card", "-d" -> {
                System.out.println("Draw card triggered");
                String arg = removeFirstWord(read);
                read = "";
                try {
                    return new Pair<>(Command.DRAW_CARD,
                            new String[]{String.valueOf(Integer.parseInt(arg) - 1)});
                } catch (NumberFormatException e) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-chat", "-c" -> {
                System.out.println("Chat triggered");
                String arg = removeFirstWord(read);
                return new Pair<>(Command.CHAT, new String[]{arg});
            }

            case "-chat_private", "-cp" -> {
                System.out.println("Chat private triggered");
                String[] args = new String[2];
                read = removeFirstWord(read);
                if (read.isEmpty()) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
                args[0] = getFirstWord(read);
                args[1] = removeFirstWord(read);
                return new Pair<>(Command.CHAT_PRIVATE, args);
            }

            case "-help", "-h" -> {
                String arg = removeFirstWord(read);
                if (arg.isEmpty()) {
                    return new Pair<>(Command.HELP, null);
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument count"});
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

    @Override
    public void showDecks() {

    }

    @Override
    public void showScores() {
    }

    @Override
    public void showEnd() {

    }

    @Override
    public void showPlayerJoined(String nickname) {

    }

    @Override
    public void showPlayerLeft(String nickname) {

    }

    @Override
    public void showEndSession() {

    }

    @Override
    public void showHelp() {

    }

    @Override
    public void showError(String content) {

    }

    @Override
    public void showPlayerReady(String nickname) {

    }

    @Override
    public void showDrawingPhase() {

    }

    public void showUserView() throws Exception{
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.USER.getPath()));
        Parent userRoot = userLoader.load();
        Scene userScene = new Scene(userRoot);
        primaryStage.setScene(userScene);
    }

    public NetController getNetController() {
        return netController;
    }

    public UserController getUserController() {
        return userController;
    }

    public SetupGameController getGameController() {
        return setupGameController;
    }

    public MenuController getMenuController() {
        return menuController;
    }

    public CreateGameController getCreateGameController() {
        return createGameController;
    }

    public JoinGameController getJoinGameController() {
        return joinGameController;
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void waitForButtonPress() {
        synchronized (lock) {
            waitingForButton = true; // Set flag to indicate waiting for button press
            try {
                while (waitingForButton) {
                    lock.wait(); // Wait until notified
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resumeExecution() {
        synchronized (lock) {
            waitingForButton = false; // Reset the flag
            lock.notifyAll(); // Notify to resume execution
        }
    }

    public void setSubmittedPlayerNickname(String submittedPlayerNickname) {
        this.submittedPlayerNickname = submittedPlayerNickname;
    }

    public void setSubmittedGameChoice(int submittedGameChoice) {
        this.submittedGameChoice = submittedGameChoice;
    }

    public void setSubmittedGameName(String submittedGameName) {
        this.submittedGameName = submittedGameName;
    }

    public void setSubmittedNumPlayers(int submittedNumPlayers) {
        this.submittedNumPlayers = submittedNumPlayers;
    }

    public void setSubmittedEndPoints(int submittedEndPoints) {
        this.submittedEndPoints = submittedEndPoints;
    }

    public void setSubmittedJoinGameName(String submittedJoinGameName) {
        this.submittedJoinGameName = submittedJoinGameName;
    }

    public Client getClientRmi() {
        return client;
    }

}
