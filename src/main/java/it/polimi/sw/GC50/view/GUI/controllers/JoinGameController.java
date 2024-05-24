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

    @FXML
    public void initialize() throws InterruptedException {
        guiView = (GuiView) AppClient.getView();
        freeGames.setItems(guiView.getMenuController().gameItems2);
        freeGames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
        });
    }

    @FXML
    public void handleEnterGameButton(ActionEvent event) throws Exception {
        showGameView();
        //guiView.setSubmittedJoinGameName();
        guiView.resumeExecution();
//        String[] parts = selectedItem.split("\nPLAYERS: ");
//        if (parts.length > 0) {
//            String game = parts[0];
//            System.out.println("Extracted game: " + game);
//        } else {
//            System.out.println("Invalid format");
//        }
    }

    public void showGameView() throws Exception {
        Stage stage = (Stage) enterGameButton.getScene().getWindow();
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.GAME.getPath()));
        Parent userRoot = userLoader.load();
        Scene userScene = new Scene(userRoot);
        stage.setScene(userScene);
    }

    public ListView<String> getFreeGamesListView() {
        return freeGames;
    }



}
