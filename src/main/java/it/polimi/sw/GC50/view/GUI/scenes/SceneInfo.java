package it.polimi.sw.GC50.view.GUI.scenes;

import it.polimi.sw.GC50.view.GUI.controllers.GUIController;
import javafx.scene.Scene;

/**
 * This class is used to store information about a scene.
 */
public class SceneInfo {

    private Scene scene;
    private ScenePath scenePath;
    private GUIController controller;

    public SceneInfo(Scene scene, ScenePath scenePath, GUIController controller) {
        this.scene = scene;
        this.scenePath = scenePath;
        this.controller = controller;
    }

    public Scene getScene() {
        return scene;
    }

    public ScenePath getScenePath() {
        return scenePath;
    }

    public GUIController getController() {
        return controller;
    }

}
