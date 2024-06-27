package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    @FXML
    private MenuButton sendMenuButton;

    /**
     * method used to initialize wait game controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();

        waitingPlayersBuffer.setVisible(true);

        initializeSendMessageButton();
        updateChat();
    }

    /**
     * method used to handle send message button
     *
     * @param playerName of receiver ()
     */
    private void handleSendMessageButton(String playerName) {
        String submittedSendMessage = chatPromptTextField.getText();
        chatPromptTextField.setText("");
        if (playerName.isEmpty()) {
            guiView.setRead("-c " + submittedSendMessage);
        } else {
            System.out.println(playerName);
            guiView.setRead("-cp " + playerName + " " + submittedSendMessage);
        }
    }

    /**
     * method used to update chat
     */
    public void updateChat() {
        chatListView.setItems(FXCollections.observableArrayList((guiView.getChatMessages())));
    }

    public void initializeSendMessageButton() {
        sendMenuButton.getItems().removeAll();
        MenuItem broadcastSend = new MenuItem("All");
        sendMenuButton.getItems().add(broadcastSend);
        broadcastSend.setOnAction((ActionEvent event) -> {handleSendMessageButton("");});
        for (String gamePlayer : guiView.getGameView().getPlayerList()) {
            MenuItem item = new MenuItem(gamePlayer);
            sendMenuButton.getItems().add(item);
            item.setOnAction(event -> {handleSendMessageButton(item.getText());});
        }
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
