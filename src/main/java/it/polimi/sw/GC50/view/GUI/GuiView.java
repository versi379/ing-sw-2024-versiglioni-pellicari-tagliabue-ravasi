package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.view.GUI.controllers.GUIController;
import it.polimi.sw.GC50.view.GUI.scenes.SceneInfo;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
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
    private ModelMex modelmex;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Game.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method uses the FXMLLoader to load the scenes of the game
     * and their respective controllers, for each client.
     */
    private void loadScenes() {
        FXMLLoader loader;
        GUIController controller;
        for (int i = 0; i < ScenePath.values().length; i++) {
            loader = new FXMLLoader(getClass().getResource(ScenePath.values()[i].getPath()));
            try {
                root = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scenes.add(new SceneInfo(new Scene(root), ScenePath.values()[i], controller));
        }
    }

    /**
     * This method extracts the controller of a given scene.
     */
    public GUIController getController(ScenePath scenePath) {
        int index = getSceneIndex(scenePath);
        if (index != -1) {
            return scenes.get(getSceneIndex(scenePath)).getController();
        }
        return null;
    }

    private int getSceneIndex(ScenePath scenePath) {
        for (int i = 0; i < scenes.size(); i++) {
            if (scenes.get(i).getScenePath().equals(scenePath))
                return i;
        }
        return -1;
    }

    @Override
    public String selectName() {
        return "giovanni";
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
    public void waitPlayer() {

    }

    @Override
    public int selectJoinOrCreate() {
        return 0;
    }

    @Override
    public void allPlayerReady() {

    }

    @Override
    public void addModel(ModelMex modelmex) {
        this.modelmex = modelmex;
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

}
