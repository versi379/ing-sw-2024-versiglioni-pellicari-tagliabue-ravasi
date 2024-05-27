package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
 * Controller for Game FXML scene.
 */
public class GameControllerGUI {

    @FXML
    public ProgressIndicator waitingPlayersIndicator;

    @FXML
    public Label waitingPlayersLabel;

    private GuiView guiView;

    @FXML
    public void initialize() {
        //guiView = (GuiView) AppClient.getView();
        //System.out.println("waiting players create: "+guiView.getCreateGameController().waitingPlayers);
        //System.out.println("waiting players join: "+guiView.getJoinGameController().waitingPlayers);
        //guiView.waitForButtonPress();
        System.out.println("prova");
    }

}
