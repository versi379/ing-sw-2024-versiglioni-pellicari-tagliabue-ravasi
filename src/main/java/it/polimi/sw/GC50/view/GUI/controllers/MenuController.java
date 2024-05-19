package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for Menu FXML scene.
 */
public class MenuController extends GUIController {

    @FXML
    private Button createGameButton;

    @FXML
    private Button joinGameButton;

    @FXML
    private Button reconnectToGameButton;

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

}
