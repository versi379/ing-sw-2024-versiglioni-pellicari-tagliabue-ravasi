package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NetController {

    @FXML
    private Button socketButton;

    @FXML
    private Button rmiButton;

    private boolean netSetted;
    private int netSelected;

    @FXML
    public void initialize() throws Exception {
        netSetted = false;
        netSelected = 1;
        socketButton.setOnAction(event -> {
            netSetted = true;
            netSelected = 1;
            System.out.println("Scelta Socket, carico login view...");
            try {
                showUserView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        rmiButton.setOnAction(event -> {
            netSetted = true;
            netSelected = 2;
            System.out.println("Scelta RMI, carico login view...");
            try {
                showUserView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void showUserView() throws Exception{
        Stage stage = (Stage) socketButton.getScene().getWindow();
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.USER.getPath()));
        Parent userRoot = userLoader.load();
        Scene userScene = new Scene(userRoot);
        userScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(userScene);
    }

    public Button getSocketButton() {
        return socketButton;
    }

    public Button getRmiButton() {
        return rmiButton;
    }

    public boolean isnetSetted() {
        return netSetted;
    }

    public int getNetSelected() {
        return netSelected;
    }

}
