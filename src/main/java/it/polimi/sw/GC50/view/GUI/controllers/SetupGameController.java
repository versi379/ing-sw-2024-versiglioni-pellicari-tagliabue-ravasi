package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * Controller for Setup  Game FXML scene.
 */
public class SetupGameController {
    private GuiView guiView;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button starterFrontButton;

    @FXML
    private ImageView imageViewStarterFront;

    @FXML
    private Button starterBackButton;

    @FXML
    private ImageView imageViewStarterBack;

    @FXML
    private Button chooseObjective1Button;

    @FXML
    private ImageView imageViewObjective1;

    @FXML
    private Button chooseObjective2Button;

    @FXML
    private ImageView imageViewObjective2;

    private GridPane commonObjectivesGrid;

    @FXML
    private ListView<String> chatListView;

    @FXML
    private TextField chatPromptTextField;

    @FXML
    private Button sendMessageButton;

    @FXML
    private Button leaveGameButton;

    /**
     * method used to initialize setup game controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();

        Rectangle2D viewport = new Rectangle2D(100, 100, 850, 570);
        Image starterCardFront = new Image(String.valueOf(getClass().getResource("/cards/" + guiView.getStarterCardFrontCode() + ".jpg")));
        imageViewStarterFront.setImage(starterCardFront);
        imageViewStarterFront.setViewport(viewport);
        Image starterCardBack = new Image(String.valueOf(getClass().getResource("/cards/" + guiView.getStarterCardBackCode() + ".jpg")));
        imageViewStarterBack.setImage(starterCardBack);
        imageViewStarterBack.setViewport(viewport);

        Image secretObjective1 = new Image(String.valueOf(getClass().getResource("/cards/" + guiView.getSecretObjectiveCode(0) + ".jpg")));
        imageViewObjective1.setImage(secretObjective1);
        imageViewObjective1.setViewport(viewport);
        Image secretObjective2 = new Image(String.valueOf(getClass().getResource("/cards/" + guiView.getSecretObjectiveCode(1) + ".jpg")));
        imageViewObjective2.setImage(secretObjective2);
        imageViewObjective2.setViewport(viewport);

        Image commonObjective1 = new Image(String.valueOf(getClass().getResource("/cards/" + guiView.getCommonObjectiveCode(0) + ".jpg")));
        Image commonObjective2 = new Image(String.valueOf(getClass().getResource("/cards/" + guiView.getCommonObjectiveCode(1) + ".jpg")));

        updateChat();
    }

    /**
     * method used to handle starter front button
     *
     * @param event an instance of action event
     */
    @FXML
    private void handleStarterFrontButton(ActionEvent event) {
        imageViewStarterBack.setOpacity(1);
        imageViewStarterFront.setOpacity(0.3);
        guiView.setRead("-cs 1");
    }

    /**
     * method used to handle starter back button
     *
     * @param event an instance of action event
     */
    @FXML
    private void handleStarterBackButton(ActionEvent event) {
        imageViewStarterFront.setOpacity(1);
        imageViewStarterBack.setOpacity(0.3);
        guiView.setRead("-cs 2");
    }

    /**
     * method used to handle choose objective 1 button
     *
     * @param event an instance of action event
     */
    @FXML
    private void handleChooseObjective1Button(ActionEvent event) {
        imageViewObjective2.setOpacity(1);
        imageViewObjective1.setOpacity(0.3);
        guiView.setRead("-co 1");
    }

    /**
     * method used to handle choose objective 1 button
     *
     * @param event an instance of action event
     */
    @FXML
    private void handleChooseObjective2Button(ActionEvent event) {
        imageViewObjective1.setOpacity(1);
        imageViewObjective2.setOpacity(0.3);
        guiView.setRead("-co 2");
    }

    /**
     * method used to handle send message button
     * @param event an instance of action event
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
