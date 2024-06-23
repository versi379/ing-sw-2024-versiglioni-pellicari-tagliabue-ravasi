package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.PlayerDataView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class PlayGameController {

    private GuiView guiView;

    @FXML
    public AnchorPane pane;

    @FXML
    private Button showBoardButton;

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

    public GridPane playerAreaGrid;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();

        turnLabel.setText("Player \"" + guiView.getCurrentPlayer() + "\" turn");
        printPlayerHand();

        playerAreaGrid = printPlayerArea(guiView.playerArea);
        pane.getChildren().add(playerAreaGrid);

        guiView.playerAreaUpdated = false;
        guiView.playerHandUpdated = false;

        scoresLabel.setText(guiView.scoresText);

        deactivateButton(placeCardButton);
        deactivateButton(drawCardButton);
    }

    @FXML
    void handleShowBoardButton(ActionEvent event) {
        updatePlayerArea();
        guiView.playerHandUpdated = false;
        deactivateButton(showBoardButton);
        activateButton(placeCardButton);
    }

    @FXML
    void handleDrawCardButton(ActionEvent event) {
        String drawnCardIndex = drawCardTextField.getText();
        guiView.setRead(drawnCardIndex);
        System.out.println("PlayerHand Updated: " + guiView.playerHandUpdated);
        System.out.println(("ServerError: " + guiView.serverError));
        while (!guiView.playerHandUpdated && !guiView.serverError) {
            System.out.println("Updating player hand...");
        }
        if (guiView.serverError) {
            guiView.serverError = false;
        } else {
            printPlayerHand();
            deactivateButton(drawCardButton);
            activateButton(showBoardButton);
        }
        guiView.playerHandUpdated = false;
    }

    @FXML
    void handlePlaceCardButton(ActionEvent event) {
        String placedCardInfo = placeCardTextField.getText();
        guiView.setRead(placedCardInfo);
        System.out.println("PlayerArea Updated: " + guiView.playerAreaUpdated);
        System.out.println(("ServerError: " + guiView.serverError));

        while (!guiView.playerAreaUpdated && !guiView.serverError) {
            try {
                System.out.println("Updating player area...");
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        if (guiView.serverError) {
            guiView.serverError = false;
        } else {
            updatePlayerArea();
            deactivateButton(placeCardButton);
            activateButton(drawCardButton);
        }
        guiView.playerAreaUpdated = false;
    }

    public ImageView printPhysicalCardFront(PhysicalCard card, int layoutX, int layoutY) {
        return printPlayableCard(card.getFront(), layoutX, layoutY);
    }

    public ImageView printPhysicalCardBack(PhysicalCard card, int layoutX, int layoutY) {
        return printPlayableCard(card.getBack(), layoutX, layoutY);
    }

    public GridPane printPlayerArea(PlayerDataView playerArea) {
        GridPane grid = new GridPane();
        grid.setLayoutX(300);
        grid.setLayoutY(300);

        CardsMatrix cardsMatrix = playerArea.getCardsMatrix();
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

                grid.add(cardImageView, offsetX, offsetY);
            }
        } else {
            Label noCardsLabel = new Label("No cards placed");
            grid.add(noCardsLabel, 0, 0);
        }

        return grid;
    }

    public ImageView printPlayableCard(PlayableCard card, int layoutX, int layoutY) {
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

    public void printPlayerHand() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setLayoutX(200);
        gridPane.setLayoutY(600);
        for (int i = 0; i < 6; i++) {
            int row = i / 3; // 0 or 1
            int col = i % 3; // 0, 1, or 2
            if (i < 3) { // print front
                gridPane.add(printPhysicalCardFront(guiView.playerHand.get(i), 0, 0), col, row);
            } else { // print back
                gridPane.add(printPhysicalCardBack(guiView.playerHand.get(i - 3), 0, 0), col, row);

            }
        }
        pane.getChildren().add(gridPane);
    }

    public void updatePlayerArea() {
        pane.getChildren().remove(playerAreaGrid);
        playerAreaGrid = printPlayerArea(guiView.playerArea);
        pane.getChildren().add(playerAreaGrid);
        guiView.playerAreaUpdated = false;
        scoresLabel.setText(guiView.scoresText);
    }

    private void activateButton(Button button) {
        button.setDisable(false);
        button.setOpacity(1);
    }

    private void deactivateButton(Button button) {
        button.setDisable(true);
        button.setOpacity(0.3);
    }

    public void updateCurrentPlayer() {
        turnLabel.setText("Player \"" + guiView.getCurrentPlayer() + "\" turn");
    }

    public void updateBoard() {
    }

    public void updateScores() {
    }

    public void updateDecks() {
    }

    public void updateHand() {
    }
}
