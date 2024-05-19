package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserController extends GUIController {

    @FXML
    private TextField playerNickname;

    @FXML
    private Button enterLobbyButton;

    @FXML
    private Button netConnectionButton;

    @FXML
    public void handleEnterLobbyButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) enterLobbyButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.MENU.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleNetConnectionButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) netConnectionButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.MENU.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
