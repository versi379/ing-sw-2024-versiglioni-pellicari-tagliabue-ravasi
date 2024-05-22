package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for Menu FXML scene.
 */
public class MenuController {

    @FXML
    private Button createNewGameButton;

    @FXML
    private Button joinGameButton;

    @FXML
    private Button reconnectToGameButton;

    @FXML
    private Button quitGameButton;

    @FXML
    private Button userMenuButton;

    private int gameChoice;

    @FXML
    public void handleCreateNewGameButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) createNewGameButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.GAME.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        gameChoice = 1;
    }

    @FXML
    public void handleJoinGameButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) joinGameButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.GAME.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        gameChoice = 2;
    }

    @FXML
    public void handleReconnectToGameButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) reconnectToGameButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.GAME.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void handleQuitGameButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) quitGameButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.GAME.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        gameChoice = 3;
    }

    @FXML
    public void handleUserMenuButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) userMenuButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.USER.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public int getGameChoice() {
        return gameChoice;
    }

}
