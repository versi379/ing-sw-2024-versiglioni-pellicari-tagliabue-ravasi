package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.List;

public class JoinGameController {

    @FXML
    private ListView<String> freeGames;

    @FXML
    private Button enterGameButton;

    private GuiView guiView;

    @FXML
    public void initialize() throws InterruptedException {
//        Label emptyGamesListLabel = new Label("No free games.");
//        freeGames.setPlaceholder(emptyGamesListLabel);
        guiView = (GuiView) AppClient.getView();
    }

    @FXML
    public void handleEnterGameButton(ActionEvent event) throws Exception {


        showGameView();
    }

    public void showGameView() throws Exception {
        Stage stage = (Stage) enterGameButton.getScene().getWindow();
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.GAME.getPath()));
        Parent userRoot = userLoader.load();
        Scene userScene = new Scene(userRoot);
        stage.setScene(userScene);
    }

    public void setFreeGames(String gameAndPlayers) {
        this.freeGames.getItems().add(gameAndPlayers);
    }
}
