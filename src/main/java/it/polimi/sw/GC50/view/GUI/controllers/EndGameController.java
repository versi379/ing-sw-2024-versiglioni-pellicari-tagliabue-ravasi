package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
    private ListView<String> chatListView;

    @FXML
    private TextField chatPromptTextField;

    @FXML
    private Button sendMessageButton;

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

        scoresLabel.setText(guiView.getScoresEnd());
    }

    @FXML
    private void handleSendMessageButton(ActionEvent event) {
        String submittedSendMessage = chatPromptTextField.getText();
        chatPromptTextField.setText("");

        guiView.setRead("-c " + submittedSendMessage);
    }

    @FXML
    private void handleLeaveGameButton(ActionEvent event) {
        guiView.setRead("-l");
    }

    public void updateChat() {
        chatListView.setItems(FXCollections.observableArrayList((guiView.getChatMessages())));
    }
}
