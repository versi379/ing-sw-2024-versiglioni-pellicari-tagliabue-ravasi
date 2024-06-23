package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.function.UnaryOperator;

/**
 * Controller for Create game FXML scene.
 */
public class CreateGameController {
    private GuiView guiView;

    @FXML
    private TextField gameName;

    private ToggleGroup numPlayersGroup;

    @FXML
    private RadioButton twoPlayersButton;

    @FXML
    private RadioButton threePlayersButton;

    @FXML
    private RadioButton fourPlayersButton;

    @FXML
    private TextField finalScore;

    @FXML
    private Button createGameButton;

    @FXML
    private ProgressIndicator waitingPlayersBuffer;

    /**
     * method that initialize the game controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();

        numPlayersGroup = new ToggleGroup();
        twoPlayersButton.setToggleGroup(numPlayersGroup);
        threePlayersButton.setToggleGroup(numPlayersGroup);
        fourPlayersButton.setToggleGroup(numPlayersGroup);

        // Create a TextFormatter that allows only integer input
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([0-9]+)?")) { // Allows negative numbers and integers
                return change;
            }
            return null;
        };
        finalScore.setTextFormatter(new TextFormatter<>(integerFilter));
    }

    /**
     * method that create game button
     * @param event     type of event
     */
    @FXML
    private void handleCreateGameButton(ActionEvent event) {
        String submittedGameName = gameName.getText();
        RadioButton selectedRadioButton = (RadioButton) numPlayersGroup.getSelectedToggle();
        int submittedNumPlayers = 2;
        if (selectedRadioButton == threePlayersButton) {
            submittedNumPlayers = 3;
        } else if (selectedRadioButton == fourPlayersButton) {
            submittedNumPlayers = 4;
        }
        int submittedEndPoints = Math.min(Integer.parseInt(finalScore.getText()), 50);

        guiView.setSubmittedGameName(submittedGameName);
        guiView.setSubmittedNumPlayers(submittedNumPlayers);
        guiView.setSubmittedEndPoints(submittedEndPoints);
        guiView.resumeExecution();
    }

    /**
     * method that shows waiting buffer
     */
    public void showWaitingBuffer() {
        createGameButton.setVisible(false);
        waitingPlayersBuffer.setVisible(true);
    }
}
