package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.util.Client;
import it.polimi.sw.GC50.net.util.PlaceCardRequest;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.GUI.controllers.GameControllerGUI;
import it.polimi.sw.GC50.view.GUI.controllers.MenuController;
import it.polimi.sw.GC50.view.GUI.controllers.NetController;
import it.polimi.sw.GC50.view.GUI.controllers.UserController;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import it.polimi.sw.GC50.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class GuiView extends Application implements View {

    private Stage primaryStage;

    // RIF client
    private ClientRmi clientRmi;

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
    public void setClient(Client client) {

    }

    @Override
    public String selectName() throws InterruptedException {
        while(userController == null) {
            System.out.println("ATTENDO CARICAMENTO USER LOGIN PAGE");
        }
        System.out.println("stampa nome dopo caricamento user login: " + userController.getPlayerNickname());
//        while(!userController.isNameSetted()) {
//            System.out.println("ATTENDO CHE UTENTE CONFERMI NOME");
//        }

        AppClient.getClientThread().sleep(5000);

        System.out.println("stampa nome dopo aver confermato: " + userController.getPlayerNickname());
        return userController.getPlayerNickname();
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
    public void updateChat(Chat chat) {

    }

    @Override
    public void showMessage(String message) {
    }

    @Override
    public void listen() {

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
}
