package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.net.Client;
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
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

/**
 * class that manages GUI view
 */
public class GuiView extends Application implements View {
    private Client client;
    private Stage primaryStage;

    private List<String> freeGames;
    private String submittedIp;
    private String submittedPlayerNickname;
    private int submittedGameChoice;
    private String submittedGameName;
    private int submittedNumPlayers;
    private int submittedEndPoints;
    private String submittedJoinGameName;
    private final List<String> chatMessages = new ArrayList<>();

    // GUI Controllers
    private EnterIPController enterIPController;
    private NetController netController;
    private UserController userController;
    private CreateGameController createGameController;
    private JoinGameController joinGameController;
    private MenuController menuController;
    private WaitGameController waitGameController;
    private SetupGameController setupGameController;
    private PlayGameController playGameController;
    private EndGameController endGameController;
    private final Object lock = new Object(); // Object for synchronization
    private boolean waitingForButton = false; // Flag to indicate if client thread is waiting for button press
    private String read; // commands sent via GUI components

    /**
     * method that starts GUI
     * @param stage a given stage
     */
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        FXMLLoader enterIPLoader = new FXMLLoader(getClass().getResource(ScenePath.ENTERIP.getPath()));
        Parent enterIPRoot = null;

        try {
            enterIPRoot = enterIPLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        enterIPController = enterIPLoader.getController();

        Scene scene = new Scene(enterIPRoot);
        scene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Codex Naturalis");
        stage.show();
    }

    /**
     * constructs an instance of GUI view
     */
    public GuiView() {
        // Ensure JavaFX is initialized
        Platform.startup(() -> {
            // Launch the JavaFX Application
            Stage stage = new Stage();
            start(stage);
        });
    }

