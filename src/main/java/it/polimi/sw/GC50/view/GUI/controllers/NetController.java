package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NetController {

    private GuiView guiView;

    @FXML
    private Button socketButton;

    @FXML
    private Button rmiButton;

    @FXML
    private Button quitButton;

    private int netSelected;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();

        netSelected = 3;
        socketButton.setOnAction(event -> {
            netSelected = 1;
            guiView.resumeExecution();
        });
        rmiButton.setOnAction(event -> {
            netSelected = 2;
            guiView.resumeExecution();
        });
        quitButton.setOnAction(event -> {
            netSelected = 3;
            guiView.resumeExecution();
        });
    }

    public int getNetSelected() {
        return netSelected;
    }

    /*
     public Button getSocketButton() {
        return socketButton;
    }

    public Button getRmiButton() {
        return rmiButton;
    }

    public void showUserView() throws IOException {
        Stage stage = (Stage) socketButton.getScene().getWindow();
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.USER.getPath()));
        Parent userRoot = userLoader.load();
        Scene userScene = new Scene(userRoot);
        userScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(userScene);
    }

     */
}
