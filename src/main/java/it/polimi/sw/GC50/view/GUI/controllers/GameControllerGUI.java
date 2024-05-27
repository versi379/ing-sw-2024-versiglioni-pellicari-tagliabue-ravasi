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

    private GuiView guiView;

    @FXML
    private Label commonObjectivesLabel;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
        commonObjectivesLabel.setText(guiView.prova);
        System.out.println("schermata game inizializzata");
    }

    public void setCommonObjectivesLabel(String text) {
        this.commonObjectivesLabel.setText(text);
    }
}
