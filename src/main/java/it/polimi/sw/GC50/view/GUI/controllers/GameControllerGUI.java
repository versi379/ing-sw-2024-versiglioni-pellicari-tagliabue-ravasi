package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    private ImageView imageViewStarterFront;
    private ImageView imageViewStarterBack;

    @FXML
    private Button starterBackButton;

    @FXML
    private Button starterFrontButton;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
        commonObjectivesLabel.setText(guiView.setupCommonObjectives);
        secretObjectivesLabel.setText(guiView.setupSecretObjectives);
        System.out.println(guiView.starterCardCode);
        Image starterCardFront = new Image(String.valueOf(getClass().getResource("/cards/fronts/" + guiView.starterCardCode + ".jpg")));
        imageViewStarterFront = new ImageView(starterCardFront);
        imageViewStarterFront.setFitWidth(200);
        imageViewStarterFront.setFitHeight(150);
        imageViewStarterFront.setLayoutX(90);
        imageViewStarterFront.setLayoutY(325);
        pane.getChildren().add(imageViewStarterFront);
        Image starterCardBack = new Image(String.valueOf(getClass().getResource("/cards/backs/" + guiView.starterCardCode + ".jpg")));
        imageViewStarterBack = new ImageView(starterCardBack);
        imageViewStarterBack.setFitWidth(200);
        imageViewStarterBack.setFitHeight(150);
        imageViewStarterBack.setLayoutX(441);
        imageViewStarterBack.setLayoutY(325);
        pane.getChildren().add(imageViewStarterBack);
    }

    @FXML
    void handleStarterBackButton(ActionEvent event) {

    }

    @FXML
    void handleStarterFrontButton(ActionEvent event) {

    }

}
