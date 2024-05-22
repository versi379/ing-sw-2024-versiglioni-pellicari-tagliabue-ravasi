package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.view.GUI.controllers.GameControllerGUI;
import it.polimi.sw.GC50.view.GUI.controllers.MenuController;
import it.polimi.sw.GC50.view.GUI.controllers.NetController;
import it.polimi.sw.GC50.view.GUI.controllers.UserController;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GuiView extends Application implements View {

    private GameView gameView;

    private Stage primaryStage;

    // GUI Controllers
    private NetController netController;
    private UserController userController;
    private MenuController menuController;
    private GameControllerGUI gameController;

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
    public String selectName() {
        while(userController == null) {
            System.out.println("ATTENDO SCHERMATA USER");
        }
        while(userController.getPlayerNickname() == null) {
            System.out.println("ATTENDO SCELTA USERNAME");
        }
        System.out.println("username impostato");
        // qui il nome è impostato ed è stata cambiata scena
        return userController.getPlayerNickname();
//        return "giovanni";
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
    public void showWaitPlayers() {

    }

    @Override
    public void showSetup() {

    }

    @Override
    public void addModel(GameView gameView) {
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
    public boolean selectStarterFace() {
        return true;
    }

    @Override
    public int game() {
        return 0;
    }

    @Override
    public void updateBoard() {

    }

    @Override
    public PlaceCardMex selectPlaceCard() {
        return null;
    }

    @Override
    public DrawingPosition selectDrawingPosition() {
        return null;
    }

    @Override
    public void showMessage(String message) {
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
    public void showPlayerJoined(String nickname) {

    }

    @Override
    public void showPlayerLeft(String nickname) {

    }

    @Override
    public void showEndSession() {

    }

    @Override
    public void showCommonObjectives() {

    }

    @Override
    public void showPlayerReady(String nickname) {

    }

    @Override
    public void showError() {

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
}
