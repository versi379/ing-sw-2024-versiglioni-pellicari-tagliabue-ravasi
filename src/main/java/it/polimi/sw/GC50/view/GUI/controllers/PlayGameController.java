package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
    private Button placeCardButton;

    private String placeCardIndexes = "";

    private String placeCardPosition = "";

    @FXML
    private Button drawCardButton;

    private String drawCardPosition = "";

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
        updateScores();
        updateChat();
    }

    /**
     * method used to handle place card button
     *
     * @param event an instance of action event
     */
    @FXML
    private void handlePlaceCardButton(ActionEvent event) {
        guiView.setRead("-p " + placeCardIndexes + " " + placeCardPosition);
        placeCardIndexes = "";
        placeCardPosition = "";
    }

    /**
     * method used to handle drew card button
     *
     * @param event an instance of action event
     */
    @FXML
    private void handleDrawCardButton(ActionEvent event) {
        guiView.setRead("-d " + drawCardPosition);
        drawCardPosition = "";
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

        pane.setLayoutX(targetAreaWidth * 69);
        pane.setLayoutY(targetAreaHeight * 36);

        if (targetAreaWidth > 0 && targetAreaHeight > 0) {
            for (Integer coordinates : cardsMatrix.getOrderList()) {
                int actualX = coordinates / cardsMatrix.length();
                int actualY = coordinates % cardsMatrix.length();
                double offsetX = (actualX - minX - (double) (maxX - minX + 1) / 2) * 69 + 21 + scrollPane.getWidth() / 2 - 45;
                double offsetY = (maxY - actualY - (double) (maxY - minY + 1) / 2) * 36 + 24 + scrollPane.getHeight() / 2 - 30;

                ImageView cardImageView = printCard(cardsMatrix.get(actualX, actualY).getCode(), 1, offsetX, offsetY);

                pane.getChildren().add(cardImageView);
            }
            for (int x = minX - 1; x <= maxX + 1; x++) {
                for (int y = minY - 1; y <= maxY + 1; y++) {
                    if ((x + y) % 2 == 0 && cardsMatrix.get(x, y) == null) {
                        Button button = new Button();
                        button.setLayoutX((x - minX - (double) (maxX - minX + 1) / 2) * 69 + 21 + scrollPane.getWidth() / 2 - 30);
                        button.setPrefWidth(60);
                        button.setLayoutY((maxY - y - (double) (maxY - minY + 1) / 2) * 36 + 24 + scrollPane.getHeight() / 2 - 20);
                        button.setPrefHeight(40);
                        button.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                placeCardPosition = (int)
                                        (((button.getLayoutX() - scrollPane.getWidth() / 2 + 30 - 21) / 69) +
                                                minX + (double) (maxX - minX + 1) / 2 + 1) +
                                        " " +
                                        (int)
                                                ((-(button.getLayoutY() - scrollPane.getHeight() / 2 + 20 - 24) / 36) +
                                                        maxY - (double) (maxY - minY + 1) / 2 + 1);
                            }
                        });
                        pane.getChildren().add(button);
                    }
                }
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

            Button buttonFront = new Button();
            buttonFront.setPrefWidth(90);
            buttonFront.setPrefHeight(60);
            buttonFront.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    placeCardIndexes = (int) (buttonFront.getLayoutX() / 100 + 1) + " 1";
                }
            });
            gridPane.add(buttonFront, cardsCounter, 0);

            Button buttonBack = new Button();
            buttonBack.setPrefWidth(90);
            buttonBack.setPrefHeight(60);
            buttonBack.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    placeCardIndexes = (int) (buttonBack.getLayoutX() / 70 + 1) + " 2";
                }
            });
            gridPane.add(buttonBack, cardsCounter, 1);
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

                    Button button = new Button();
                    button.setPrefWidth(90);
                    button.setPrefHeight(60);
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            drawCardPosition = String.valueOf((int) (button.getLayoutX() / 100 + 1));
                        }
                    });
                    gridPane.add(button, cardsCounter, 0);

                } else {
                    gridPane.add(printCard(guiView.getDecks()[cardsCounter].getCode(), 1, 0, 0),
                            cardsCounter - 3, 1);

                    Button button = new Button();
                    button.setPrefWidth(90);
                    button.setPrefHeight(60);
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            drawCardPosition = String.valueOf((int) (button.getLayoutX() / 100 + 3 + 1));
                        }
                    });
                    gridPane.add(button, cardsCounter - 3, 1);
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

    /**
     * method used to update chat
     */
    public void updateChat() {
        chatListView.setItems(FXCollections.observableArrayList((guiView.getChatMessages())));
    }
}