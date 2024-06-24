package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
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
import java.util.*;

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
    private int submittedSetupStarter;
    private int submittedSetupObjective;
    private List<String> chatMessages = new ArrayList<>();

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

    public GuiView() {
        // Ensure JavaFX is initialized
        Platform.startup(() -> {
            // Launch the JavaFX Application
            Stage stage = new Stage();
            start(stage);
        });
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void showConnected() {
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String selectServerIp() {

        waitForButtonPress();
        return submittedIp;
    }

    @Override
    public int selectConnectionType() {

        Platform.runLater(() -> {
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
        });

        waitForButtonPress();
        return getNetController().getNetSelected();
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
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

        this.freeGames = new ArrayList<>();
        if (!freeGames.isEmpty()) {
            for (String game : freeGames.keySet()) {
                StringBuilder gameItem = new StringBuilder(game + "\nPLAYERS: ");
                for (String nickname : freeGames.get(game)) {
                    gameItem.append(nickname).append(", ");
                }
                if (gameItem.length() > 0) {
                    gameItem.setLength(gameItem.length() - 2);
                }
                this.freeGames.add(gameItem.toString());
            }
        }
    }

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

    @Override
    public int selectNumberOfPlayers() {
        return submittedNumPlayers;
    }

    @Override
    public int selectEndScore() {
        return submittedEndPoints;
    }

    @Override
    public String selectJoinGameName() {
        waitForButtonPress();
        return submittedJoinGameName;
    }

    // WAITING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void showPlayerJoined(String nickname) {
    }

    @Override
    public void showPlayerLeft(String nickname) {
    }

    @Override
    public void showWaitPlayers() {

        Platform.runLater(() -> {
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
        });
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    }

    @Override
    public void showObjectives() {
    }

    @Override
    public void showPlayerReady(String nickname) {
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    }

    @Override
    public void showCurrentPlayer() {
        Platform.runLater(() -> {
            playGameController.updateCurrentPlayer();
        });
    }

    // questo metodo viene chiamato per il solo giocatore che deve piazzare una carta (cioè è il suo turno)
    @Override
    public void showPlacingPhase() {
        System.out.println("> placing phase");
        showCardsArea(getGameView().getCurrentPlayer());
        showHand();
    }

    @Override
    public void showDrawingPhase() {
        System.out.println("> drawing phase");
        showDecks();
    }

    @Override
    public void showCardsArea(String nickname) {
        if (playGameController != null) {
            Platform.runLater(() -> {
                playGameController.updateBoard();
            });
        }
    }

    @Override
    public void showHand() {
        if (playGameController != null) {
            Platform.runLater(() -> {
                playGameController.updateHand();
            });
        }
    }

    @Override
    public void showDecks() {
        if (playGameController != null) {
            Platform.runLater(() -> {
                playGameController.updateDecks();
            });
        }
    }

    @Override
    public void showScores() {
        if (playGameController != null) {
            Platform.runLater(() -> {
                playGameController.updateScores();
            });
        }
    }

    // END /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void showEnd() {

        Platform.runLater(() -> {
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
        });
    }

    // CHAT ////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    }

    // OTHER ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void showHelp() {
    }

    @Override
    public void showError(String content) {
        System.err.println("> Error: " + content);
    }

    @Override
    public void showEndSession() {
    }

    // COMMANDS ////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void listen() {
        waitForButtonPress();
        Pair<Command, String[]> command = readCommand();
        System.out.println("Add command: " + command.getKey() + ", " + Arrays.toString(command.getValue()));
        client.addCommand(command.getKey(), command.getValue());
    }

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

    public EnterIPController getEnterIPController() {
        return enterIPController;
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

    // BUTTONS /////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    public void setSubmittedIp(String submittedIp) {
        this.submittedIp = submittedIp;
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

    public void setRead(String read) {
        this.read = read;
        resumeExecution();
    }

    // PARAMS //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private GameView getGameView() {
        return client.getGameView();
    }

    public List<String> getFreeGames() {
        return new ArrayList<>(freeGames);
    }

    public List<String> getChatMessages() {
        return chatMessages;
    }

    public void setSubmittedSetupStarter(int submittedSetupStarter) {
        this.submittedSetupStarter = submittedSetupStarter;
    }

    public int getSubmittedSetupStarter() {
        return submittedSetupStarter;
    }

    public void setSubmittedSetupObjective(int submittedSetupObjective) {
        this.submittedSetupObjective = submittedSetupObjective;
    }

    public int getSubmittedSetupObjective() {
        return submittedSetupObjective;
    }

    public String getCommonObjectiveCode(int index) {
        return getGameView().getCommonObjectives().get(index).getCode();
    }

    public String getSecretObjectiveCode(int index) {
        return getGameView().getSecreteObjectivesSelection().get(index).getCode();
    }

    public String getStarterCardFrontCode() {
        return getGameView().getStarterCard().getFront().getCode();
    }

    public String getStarterCardBackCode() {
        return getGameView().getStarterCard().getBack().getCode();
    }

    public List<PhysicalCard> getPlayerHand() {
        return getGameView().getHand();
    }

    public PlayableCard[] getDecks() {
        return getGameView().getDecks();
    }

    public String getCurrentPlayer() {
        return getGameView().getCurrentPlayer();
    }

    public PlayerDataView getPlayerArea() {
        return getGameView().getPlayerArea(getCurrentPlayer());
    }

    public String getScoresText() {
        Map<String, Integer> scores = new HashMap<>();
        for (String nickname : getGameView().getPlayerList()) {
            scores.put(nickname, getGameView().getPlayerArea(nickname).getTotalScore());
        }

        String scoresText = "";
        for (String nickname : scores.keySet()) {
            scoresText = scoresText += (nickname + ": " + scores.get(nickname) + "\n");
        }
        return scoresText;
    }
}
