package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.function.UnaryOperator;

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
            if (newText.matches("-?([0-9]+)?")) { // Allows negative numbers and integers
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(integerFilter);
        finalScore.setTextFormatter(textFormatter);
    }

    @FXML
    public void handleCreateGameButton(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) numPlayersGroup.getSelectedToggle();
        int submittedNumPlayers = 2;
        if (selectedRadioButton == threePlayersButton) {
            submittedNumPlayers = 3;
        } else if (selectedRadioButton == fourPlayersButton) {
            submittedNumPlayers = 4;
        }

        String submittedGameName = gameName.getText();
        int submittedEndPoints = Integer.parseInt(finalScore.getText());
        guiView.setSubmittedGameName(submittedGameName);
        guiView.setSubmittedNumPlayers(submittedNumPlayers);
        guiView.setSubmittedEndPoints(submittedEndPoints);
        guiView.resumeExecution();
    }

    public void showWaitingBuffer() {
        createGameButton.setVisible(false);
        waitingPlayersBuffer.setVisible(true);
    }
}
