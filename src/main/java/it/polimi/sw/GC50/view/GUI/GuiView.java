package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.util.PlaceCardRequest;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.GUI.controllers.GameControllerGUI;
import it.polimi.sw.GC50.view.GUI.controllers.MenuController;
import it.polimi.sw.GC50.view.GUI.controllers.NetController;
import it.polimi.sw.GC50.view.GUI.controllers.UserController;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class GuiView extends Application implements View {

    private GameView gameView;

    private Stage primaryStage;

    // RIF client
    private ClientRmi clientRmi;

    private String submittedPlayerNickname;

    // GUI Controllers
    private NetController netController;
    private UserController userController;
    private MenuController menuController;
    private GameControllerGUI gameController;

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
        gameController = gameLoader.getController();

        Scene scene = new Scene(netRoot);
        stage.setScene(scene);
        stage.show();
    }

    public GuiView() {

    }

    @Override
    public String selectName() throws InterruptedException {
        while(userController == null) {
            System.out.println("ATTENDO CARICAMENTO USER LOGIN PAGE");
        }
        System.out.println("stampa nome dopo caricamento user login: " + userController.getPlayerNickname());

        waitForButtonPress();

        System.out.println("NOME CHE MANDA AL SERVER: !!! " + submittedPlayerNickname);

        return submittedPlayerNickname;
    }

    @Override
    public int selectJoinOrCreate() {
        return menuController.getGameChoice();
    }

    @Override
    public void showFreeGames(Map<String, List<String>> freeGames) {

    }

    @Override
    public String selectGameName() {
        return null;
    }

    @Override
    public int selectNumberOfPlayers() {
        return 0;
    }

    @Override
    public int selectEndScore() {
        return 0;
    }

    @Override
    public void showWaitPlayers() {

    }

    @Override
    public void showSetup() {

    }

    @Override
    public void setModel(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void updateChat(Chat chat) {

    }

    @Override
    public int selectObjectiveCard() {
        return 0;
    }

    @Override
    public int selectStarterFace() {
        return 0;
    }

    @Override
    public PlaceCardRequest selectPlaceCard() {
        return null;
    }

    @Override
    public int selectDrawingPosition() {
        return 0;
    }

    @Override
    public void showMessage(String message) {
    }

    @Override
    public Pair<Command, List<Integer>> listenCommands() {
        return null;
    }

    @Override
    public void showPlayerArea(String nickname) {
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
    public void showError(String content) {

    }

    @Override
    public void showCommonObjectives() {

    }

    @Override
    public void showPlayerReady(String nickname) {

    }

    @Override
    public void showStart() {

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
        return gameController;
    }

    public void setClientRmi(ClientRmi clientRmi) {
        this.clientRmi = clientRmi;
    }



    private void waitForButtonPress() {
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
            lock.notify(); // Notify to resume execution
        }
    }

    public void setSubmittedPlayerNickname(String submittedPlayerNickname) {
        this.submittedPlayerNickname = submittedPlayerNickname;
    }
}
