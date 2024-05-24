package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;

import java.util.List;

public class JoinGameController {

    @FXML
    private ListView<String> freeGames;

    @FXML
    private Button joinGameButton;

    private GuiView guiView;

    @FXML
    public void initialize() {
        Label emptyGamesListLabel = new Label("No free games.");
        freeGames.setPlaceholder(emptyGamesListLabel);
        guiView = (GuiView) AppClient.getView();
    }

    @FXML
    public void handleJoinGameButton(ActionEvent event) throws Exception {


        //guiView.resumeExecution();
    }

    public void setFreeGames(String gameAndPlayers) {
        this.freeGames.getItems().add(gameAndPlayers);
    }
}
