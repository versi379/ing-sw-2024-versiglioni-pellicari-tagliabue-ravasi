package it.polimi.sw.GC50.view.GUI.scenes;

/**
 * Used to store the paths of the FXML files of the scenes.
 */
public enum ScenePath {

    ENTERIP("/scenes/EnterIP.fxml"),
    NET("/scenes/Net.fxml"),
    USER("/scenes/User.fxml"),
    MENU("/scenes/Menu.fxml"),
    CREATEGAME("/scenes/CreateGame.fxml"),
    JOINGAME("/scenes/JoinGame.fxml"),
    RULES("/scenes/Rules.fxml"),
    WAITGAME("/scenes/WaitGame.fxml"),
    SETUPGAME("/scenes/SetupGame.fxml"),
    PLAYGAME("/scenes/PlayGame.fxml"),
    ENDGAME("/scenes/EndGame.fxml");

    private final String path;

    /**
     * constructs an instance of scene path
     * @param path file path
     */
    ScenePath(String path) {
        this.path = path;
    }

    /**
     * @return file path
     */
    public String getPath() {
        return path;
    }
}
