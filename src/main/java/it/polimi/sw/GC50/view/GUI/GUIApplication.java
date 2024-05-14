package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.view.GUI.controllers.GUIController;
import it.polimi.sw.GC50.view.GUI.controllers.GameController;
import it.polimi.sw.GC50.view.GUI.controllers.MenuController;
import it.polimi.sw.GC50.view.GUI.scenes.SceneInfo;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is the main class of the GUI, and it is used to start the GUI. It contains all the
 * methods to change the scene and to get the controller of a specific scene.
 */
public class GUIApplication extends Application {

    private Stage primaryStage;
    private StackPane root;
    private ArrayList<SceneInfo> scenes;

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadScenes();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Codex Naturalis");
        root = new StackPane();
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

}
