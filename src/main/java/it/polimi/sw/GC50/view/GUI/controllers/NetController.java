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

    private int netSelected;

    @FXML
    public void initialize() throws Exception {
    }

    @FXML
    public void handleSocketButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) socketButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.USER.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        netSelected = 1;
    }

    @FXML
    public void handleRmiButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) rmiButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.USER.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        netSelected = 2;
    }

    public Button getSocketButton() {
        return socketButton;
    }

    public Button getRmiButton() {
        return rmiButton;
    }

    public int getNetSelected() {
        return netSelected;
    }

}
