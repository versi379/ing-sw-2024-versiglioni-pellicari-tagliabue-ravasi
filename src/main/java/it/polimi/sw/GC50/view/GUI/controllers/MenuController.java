package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.rmi.ssl.SslRMIClientSocketFactory;

/**
 * Controller for Menu FXML scene.
 */
public class MenuController {

    @FXML
    private Button createNewGameButton;

    @FXML
    private Button joinGameButton;

    @FXML
    private Button quitButton;

    private int gameChoice;

    private GuiView guiView;


    public ObservableList<String> gameItems2 = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws Exception {
        guiView = (GuiView) AppClient.getView();
    }

    public void showCreateGameView() throws Exception {
        Stage stage = (Stage) createNewGameButton.getScene().getWindow();
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.CREATEGAME.getPath()));
        Parent userRoot = userLoader.load();
        Scene userScene = new Scene(userRoot);
        stage.setScene(userScene);
    }

    // deve essere lanciato quando i free games sono aggiornati (showfreegames)
    public void showJoinGameView() throws Exception {
        Stage stage = (Stage) joinGameButton.getScene().getWindow();
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.JOINGAME.getPath()));
        Parent userRoot = userLoader.load();
        Scene userScene = new Scene(userRoot);
        stage.setScene(userScene);
    }

    @FXML
    public void handleCreateNewGameButton(ActionEvent event) throws Exception {
        gameChoice = 1;
        showCreateGameView();
        guiView.setSubmittedGameChoice(gameChoice);
        guiView.resumeExecution();
    }

    @FXML
    public void handleJoinGameButton(ActionEvent event) throws Exception {
        gameChoice = 2;
        guiView.setSubmittedGameChoice(gameChoice);
        guiView.resumeExecution();
        showJoinGameView();
    }

    @FXML
    public void handleQuitButton(ActionEvent event) {

    }

    public int getGameChoice() {
        return gameChoice;
    }

}
