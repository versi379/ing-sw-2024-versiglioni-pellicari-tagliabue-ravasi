package it.polimi.sw.GC50.view.GUI.scenes;

/**
 * Used to store the paths of the FXML files of the scenes.
 */
public enum ScenePath {

    NET("/scenes/Net.fxml"),
    USER("/scenes/User.fxml"),
    MENU("/scenes/Menu.fxml"),
    CREATEGAME("/scenes/CreateGame.fxml"),
    JOINGAME("/scenes/JoinGame.fxml"),
    RULES("scenes/Rules.fxml"),
    SETUPGAME("/scenes/SetupGame.fxml"),
    PLAYGAME("/scenes/PlayGame.fxml");

    private final String path;

    ScenePath(final String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
