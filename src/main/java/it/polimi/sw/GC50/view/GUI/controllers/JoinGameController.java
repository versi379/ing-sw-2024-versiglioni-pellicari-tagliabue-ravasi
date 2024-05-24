package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.List;

public class JoinGameController {

    @FXML
    private Pane joinGamePane;

    @FXML
    public ListView<String> freeGames;

    @FXML
    private Button enterGameButton;

    private GuiView guiView;

    private String submittedJoinGameName;

    @FXML
    public void initialize() throws InterruptedException {
        guiView = (GuiView) AppClient.getView();
        freeGames.setItems(guiView.getMenuController().gameItems2);
        freeGames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selectedListViewItem = newValue;
            String[] parts = selectedListViewItem.split("\nPLAYERS: ");
            submittedJoinGameName = parts[0];
        });
    }

    @FXML
    public void handleEnterGameButton(ActionEvent event) throws Exception {
        showGameView();
        guiView.setSubmittedJoinGameName(submittedJoinGameName);
        guiView.resumeExecution();
    }

    public void showGameView() throws Exception {
        Stage stage = (Stage) enterGameButton.getScene().getWindow();
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource(ScenePath.GAME.getPath()));
        Parent gameRoot = gameLoader.load();
        Scene gameScene = new Scene(gameRoot);
        gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(gameScene);
    }

    public ListView<String> getFreeGamesListView() {
        return freeGames;
    }



}
