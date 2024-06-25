package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

/**
 * Controller for Wait Game FXML scene.
 */
public class WaitGameController {
    private GuiView guiView;

    @FXML
    private Button leaveGameButton;

    @FXML
    private ProgressIndicator waitingPlayersBuffer;

    @FXML
    private ListView<String> chatListView;

    @FXML
    private TextField chatPromptTextField;

    /**
     * method used to initialize wait game controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();

        waitingPlayersBuffer.setVisible(true);
    }

    /**
     * method used to handle send message button
     * @param event     an instance of action event
     */
    @FXML
    private void handleSendMessageButton(ActionEvent event) {
        String submittedSendMessage = chatPromptTextField.getText();

        guiView.setRead("-c " + submittedSendMessage);

        chatPromptTextField.setText("");
    }

    /**
     * method used to update chat
     */
    public void updateChat() {
        chatListView.setItems(FXCollections.observableArrayList((guiView.getChatMessages())));
    }

    /**
     * method used to handle leave game button
     * @param event an instance of action event
     */
    @FXML
    private void handleLeaveGameButton(ActionEvent event) {
        guiView.setRead("-l");
    }
}
