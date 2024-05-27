package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for Game FXML scene.
 */
public class GameControllerGUI {

    @FXML
    private AnchorPane pane;

    private GuiView guiView;

    @FXML
    private Label commonObjectivesLabel;

    @FXML
    private Label secretObjectivesLabel;

    private ImageView imageView;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
        commonObjectivesLabel.setText(guiView.setupCommonObjectives);
        secretObjectivesLabel.setText(guiView.setupSecretObjectives);
        Image starterCardFront = new Image(String.valueOf(getClass().getResource("/cards/fronts/30022.jpg")));
        imageView = new ImageView(starterCardFront);
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        pane.getChildren().add(imageView);
        Image starterCardBack = new Image(String.valueOf(getClass().getResource("/cards/backs/0002.jpg")));
        imageView = new ImageView(starterCardBack);
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        pane.getChildren().add(imageView);
    }



}
