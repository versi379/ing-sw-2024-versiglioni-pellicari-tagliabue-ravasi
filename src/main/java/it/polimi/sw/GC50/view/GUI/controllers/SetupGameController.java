package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for Game FXML scene.
 */
public class SetupGameController {

    @FXML
    public AnchorPane pane;

    private GuiView guiView;

    @FXML
    private Label commonObjectivesLabel;

    @FXML
    private Label secretObjectivesLabel;

    @FXML
    private Button starterFrontButton;
    @FXML
    private Button starterBackButton;

    @FXML
    private ImageView imageViewStarterBack;

    @FXML
    private ImageView imageViewStarterFront;

    @FXML
    private Button chooseObjective1Button;
    @FXML
    private Button chooseObjective2Button;

    /**
     * method used to initialize setup game controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
        commonObjectivesLabel.setText(guiView.setupCommonObjectives);
        secretObjectivesLabel.setText(guiView.setupSecretObjectives);
        Rectangle2D viewport = new Rectangle2D(100, 100, 850, 570);
        Image starterCardFront = new Image(String.valueOf(getClass().getResource("/cards/" + guiView.starterCardFrontCode + ".jpg")));
        imageViewStarterFront.setImage(starterCardFront);
        imageViewStarterFront.setViewport(viewport);
        Image starterCardBack = new Image(String.valueOf(getClass().getResource("/cards/" + guiView.starterCardBackCode + ".jpg")));
        imageViewStarterBack.setImage(starterCardBack);
        imageViewStarterBack.setViewport(viewport);
    }

    // add  COMMAND listeners
    // associate GUI action to command code VVV

    /**
     * method used to handle starter front button
     * @param event an instance of action event
     */
    @FXML
    private void handleStarterFrontButton(ActionEvent event) {
        guiView.setRead("-cs 1");
        starterBackButton.setDisable(true);
        imageViewStarterBack.setOpacity(0.3);
    }
    /**
     * method used to handle starter back button
     * @param event an instance of action event
     */
    @FXML
    private void handleStarterBackButton(ActionEvent event) {
        guiView.setRead("-cs 2");
        starterFrontButton.setDisable(true);
        imageViewStarterFront.setOpacity(0.3);
    }

    /**
     * method used to handle choose objective 1 button
     * @param event an instance of action event
     */
    @FXML
    private void handleChooseObjective1Button(ActionEvent event) {
        guiView.setRead("-co 1");
        chooseObjective2Button.setDisable(true);
    }
    /**
     * method used to handle choose objective 1 button
     * @param event an instance of action event
     */
    @FXML
    private void handleChooseObjective2Button(ActionEvent event) {
        guiView.setRead("-co 2");
        chooseObjective1Button.setDisable(true);
    }
}
