package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for Game FXML scene.
 */
public class EndGameController {

    private GuiView guiView;

    @FXML
    private Label resultsLabel;

    @FXML
    private Label scoresLabel;

    @FXML
    private Button leaveGameButton;

    /**
     * method used to initialize wait game controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();

        StringBuilder results = new StringBuilder();
        if (guiView.getGameView().getWinnerList().size() == 1) {
            results.append("Winner -> \"").append(guiView.getGameView().getWinnerList().getFirst()).append("\"");
        } else {
            results.append("Winners ->");
            for (String nickname : guiView.getGameView().getWinnerList()) {
                results.append(" \"" + nickname + "\"");
            }
            results.append("\n");
        }

        resultsLabel.setText("GAME ENDED\n" + results);

        scoresLabel.setText(guiView.getScoresText());
    }

    @FXML
    private void handleLeaveGameButton(ActionEvent event) {
        guiView.setRead("-l");
    }
}
