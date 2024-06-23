package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;
import it.polimi.sw.GC50.net.client.Client;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.GUI.controllers.*;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.PlayerDataView;
import it.polimi.sw.GC50.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiView extends Application implements View {
    private Client client;
    private Stage primaryStage;

    private String submittedIp;
    private String submittedPlayerNickname;
    private int submittedGameChoice;
    private String submittedGameName;
    private int submittedNumPlayers;
    private int submittedEndPoints;
    private String submittedJoinGameName;

    // GUI Controllers
    private NetController netController;
    private UserController userController;
    private CreateGameController createGameController;
    private JoinGameController joinGameController;
    private MenuController menuController;
    private SetupGameController setupGameController;
    private PlayGameController playGameController;

    public String setupCommonObjectives;
    public String setupSecretObjectives;
    public String starterCardFrontCode;
    public String starterCardBackCode;

    public List<PhysicalCard> playerHand = new ArrayList<>();
    public PlayerDataView playerArea;
    public Boolean playerAreaUpdated = false;
    public Boolean playerHandUpdated = false;
    public Boolean serverError = false;
    public String scoresText;

    private final Object lock = new Object(); // Object for synchronization
    private volatile boolean waitingForButton = false; // Flag to indicate if client thread is waiting for button press

    private String read; // commands sent via GUI components

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        FXMLLoader netLoader = new FXMLLoader(getClass().getResource(ScenePath.NET.getPath()));
        Parent netRoot = null;

        try {
            netRoot = netLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        netController = netLoader.getController();

        Scene scene = new Scene(netRoot);
        scene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Codex Naturalis");
        stage.show();
    }

    public GuiView() {
        // Ensure JavaFX is initialized
        Platform.startup(() -> {
            // Launch the JavaFX Application
            Stage stage = new Stage();
            start(stage);
        });
    }

    /**
     * Given a client sets a new client
     * @param client client given
     */
    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * show connected
     */
    @Override
    public void showConnected() {
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return IP submitted
     */
    @Override
    public String selectServerIp() {
        waitForButtonPress();
        return submittedIp;
    }

    /**
     * @return type of connection selected
     */
    @Override
    public int selectConnectionType() {
        while (getNetController() == null || !getNetController().isNetSet()) {
        }
        return getNetController().getNetSelected();
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return selected name
     */
    @Override
    public String selectName() {

        Platform.runLater(() -> {
            FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.USER.getPath()));
            Parent userRoot = null;

            try {
                userRoot = userLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            userController = userLoader.getController();

            Scene gameScene = new Scene(userRoot);
            gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
            getPrimaryStage().setScene(gameScene);
        });

        waitForButtonPress();
        return submittedPlayerNickname;
    }

    /**
     *
     * @return join or create a new game
     */
    @Override
    public int selectJoinOrCreate() {

        Platform.runLater(() -> {
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource(ScenePath.MENU.getPath()));
            Parent menuRoot = null;

            try {
                menuRoot = menuLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            menuController = menuLoader.getController();

            Scene gameScene = new Scene(menuRoot);
            gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
            getPrimaryStage().setScene(gameScene);
        });

        waitForButtonPress();
        return submittedGameChoice;
    }

    // map of game names with associated list of players

    /**
     * shows free games
     * @param freeGames a map of free games
     */
    @Override
    public void showFreeGames(Map<String, List<String>> freeGames) {

        Platform.runLater(() -> {
            FXMLLoader joinGameLoader = new FXMLLoader(getClass().getResource(ScenePath.JOINGAME.getPath()));
            Parent joinGameRoot = null;

            try {
                joinGameRoot = joinGameLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            joinGameController = joinGameLoader.getController();

            Scene gameScene = new Scene(joinGameRoot);
            gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
            getPrimaryStage().setScene(gameScene);
        });

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

    /**
     * @return game names
     */
    @Override
    public String selectGameName() {

        Platform.runLater(() -> {
            FXMLLoader createGameLoader = new FXMLLoader(getClass().getResource(ScenePath.CREATEGAME.getPath()));
            Parent createGameRoot = null;

            try {
                createGameRoot = createGameLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            createGameController = createGameLoader.getController();

            Scene gameScene = new Scene(createGameRoot);
            gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
            getPrimaryStage().setScene(gameScene);
        });

        waitForButtonPress();
        return submittedGameName;
    }

    /**
     * @return selected number of players
     */
    @Override
    public int selectNumberOfPlayers() {
        return submittedNumPlayers;
    }

    /**
     * @return selected end score
     */
    @Override
    public int selectEndScore() {
        return submittedEndPoints;
    }

    /**
     * @return selected join game name
     */
    @Override
    public String selectJoinGameName() {
        waitForButtonPress();
        return submittedJoinGameName;
    }

    // WAITING /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return type of game wiew
     */
    private GameView getGameView() {
        return client.getGameView();
    }
    /**
     * shows player that have joined a game
     */
    @Override
    public void showPlayerJoined(String nickname) {
    }

    /**
     * Given a nickname show the players who left the game
     * @param nickname  player's nickname
     */
    @Override
    public void showPlayerLeft(String nickname) {
    }

    /**
     * shows waiting players
     */
    @Override
    public void showWaitPlayers() {
        if (createGameController != null) {
            createGameController.showWaitingBuffer();
        }
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that shows setup
     */
    @Override
    public void showSetup() {

        Platform.runLater(() -> {
            FXMLLoader setupGameLoader = new FXMLLoader(getClass().getResource(ScenePath.SETUPGAME.getPath()));
            Parent setupGameRoot = null;

            try {
                setupGameRoot = setupGameLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setupGameController = setupGameLoader.getController();

            Scene gameScene = new Scene(setupGameRoot);
            gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
            getPrimaryStage().setScene(gameScene);
        });

        showObjectives();
        showSecretObjectiveSelection();
        showStarterCardSelection();
    }

    /**
     * method that shows objectives
     */
    @Override
    public void showObjectives() {
        List<ObjectiveCard> commonObjectives = getGameView().getCommonObjectives();
        StringBuilder commonObjectiveStringBuilder = new StringBuilder("Common Objective Cards");
        for (ObjectiveCard commonObjective : commonObjectives) {
            commonObjectiveStringBuilder.append(commonObjective.toStringTUI()).append("\n");
        }
        setupCommonObjectives = commonObjectiveStringBuilder.toString();
    }
    /**
     * method that shows secret objectives selected
     */
    private void showSecretObjectiveSelection() {
        List<ObjectiveCard> objectiveCards = getGameView().getSecreteObjectivesSelection();
        StringBuilder secretObjectiveStringBuilder = new StringBuilder("Secret Objective Cards");
        for (int i = 0; i < objectiveCards.size(); i++) {
            secretObjectiveStringBuilder.append((i + 1) + ") " + objectiveCards.get(i).toStringTUI());
        }
        setupSecretObjectives = secretObjectiveStringBuilder.toString();
    }
    /**
     * method that shows starter card selection
     */
    private void showStarterCardSelection() {
        PhysicalCard starterCard = getGameView().getStarterCard();
        starterCardFrontCode = starterCard.getFront().getCode();
        starterCardBackCode = starterCard.getBack().getCode();
    }
    /**
     * method that players that are ready
     * @param nickname player's nickname
     */
    @Override
    public void showPlayerReady(String nickname) {
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * show start of the game
     */
    @Override
    public void showStart() {

        Platform.runLater(() -> {
            FXMLLoader playGameLoader = new FXMLLoader(getClass().getResource(ScenePath.PLAYGAME.getPath()));
            Parent playGameRoot = null;

            try {
                playGameRoot = playGameLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            playGameController = playGameLoader.getController();

            Scene gameScene = new Scene(playGameRoot);
            gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
            getPrimaryStage().setScene(gameScene);
        });

        showCardsArea(getGameView().getCurrentPlayer());
        showHand();
    }

    /**
     * shows the current player
     */
    @Override
    public void showCurrentPlayer() {
        playGameController.updateCurrentPlayer();
    }

    /**
     * method that is called only when a player has to place a card (in his/her turn)
     */
    // questo metodo viene chiamato per il solo giocatore che deve piazzare una carta (cioè è il suo turno)
    @Override
    public void showPlacingPhase() {
        System.err.println("> PLACING PHASE");
    }

    /**
     * method that shows drawing phase
     */
    @Override
    public void showDrawingPhase() {
        System.err.println("> DRAWING PHASE");
    }
    /**
     * method that shows card area
     */
    @Override
    public void showCardsArea(String nickname) {
        playerArea = getGameView().getPlayerArea(nickname);
        playerAreaUpdated = true;
        System.err.println("> player area updated del giocatore: " + nickname);

        //playGameController.updateBoard();
    }
    /**
     * method that shows hand
     */
    @Override
    public void showHand() {
        playerHand = getGameView().getHand();
        playerHandUpdated = true;

        //playGameController.updateHand();
    }
    /**
     * method that shows decks
     */
    @Override
    public void showDecks() {
        playGameController.updateDecks();
    }
    /**
     * method that shows scores
     */
    @Override
    public void showScores() {
        scoresText = "";
        Map<String, Integer> scores = new HashMap<>();
        for (String nickname : getGameView().getPlayerList()) {
            scores.put(nickname, getGameView().getPlayerArea(nickname).getTotalScore());
        }
        printScores(scores);

        playGameController.updateScores();
    }
    /**
     * method that prints scores
     */
    private void printScores(Map<String, Integer> scores) {
        for (String nickname : scores.keySet()) {
            scoresText = scoresText += (nickname + ": " + scores.get(nickname) + "\n");
        }
    }

    // END /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that shows end of the game
     */
    @Override
    public void showEnd() {
    }

    // CHAT ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that shows a chat message
     * @param sender        of the message
     * @param content       of the message
     * @param time          when message is sent
     */
    @Override
    public void showChatMessage(String sender, String content, String time) {
    }

    // OTHER ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that shows help
     */
    @Override
    public void showHelp() {
    }
    /**
     * method that shows errors
     */
    @Override
    public void showError(String content) {
        System.err.println("> Error: " + content);
    }
    /**
     * method that shows that session is ended
     */
    @Override
    public void showEndSession() {
    }

    // COMMANDS ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * waits for a command
     */
    @Override
    public void listen() {
        waitForButtonPress();
        Pair<Command, String[]> command = readCommand();
        System.out.println("ADD COMMAND: " + command.getKey());
        client.addCommand(command.getKey(), command.getValue());
    }

    /**
     * @return a pair command/string
     */
    // commands must be read via GUI rather than terminal
    public Pair<Command, String[]> readCommand() {

        switch (getFirstWord(read)) {
            case "-choose_objective", "-co" -> {
                System.err.println("> Choose objective triggered");
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
                System.err.println("> Choose starter face triggered");
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
                System.err.println("> Place card triggered");
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
                System.err.println("> Draw card triggered");
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
                System.err.println("> Chat triggered");
                String arg = removeFirstWord(read);
                return new Pair<>(Command.CHAT, new String[]{arg});
            }

            case "-chat_private", "-cp" -> {
                System.err.println("> Chat private triggered");
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

    /**
     * @return net controller
     */
    public NetController getNetController() {
        return netController;
    }
    /**
     * @return user controller
     */
    public UserController getUserController() {
        return userController;
    }
    /**
     * @return setup game controller
     */
    public SetupGameController getGameController() {
        return setupGameController;
    }

    /**
     * @return menu controller
     */
    public MenuController getMenuController() {
        return menuController;
    }

    /**
     * @return game controller
     */
    public CreateGameController getCreateGameController() {
        return createGameController;
    }
    /**
     * @return join game controller
     */
    public JoinGameController getJoinGameController() {
        return joinGameController;
    }

    /**
     * @return primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * wait for button press
     */
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

    /**
     * method that resumes execution when the button is pressed
     */
    public void resumeExecution() {
        synchronized (lock) {
            waitingForButton = false; // Reset the flag
            lock.notifyAll(); // Notify to resume execution
        }
    }

    /**
     * submits a player nickname
     * @param submittedPlayerNickname   type of choice
     */
    public void setSubmittedPlayerNickname(String submittedPlayerNickname) {
        this.submittedPlayerNickname = submittedPlayerNickname;
    }

    /**
     * submits a game choice
     * @param submittedGameChoice type of choice
     */
    public void setSubmittedGameChoice(int submittedGameChoice) {
        this.submittedGameChoice = submittedGameChoice;
    }

    /**
     * submits a game name
     * @param submittedGameName type of game name
     */
    public void setSubmittedGameName(String submittedGameName) {
        this.submittedGameName = submittedGameName;
    }

    /**
     * submits a number of players
     * @param submittedNumPlayers   number of players
     */
    public void setSubmittedNumPlayers(int submittedNumPlayers) {
        this.submittedNumPlayers = submittedNumPlayers;
    }

    /**
     * submits a end points
     * @param submittedEndPoints end points submitted
     */
    public void setSubmittedEndPoints(int submittedEndPoints) {
        this.submittedEndPoints = submittedEndPoints;
    }

    /**
     * submits a join game name
     * @param submittedJoinGameName join game name submitted
     */
    public void setSubmittedJoinGameName(String submittedJoinGameName) {
        this.submittedJoinGameName = submittedJoinGameName;
    }

    /**
     * submits an IP address
     * @param submittedIp IP address submitted
     */
    public void setSubmittedIp(String submittedIp) {
        this.submittedIp = submittedIp;
    }

    /**
     * Sets a new read
     * @param read  type of read
     */
    public void setRead(String read) {
        this.read = read;
        resumeExecution();
    }

    public String getCurrentPlayer() {
        return getGameView().getCurrentPlayer();
    }
}
