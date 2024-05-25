package it.polimi.sw.GC50.view.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
 * Controller for Game FXML scene.
 */
public class GameControllerGUI {

    @FXML
    private ProgressIndicator waitingPlayersIndicator;

    @FXML
    private Label waitingPlayersLabel;

    public ProgressIndicator getWaitingPlayersIndicator() {
        return waitingPlayersIndicator;
    }

    public Label getWaitingPlayersLabel() {
        return waitingPlayersLabel;
    }
}
