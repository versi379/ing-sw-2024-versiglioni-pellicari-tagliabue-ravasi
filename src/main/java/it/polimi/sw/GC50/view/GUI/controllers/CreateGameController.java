package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

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
    public void initialize() {
        numPlayersGroup = new ToggleGroup();
        twoPlayersButton.setToggleGroup(numPlayersGroup);
        threePlayersButton.setToggleGroup(numPlayersGroup);
        fourPlayersButton.setToggleGroup(numPlayersGroup);
        guiView = (GuiView) AppClient.getView();
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
        // prima di entrare nella game view attendo

        while(waitingPlayers) {
            System.out.println("attendo giocatori");
        }
        // finito waiting entro

        showGameView();
    }

    public void showGameView() throws Exception {
        Stage stage = (Stage) createGameButton.getScene().getWindow();
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource(ScenePath.GAME.getPath()));
        Parent gameRoot = gameLoader.load();
        Scene gameScene = new Scene(gameRoot);
        gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(gameScene);
    }

}

