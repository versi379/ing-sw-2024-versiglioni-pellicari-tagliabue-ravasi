package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.view.GUI.controllers.GUIController;
import it.polimi.sw.GC50.view.GUI.controllers.NetController;
import it.polimi.sw.GC50.view.GUI.scenes.SceneInfo;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuiView extends Application implements View {

    private Stage primaryStage;
    private StackPane root;
    private ArrayList<SceneInfo> scenes;
    private GameView gameView;

    NetController netController;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ScenePath.USERCONNECTION.getPath()));
        Parent root = loader.load();
        netController = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String selectName() {
        return netController.getPlayerNickname() != null ? netController.getPlayerNickname() : "Giocatore";
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
    public int selectJoinOrCreate() {
        return 0;
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
    public void showFreeGames(Map<String, List<String>> freeGames) {

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
}
