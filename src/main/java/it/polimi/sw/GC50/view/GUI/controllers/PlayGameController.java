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

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();

        printObjectives();
        updateCurrentPlayer();
        updateBoard();
        updateHand();
        updateDecks();
        updateScores();
        updateChat();
    }

    @FXML
    private void handlePlaceCardButton(ActionEvent event) {
        String submittedPlaceCard = placeCardTextField.getText();
        placeCardTextField.setText("");

        guiView.setRead("-p " + submittedPlaceCard);
    }

    @FXML
    private void handleDrawCardButton(ActionEvent event) {
        String submittedDrawCard = drawCardTextField.getText();
        drawCardTextField.setText("");

        guiView.setRead("-d " + submittedDrawCard);
    }

    @FXML
    private void handleLeaveGameButton(ActionEvent event) {
        guiView.setRead("-l");
    }

    @FXML
    private void handleSendMessageButton(ActionEvent event) {
        String submittedSendMessage = chatPromptTextField.getText();
        chatPromptTextField.setText("");

        guiView.setRead("-c " + submittedSendMessage);
    }

    public void updateCurrentPlayer() {
        turnLabel.setText("Player \"" + guiView.getCurrentPlayer() + "\" turn");
        phaseLabel.setText("");
    }

    public void updatePlacingPhase() {
        phaseLabel.setText("Placing phase");
    }

    public void updateDrawingPhase() {
        phaseLabel.setText("Drawing phase");
    }

    public void updateBoard() {
        pane.getChildren().remove(playerAreaGrid);
        playerAreaGrid = printPlayerArea(guiView.getPlayerArea().getCardsMatrix());
        pane.getChildren().add(playerAreaGrid);
    }

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

    public void updateScores() {
        scoresLabel.setText(guiView.getScoresPlaying());
    }

    public void updateHand() {
        pane.getChildren().remove(handGrid);
        handGrid = printHand(guiView.getPlayerHand());
        pane.getChildren().add(handGrid);
    }

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

    public void updateDecks() {
        pane.getChildren().remove(deckGrid);
        deckGrid = printDecks(guiView.getDecks());
        pane.getChildren().add(deckGrid);
    }

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

    private void printObjectives() {
        if (guiView.getSubmittedSetupObjective() == 1) {
            printStarter(guiView.getSecretObjectiveCode(0), 50, 300);
        } else {
            printStarter(guiView.getSecretObjectiveCode(1), 50, 300);
        }
        printStarter(guiView.getCommonObjectiveCode(0), 50, 360);
        printStarter(guiView.getCommonObjectiveCode(1), 50, 420);
    }

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

    public void updateChat() {
        chatListView.setItems(FXCollections.observableArrayList((guiView.getChatMessages())));
    }
}
