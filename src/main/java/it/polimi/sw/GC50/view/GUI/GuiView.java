package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import it.polimi.sw.GC50.net.util.Client;
import it.polimi.sw.GC50.view.GUI.controllers.*;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.TUI.TuiModelPrinter;
import it.polimi.sw.GC50.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

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
    private GameControllerGUI gameControllerGUI;
    private CreateGameController createGameController;
    private JoinGameController joinGameController;

    public String setupCommonObjectives;
    public String setupSecretObjectives;

    private Object lock = new Object(); // Object for synchronization
    private volatile boolean waitingForButton = false; // Flag to indicate if client thread is waiting for button press


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

        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource(ScenePath.GAME.getPath()));
        Parent gameRoot = gameLoader.load();
        gameControllerGUI = gameLoader.getController();

        FXMLLoader createGameLoader = new FXMLLoader(getClass().getResource(ScenePath.CREATEGAME.getPath()));
        Parent createGameRoot = createGameLoader.load();
        createGameController = createGameLoader.getController();

        FXMLLoader joinGameLoader = new FXMLLoader(getClass().getResource(ScenePath.JOINGAME.getPath()));
        Parent joinGameRoot = joinGameLoader.load();
        joinGameController = joinGameLoader.getController();

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
    public String selectName() throws InterruptedException {
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

//        System.out.println();
//        System.out.println(blueTxt + "Select the secret objective card and starter card face you want to play with:" + baseTxt);
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
        TuiModelPrinter.printStarterCard(getGameView().getStarterCard()); //
        PlayableCard starterFront = getGameView().getStarterCard().getFront();
        PlayableCard starterBack = getGameView().getStarterCard().getBack();
        System.out.println(GuiModelPrinter.identifyPlayableCard(starterFront));
    }

    @Override
    public void showChatMessage(Chat chat) {

    }

    @Override
    public void showChatMessage(String sender, String content, String time) {
    }

    @Override
    public void listen() {

    }

    @Override
    public void showCardsArea(String nickname) {
    }

    @Override
    public void showHand() {

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
    public void showStart() {

    }

    @Override
    public void showPlacingPhase() {

    }

    @Override
    public void showDrawingPhase() {

    }

    @Override
    public void showCurrentPlayer() {

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

    public GameControllerGUI getGameController() {
        return gameControllerGUI;
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
