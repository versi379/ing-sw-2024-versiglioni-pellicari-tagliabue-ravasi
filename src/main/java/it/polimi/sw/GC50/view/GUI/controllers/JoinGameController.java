package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.game.GameStatus;
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
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.List;

public class JoinGameController {
    private GuiView guiView;
    @FXML
    private Pane joinGamePane;

    @FXML
    public ListView<String> freeGames;

    @FXML
    private Button enterGameButton;

    private String submittedJoinGameName;

    public boolean waitingPlayers = true;

    @FXML
    private ProgressIndicator waitingPlayersBuffer;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
        freeGames.setItems(guiView.getMenuController().gameItems2);
        freeGames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selectedListViewItem = newValue;
            String[] parts = selectedListViewItem.split("\nPLAYERS: ");
            submittedJoinGameName = parts[0];
        });
    }

    @FXML
    public void handleEnterGameButton(ActionEvent event) {
        guiView.setSubmittedJoinGameName(submittedJoinGameName);
        guiView.resumeExecution();
    }
}
