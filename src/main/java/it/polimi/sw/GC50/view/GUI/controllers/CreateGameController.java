package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.function.UnaryOperator;

public class CreateGameController {

    @FXML
    private TextField finalScore;

    @FXML
    private TextField gameName;

    @FXML
    private RadioButton twoPlayersButton;
    @FXML
    private RadioButton threePlayersButton;
    @FXML
    private RadioButton fourPlayersButton;

    @FXML
    private Button createGameButton;

    private ToggleGroup numPlayersGroup;

    private GuiView guiView;

    public boolean waitingPlayers = true;

    @FXML
    private ProgressIndicator waitingPlayersBuffer;

    @FXML
    public void initialize() {
        numPlayersGroup = new ToggleGroup();
        twoPlayersButton.setToggleGroup(numPlayersGroup);
        threePlayersButton.setToggleGroup(numPlayersGroup);
        fourPlayersButton.setToggleGroup(numPlayersGroup);
        guiView = (GuiView) AppClient.getView();

        // Create a TextFormatter that allows only integer input
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) { // Allows negative numbers and integers, disallow zero at start
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(integerFilter);
        finalScore.setTextFormatter(textFormatter);
    }

    @FXML
    public void handleCreateGameButton(ActionEvent event) throws Exception {
        RadioButton selectedRadioButton = (RadioButton) numPlayersGroup.getSelectedToggle();
        int submittedNumPlayers = 2;
        if (selectedRadioButton != null) {
            if (selectedRadioButton == twoPlayersButton) {
                submittedNumPlayers = 2;
            } else if (selectedRadioButton == threePlayersButton) {
                submittedNumPlayers = 3;
            } else {
                submittedNumPlayers = 4;
            }
        } else {
            // tell user he must choose num players
        }
        String submittedGameName = gameName.getText();
        int submittedEndPoints = Integer.parseInt(finalScore.getText());
        guiView.setSubmittedGameName(submittedGameName);
        guiView.setSubmittedNumPlayers(submittedNumPlayers);
        guiView.setSubmittedEndPoints(submittedEndPoints);
        guiView.resumeExecution();
        createGameButton.setVisible(false);
        waitingPlayersBuffer.setVisible(true);
    }

}

