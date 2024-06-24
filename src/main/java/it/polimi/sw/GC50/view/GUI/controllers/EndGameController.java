package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for Game FXML scene.
 */
public class EndGameController {
    private GuiView guiView;

    @FXML
    private Button leaveGameButton;

    /**
     * method used to initialize wait game controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
    }

    @FXML
    private void handleLeaveGameButton(ActionEvent event) {
        guiView.setRead("-l");
    }
}
