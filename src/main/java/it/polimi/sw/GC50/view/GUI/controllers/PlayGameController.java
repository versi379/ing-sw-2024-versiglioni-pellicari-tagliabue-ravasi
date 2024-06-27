package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Controller for Play Game FXML scene.
 */
public class PlayGameController {

    private GuiView guiView;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button drawCardButton;

    @FXML
    private TextField drawCardTextField;

    @FXML
    private Button placeCardButton;

    @FXML
    private TextField placeCardTextField;

    @FXML
    private Label scoresLabel;

    @FXML
    private Label turnLabel;

    @FXML
    private Label phaseLabel;

    @FXML
    private ScrollPane scrollPane;

    private GridPane handGrid;

    private GridPane deckGrid;

    @FXML
    private ListView<String> chatListView;

    @FXML
    private TextField chatPromptTextField;

    @FXML
    private MenuButton sendMenuButton;

    @FXML
    private Button leaveGameButton;

    /**
     * method used to initialize play game controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();

        printObjectives();
        updateCurrentPlayer();
        updateBoard();
        updateHand();
        updateDecks();
        updateScores();
        initializeSendMessageButton();
        updateChat();
    }

    /**
     * method used to handle place card button
     *
     * @param event an instance of action event
     */
    @FXML
    private void handlePlaceCardButton(ActionEvent event) {
        String submittedPlaceCard = placeCardTextField.getText();
        placeCardTextField.setText("");

        guiView.setRead("-p " + submittedPlaceCard);
    }

    /**
     * method used to handle drew card button
     *
     * @param event an instance of action event
     */
    @FXML
    private void handleDrawCardButton(ActionEvent event) {
        String submittedDrawCard = drawCardTextField.getText();
        drawCardTextField.setText("");

        guiView.setRead("-d " + submittedDrawCard);
    }

    /**
     * method used to handle leave game button
     *
     * @param event an instance of action event
     */
    @FXML
    private void handleLeaveGameButton(ActionEvent event) {
        guiView.setRead("-l");
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
     * method used to update current player
     */
    public void updateCurrentPlayer() {
        turnLabel.setText("Player \"" + guiView.getCurrentPlayer() + "\" turn");
        phaseLabel.setText("");
    }

    /**
     * method used to update placing phase
     */
    public void updatePlacingPhase() {
        phaseLabel.setText("Placing phase");
    }

    /**
     * method used to update drawing phase
     */
    public void updateDrawingPhase() {
        phaseLabel.setText("Drawing phase");
    }

    /**
     * method used to update board
     */
    public void updateBoard() {
        scrollPane.setContent(printCardsArea(guiView.getPlayerArea().getCardsMatrix()));
    }

    private Pane printCardsArea(CardsMatrix cardsMatrix) {
        Pane pane = new Pane();

        int minX = cardsMatrix.getMinX();
        int maxX = cardsMatrix.getMaxX();
        int minY = cardsMatrix.getMinY();
        int maxY = cardsMatrix.getMaxY();

        int targetAreaWidth = maxX - minX + 1;
        int targetAreaHeight = maxY - minY + 1;

        if (targetAreaWidth > 0 && targetAreaHeight > 0) {
            for (Integer coordinates : cardsMatrix.getOrderList()) {
                int actualX = coordinates / cardsMatrix.length();
                int actualY = coordinates % cardsMatrix.length();
                double offsetX = (actualX - minX) * 69 + scrollPane.getWidth() / 2 - 45;
                double offsetY = (maxY - actualY) * 36 + scrollPane.getHeight() / 2 - 30;

                ImageView cardImageView = printCard(cardsMatrix.get(actualX, actualY).getCode(), 1, offsetX, offsetY);

                pane.getChildren().add(cardImageView);
            }
        } else {
            Label noCardsLabel = new Label("No cards placed");
            pane.getChildren().add(noCardsLabel);
        }

        return pane;
    }

    /**
     * method used to update scores
     */
    public void updateScores() {
        scoresLabel.setText(guiView.getScoresPlaying());
    }

    /**
     * method used to update hand
     */
    public void updateHand() {
        pane.getChildren().remove(handGrid);
        handGrid = printHand(guiView.getPlayerHand());
        pane.getChildren().add(handGrid);
    }

    /**
     * method used to print hand
     *
     * @param hand list of physical card
     * @return printed hand
     */
    private GridPane printHand(List<PhysicalCard> hand) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setLayoutX(590);
        gridPane.setLayoutY(50);

        for (int cardsCounter = 0; cardsCounter < hand.size(); cardsCounter++) {

            gridPane.add(printCard(guiView.getPlayerHand().get(cardsCounter).getFront().getCode(), 1, 0, 0),
                    cardsCounter, 0);
            gridPane.add(printCard(guiView.getPlayerHand().get(cardsCounter).getBack().getCode(), 1, 0, 0),
                    cardsCounter, 1);
        }
        return gridPane;
    }

    /**
     * method used to update decks
     */
    public void updateDecks() {
        pane.getChildren().remove(deckGrid);
        deckGrid = printDecks(guiView.getDecks());
        pane.getChildren().add(deckGrid);
    }

    /**
     * method used to print decks
     *
     * @param deck that have to be printed
     * @return deck image
     */
    private GridPane printDecks(PlayableCard[] deck) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setLayoutX(250);
        gridPane.setLayoutY(50);

        for (int cardsCounter = 0; cardsCounter < deck.length; cardsCounter++) {

            if (guiView.getDecks()[cardsCounter] != null) {
                if (cardsCounter < 3) {
                    gridPane.add(printCard(guiView.getDecks()[cardsCounter].getCode(), 1, 0, 0),
                            cardsCounter, 0);
                } else {
                    gridPane.add(printCard(guiView.getDecks()[cardsCounter].getCode(), 1, 0, 0),
                            cardsCounter - 3, 1);
                }
            }
        }
        return gridPane;
    }

    /**
     * method used to print objectives
     */
    private void printObjectives() {
        pane.getChildren().add(printCard(guiView.getSecretObjectiveCode(), 1.25, 50, 275));
        pane.getChildren().add(printCard(guiView.getCommonObjectiveCode(0), 1.25, 50, 350));
        pane.getChildren().add(printCard(guiView.getCommonObjectiveCode(1), 1.25, 50, 425));
    }

    /**
     * method used to print card
     *
     * @param cardCode printed card's code
     * @param size     card's size multiplier
     * @param layoutX  X layout of the card
     * @param layoutY  Y layout of the card
     * @return an image of card
     */
    private ImageView printCard(String cardCode, double size, double layoutX, double layoutY) {
        Image cardImage = new Image(String.valueOf(getClass().getResource("/cards/" + cardCode + ".jpg")));
        ImageView cardImageView = new ImageView(cardImage);
        Rectangle2D viewport = new Rectangle2D(70, 72, 885, 608);
        cardImageView.setViewport(viewport);
        cardImageView.setFitWidth(90 * size);
        cardImageView.setFitHeight(60 * size);
        cardImageView.setLayoutX(layoutX);
        cardImageView.setLayoutY(layoutY);
        return cardImageView;
    }


}
