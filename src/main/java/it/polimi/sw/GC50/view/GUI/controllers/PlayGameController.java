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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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

    private GridPane playerAreaGrid;

    private GridPane handGrid;

    private GridPane deckGrid;

    @FXML
    private ListView<String> chatListView;

    @FXML
    private TextField chatPromptTextField;

    @FXML
    private Button sendMessageButton;

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
        scoresLabel.setText(guiView.getScoresPlaying());

        updateChat();
    }

    /**
     * method used to handle place card button
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
     * @param event an instance of action event
     */
    @FXML
    private void handleLeaveGameButton(ActionEvent event) {
        guiView.setRead("-l");
    }
    /**
     * method used to handle send message button
     * @param event an instance of action event
     */
    @FXML
    private void handleSendMessageButton(ActionEvent event) {
        String submittedSendMessage = chatPromptTextField.getText();
        chatPromptTextField.setText("");

        guiView.setRead("-c " + submittedSendMessage);
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
        pane.getChildren().remove(playerAreaGrid);
        playerAreaGrid = printPlayerArea(guiView.getPlayerArea().getCardsMatrix());
        pane.getChildren().add(playerAreaGrid);
    }

    /**
     * method used to print player area
     * @param cardsMatrix a copy of card matrix
     * @return player area
     */
    private GridPane printPlayerArea(CardsMatrix cardsMatrix) {
        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(300);
        gridPane.setLayoutY(300);

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
                ImageView cardImageView = printPlayableCard(cardsMatrix.get(actualX, actualY), 0, 0);

                int offsetX = actualX - minX;
                int offsetY = 800 - (actualY - minY);

                gridPane.add(cardImageView, offsetX, offsetY);
            }
        } else {
            Label noCardsLabel = new Label("No cards placed");
            gridPane.add(noCardsLabel, 0, 0);
        }

        return gridPane;
    }

    /**
     * method used to print playable card
     * @param card      printed card
     * @param layoutX   X layout of the card
     * @param layoutY   Y layout of the card
     * @return an image of card
     */
    private ImageView printPlayableCard(PlayableCard card, int layoutX, int layoutY) {
        String cardCode = card.getCode();
        Image cardImage = new Image(String.valueOf(getClass().getResource("/cards/" + cardCode + ".jpg")));
        ImageView cardImageView = new ImageView(cardImage);
        Rectangle2D viewport = new Rectangle2D(100, 100, 850, 570);
        cardImageView.setViewport(viewport);
        cardImageView.setFitWidth(80);
        cardImageView.setFitHeight(40);
        cardImageView.setLayoutX(layoutX);
        cardImageView.setLayoutY(layoutY);
        return cardImageView;
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
     * @param hand  list of physical card
     * @return  printed hand
     */
    private GridPane printHand(List<PhysicalCard> hand) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setLayoutX(400);
        gridPane.setLayoutY(650);

        for (int cardsCounter = 0; cardsCounter < hand.size(); cardsCounter++) {

            gridPane.add(printPlayableCard(guiView.getPlayerHand().get(cardsCounter).getFront(), 0, 0),
                    cardsCounter, 0);
            gridPane.add(printPlayableCard(guiView.getPlayerHand().get(cardsCounter).getBack(), 0, 0),
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
     * @param deck  that have to be printed
     * @return  deck image
     */
    private GridPane printDecks(PlayableCard[] deck) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setLayoutX(400);
        gridPane.setLayoutY(50);

        for (int cardsCounter = 0; cardsCounter < deck.length; cardsCounter++) {

            if (cardsCounter < 3) {
                gridPane.add(printPlayableCard(guiView.getDecks()[cardsCounter], 0, 0),
                        cardsCounter, 0);
            } else {
                gridPane.add(printPlayableCard(guiView.getDecks()[cardsCounter], 0, 0),
                        cardsCounter - 3, 1);
            }
        }
        return gridPane;
    }

    /**
     * method used to print objectives
     */
    private void printObjectives() {
        if (guiView.getSubmittedSetupObjective() == 1) {
            printStarter(guiView.getSecretObjectiveCode(0), 50, 300);
        } else {
            printStarter(guiView.getSecretObjectiveCode(1), 50, 300);
        }
        printStarter(guiView.getCommonObjectiveCode(0), 50, 360);
        printStarter(guiView.getCommonObjectiveCode(1), 50, 420);
    }

    /**
     * method used to print starter card
     * @param cardCode  code of the card
     * @param layoutX   X layout of the card
     * @param layoutY   Y layout of the card
     */
    private void printStarter(String cardCode, int layoutX, int layoutY) {
        Image cardImage = new Image(String.valueOf(getClass().getResource("/cards/" + cardCode + ".jpg")));
        ImageView cardImageView = new ImageView(cardImage);
        Rectangle2D viewport = new Rectangle2D(100, 100, 850, 570);
        cardImageView.setViewport(viewport);
        cardImageView.setFitWidth(90);
        cardImageView.setFitHeight(50);
        cardImageView.setLayoutX(layoutX);
        cardImageView.setLayoutY(layoutY);
        pane.getChildren().add(cardImageView);
    }

    /**
     * method used to update chat
     */
    public void updateChat() {
        chatListView.setItems(FXCollections.observableArrayList((guiView.getChatMessages())));
    }
}
