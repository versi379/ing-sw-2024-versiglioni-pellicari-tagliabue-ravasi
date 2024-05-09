package it.polimi.sw.GC50.view.GUI.scenes;

/**
 * Used to store the paths of the FXML files of the scenes.
 */
public enum ScenePath {

    MENU("/Menu.fxml");

    private final String path;

    ScenePath(final String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