    /**
     * Given a specific client sets as client
     * @param client specific client
     */
    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * method that shows connected
     */
    @Override
    public void showConnected() {
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return a selected server IP
     */
    @Override
    public String selectServerIp() {

        waitForButtonPress();
        return submittedIp;
    }

    /**
     * @return selects a connection type
     */
    @Override
    public int selectConnectionType() {

        Platform.runLater(() -> {
            synchronized (this) {
                FXMLLoader netLoader = new FXMLLoader(getClass().getResource(ScenePath.NET.getPath()));
                Parent netRoot = null;

                try {
                    netRoot = netLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                netController = netLoader.getController();

                Scene gameScene = new Scene(netRoot);
                gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
                getPrimaryStage().setScene(gameScene);
                notifyAll();
            }
        });

        synchronized (this) {
            while (netController == null) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }

        waitForButtonPress();
        return getNetController().getNetSelected();
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return selects a player's nickname
     */
    @Override
    public String selectNickname() {

        Platform.runLater(() -> {
            synchronized (this) {
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
                notifyAll();
            }
        });

        synchronized (this) {
            while (userController == null) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }

        waitForButtonPress();
        return submittedPlayerNickname;
    }

    /**
     * @return choose between join or create view
     */
    @Override
    public int selectJoinOrCreate() {

        Platform.runLater(() -> {
            synchronized (this) {
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
                getPrimaryStage().setTitle("Codex Naturalis: Client " + getGameView().getNickname());
                notifyAll();
            }
        });

        synchronized (this) {
            while (menuController == null) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }

        waitForButtonPress();
        return submittedGameChoice;
    }

    /**
     * method that shows free games
     * @param freeGames a map of free games
     */
    // map of game names with associated list of players
    @Override
    public void showFreeGames(Map<String, List<String>> freeGames) {
        if (!freeGames.isEmpty()) {
            this.freeGames = new ArrayList<>();
            for (String game : freeGames.keySet()) {
                String freeGameText = "Game \"" + game + "\"\nPlayers:";
                for (String nickname : freeGames.get(game)) {
                    freeGameText += " \"" + nickname + "\"";
                }
                this.freeGames.add(freeGameText);
            }

            Platform.runLater(() -> {
                synchronized (this) {
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
                    notifyAll();
                }
            });

            synchronized (this) {
                while (joinGameController == null) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }

    /**
     * @return a string with the name of the game
     */
    @Override
    public String selectGameName() {

        Platform.runLater(() -> {
            synchronized (this) {
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
                notifyAll();
            }
        });

        synchronized (this) {
            while (createGameController == null) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }

        waitForButtonPress();
        return submittedGameName;
    }

    /**
     * @return number of the players in a game
     */
    @Override
    public int selectNumberOfPlayers() {
        return submittedNumPlayers;
    }

    /**
     * @return end score
     */
    @Override
    public int selectEndScore() {
        return submittedEndPoints;
    }

    /**
     * @return join name game
     */
    @Override
    public String selectJoinGameName() {
        waitForButtonPress();
        return submittedJoinGameName;
    }

    // WAITING /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Shows players that have joined a game
     * @param nickname player's nickname
     */
    @Override
    public void showPlayerJoined(String nickname) {
    }
    /**
     * Shows players that have left a game
     * @param nickname player's nickname
     */
    @Override
    public void showPlayerLeft(String nickname) {
    }
    /**
     * Shows waiting players
     */
    @Override
    public void showWaitPlayers() {

        Platform.runLater(() -> {
            synchronized (this) {
                FXMLLoader setupGameLoader = new FXMLLoader(getClass().getResource(ScenePath.WAITGAME.getPath()));
                Parent waitGameRoot = null;

                try {
                    waitGameRoot = setupGameLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                waitGameController = setupGameLoader.getController();

                Scene gameScene = new Scene(waitGameRoot);
                gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
                getPrimaryStage().setScene(gameScene);
                notifyAll();
            }
        });

        synchronized (this) {
            while (waitGameController == null) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that shows game setup
     */
    @Override
    public void showSetup() {

        Platform.runLater(() -> {
            synchronized (this) {
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
                notifyAll();
            }
        });

        synchronized (this) {
            while (setupGameController == null) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    /**
     * method that shows objectives
     */
    @Override
    public void showObjectives() {
    }
    /**
     * method that shows players that are in a ready status
     */
    @Override
    public void showPlayerReady(String nickname) {
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that shows game start
     */
    @Override
    public void showStart() {

        Platform.runLater(() -> {
            synchronized (this) {
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
                notifyAll();
            }
        });

        synchronized (this) {
            while (playGameController == null) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    /**
     * method that shows the current player
     */
    @Override
    public void showCurrentPlayer() {
        Platform.runLater(() -> {
            playGameController.updateCurrentPlayer();
        });
    }
    /**
     * method that shows placing phase
     */
    @Override
    public void showPlacingPhase() {
        Platform.runLater(() -> {
            playGameController.updatePlacingPhase();
        });
        showCardsArea(getGameView().getCurrentPlayer());
        showHand();
    }
    /**
     * method that shows drawing phase
     */
    @Override
    public void showDrawingPhase() {
        Platform.runLater(() -> {
            playGameController.updateDrawingPhase();
        });
        showDecks();
    }
    /**
     * method that shows cards area
     */
    @Override
    public void showCardsArea(String nickname) {
        Platform.runLater(() -> {
            playGameController.updateBoard();
        });
    }
    /**
     * method that shows player's hand
     */
    @Override
    public void showHand() {
        Platform.runLater(() -> {
            playGameController.updateHand();
        });
    }
    /**
     * method that shows decks
     */
    @Override
    public void showDecks() {
        Platform.runLater(() -> {
            playGameController.updateDecks();
        });
    }
    /**
     * method that shows scores
     */
    @Override
    public void showScores() {
        Platform.runLater(() -> {
            playGameController.updateScores();
        });
    }

    // END /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * method that shows end of the game
     */
    @Override
    public void showEnd() {

        Platform.runLater(() -> {
            synchronized (this) {
                FXMLLoader endGameLoader = new FXMLLoader(getClass().getResource(ScenePath.ENDGAME.getPath()));
                Parent waitGameRoot = null;

                try {
                    waitGameRoot = endGameLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                endGameController = endGameLoader.getController();

                Scene gameScene = new Scene(waitGameRoot);
                gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
                getPrimaryStage().setScene(gameScene);
                notifyAll();
            }
        });

        synchronized (this) {
            while (endGameController == null) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    // CHAT ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * method that shows chat message
     */
    @Override
    public void showChatMessage(String sender, String content, String time) {

        StringBuilder messageItem = new StringBuilder();
        if (getGameView().getNickname().equals(sender)) {
            messageItem.append("You: ");
        } else {
            messageItem.append(sender).append(": ");
        }
        messageItem.append(content).append(" (").append(time).append(")");
        this.chatMessages.add(messageItem.toString());

        if (waitGameController != null) {
            Platform.runLater(() -> {
                waitGameController.updateChat();
            });
        }

        if (setupGameController != null) {
            Platform.runLater(() -> {
                setupGameController.updateChat();
            });
        }

        if (playGameController != null) {
            Platform.runLater(() -> {
                playGameController.updateChat();
            });
        }

        if (endGameController != null) {
            Platform.runLater(() -> {
                endGameController.updateChat();
            });
        }
    }

    // OTHER ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * method that shows help commands
     */
    @Override
    public void showHelp() {
    }
    /**
     * method that shows error
     * @param content type of error
     */
    @Override
    public void showError(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
    /**
     * method that shows end session
     */
    @Override
    public void showEndSession() {
    }

    // COMMANDS ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * method that waits for commands
     */
    @Override
    public void listen() {
        waitForButtonPress();
        Pair<Command, String[]> command = readCommand();
        System.out.println("Add command: " + command.getKey() + ", " + Arrays.toString(command.getValue()));
        client.addCommand(command.getKey(), command.getValue());
    }

    /**
     * method that reads a command
     * @return a pair command/string
     */
    // commands must be read via GUI rather than terminal
    private Pair<Command, String[]> readCommand() {

        switch (getFirstWord(read)) {
            case "-co" -> {
                String arg = removeFirstWord(read);
                try {
                    return new Pair<>(Command.CHOOSE_OBJECTIVE,
                            new String[]{String.valueOf(Integer.parseInt(arg) - 1)});
                } catch (NumberFormatException e) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-cs" -> {
                String arg = removeFirstWord(read);
                try {
                    return new Pair<>(Command.CHOOSE_STARTER_FACE,
                            new String[]{String.valueOf(Integer.parseInt(arg) - 1)});
                } catch (NumberFormatException e) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-o" -> {
                String arg = removeFirstWord(read);
                if (arg.isEmpty()) {
                    return new Pair<>(Command.SHOW_OBJECTIVES, null);
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-p" -> {
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

            case "-d" -> {
                String arg = removeFirstWord(read);
                try {
                    return new Pair<>(Command.DRAW_CARD,
                            new String[]{String.valueOf(Integer.parseInt(arg) - 1)});
                } catch (NumberFormatException e) {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-c" -> {
                String arg = removeFirstWord(read);
                if (!arg.isEmpty()) {
                    return new Pair<>(Command.CHAT, new String[]{arg});
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-cp" -> {
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

            case "-l" -> {
                String arg = removeFirstWord(read);
                if (arg.isEmpty()) {
                    return new Pair<>(Command.LEAVE, null);
                } else {
                    return new Pair<>(Command.NOT_A_COMMAND, new String[]{"Invalid argument format"});
                }
            }

            case "-h" -> {
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

    /**
     * @return IP controller
     */
    public EnterIPController getEnterIPController() {
        return enterIPController;
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
     * @return create game controller
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

    // BUTTONS /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that waits for button press
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
     * method that resumes game execution
     */
    public void resumeExecution() {
        synchronized (lock) {
            waitingForButton = false; // Reset the flag
            lock.notifyAll(); // Notify to resume execution
        }
    }

    /**
     * method that sets a submitted ip
     * @param submittedIp a specific IP
     */
    public void setSubmittedIp(String submittedIp) {
        this.submittedIp = submittedIp;
    }
    /**
     * method that sets a submitted player nickname
     * @param submittedPlayerNickname a specific player nickname
     */
    public void setSubmittedPlayerNickname(String submittedPlayerNickname) {
        this.submittedPlayerNickname = submittedPlayerNickname;
    }

    /**
     * method that sets a submitted gaming choice
     * @param submittedGameChoice a specific game choice
     */
    public void setSubmittedGameChoice(int submittedGameChoice) {
        this.submittedGameChoice = submittedGameChoice;
    }
    /**
     * method that sets a submitted game name
     * @param submittedGameName a specific game name
     */
    public void setSubmittedGameName(String submittedGameName) {
        this.submittedGameName = submittedGameName;
    }
    /**
     * method that sets a submitted number of players
     * @param submittedNumPlayers a specific number of players
     */
    public void setSubmittedNumPlayers(int submittedNumPlayers) {
        this.submittedNumPlayers = submittedNumPlayers;
    }
    /**
     * method that sets a submitted end points
     * @param submittedEndPoints a specific end points
     */
    public void setSubmittedEndPoints(int submittedEndPoints) {
        this.submittedEndPoints = submittedEndPoints;
    }
    /**
     * method that sets a submitted join game name
     * @param submittedJoinGameName a specific join game name
     */
    public void setSubmittedJoinGameName(String submittedJoinGameName) {
        this.submittedJoinGameName = submittedJoinGameName;
    }

    /**
     * method that sets a read
     * @param read given read
     */
    public void setRead(String read) {
        this.read = read;
        resumeExecution();
    }

    // PARAMS //////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return game view
     */
    public GameView getGameView() {
        return client.getGameView();
    }
    /**
     * @return free games
     */
    public List<String> getFreeGames() {
        return new ArrayList<>(freeGames);
    }
    /**
     * @return chat message
     */
    public List<String> getChatMessages() {
        return chatMessages;
    }

    public String getCommonObjectiveCode(int index) {
        return getGameView().getCommonObjectives().get(index).getCode();
    }
    /**
     * @return secret objective
     */
    public String getSecretObjectiveCode(int index) {
        return getGameView().getSecreteObjectivesSelection().get(index).getCode();
    }

    public String getSecretObjectiveCode() {
        return getGameView().getSecretObjective().getCode();
    }

    /**
     * @return front of starter card
     */
    public String getStarterCardFrontCode() {
        return getGameView().getStarterCard().getFront().getCode();
    }

    /**
     * @return back of starter card
     */
    public String getStarterCardBackCode() {
        return getGameView().getStarterCard().getBack().getCode();
    }

    /**
     * @return player's hand
     */
    public List<PhysicalCard> getPlayerHand() {
        return getGameView().getHand();
    }

    /**
     * @return decks
     */
    public PlayableCard[] getDecks() {
        return getGameView().getDecks();
    }

    /**
     * @return who is the current player
     */
    public String getCurrentPlayer() {
        return getGameView().getCurrentPlayer();
    }
    /**
     * @return player area
     */
    public PlayerDataView getPlayerArea() {
        return getGameView().getPlayerArea(getCurrentPlayer());
    }

    /**
     * @return playing scores
     */
    public String getScoresPlaying() {
        Map<String, Integer> scores = new HashMap<>();
        for (String nickname : getGameView().getPlayerList()) {
            scores.put(nickname, getGameView().getPlayerArea(nickname).getTotalScore());
        }

        String scoresText = "SCORES: \n";
        for (String nickname : scores.keySet()) {
            scoresText += ("Player \"" + nickname + "\" -> " + scores.get(nickname) + "\n");
        }
        return scoresText;
    }

    /**
     * @return end scores
     */
    public String getScoresEnd() {
        Map<String, Pair<Integer, Integer>> scores = new HashMap<>();
        for (String nickname : getGameView().getPlayerList()) {
            scores.put(nickname, new Pair(getGameView().getPlayerArea(nickname).getTotalScore(),
                    getGameView().getPlayerArea(nickname).getObjectivesScore()));
        }

        String scoresText = "SCORES: \n";
        for (String nickname : scores.keySet()) {
            scoresText += ("Player \"" + nickname + "\" -> total: " + scores.get(nickname).getKey() +
                    ", objectives: " + scores.get(nickname).getValue() + "\n");
        }
        return scoresText;
    }
}
