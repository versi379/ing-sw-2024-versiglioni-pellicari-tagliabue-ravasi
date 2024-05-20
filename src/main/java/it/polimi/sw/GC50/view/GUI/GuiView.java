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
    public void waitPlayers() {

    }

    @Override
    public int selectJoinOrCreate() {
        return 0;
    }

    @Override
    public void setup() {

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
    public void printMessage(String message) {
    }

    @Override
    public void printPlayerArea(String nickname) {
    }

    @Override
    public void printDecks() {

    }

    @Override
    public void printScores() {
    }

    @Override
    public void playerJoined(String nickname) {

    }

    @Override
    public void playerLeft(String nickname) {

    }

    @Override
    public void endSession() {

    }

    @Override
    public void printCommonObjectives() {

    }

    @Override
    public void playerReady(String nickname) {

    }

    @Override
    public void error() {

    }
}
