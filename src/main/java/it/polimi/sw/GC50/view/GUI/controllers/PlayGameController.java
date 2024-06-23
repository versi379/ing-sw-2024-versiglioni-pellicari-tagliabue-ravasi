package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.PlayerDataView;
import it.polimi.sw.GC50.view.TUI.TuiModelPrinter;
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

    private GridPane playerAreaGrid;

    private GridPane handGrid;

    @FXML
    private ListView chatListView;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();

        turnLabel.setText("Player \"" + guiView.getCurrentPlayer() + "\" turn");
        updateBoard();
        updateHand();
        scoresLabel.setText(guiView.getScoresText());

        /*
        activateButton(placeCardButton);
        activateButton(drawCardButton);

         */
    }

    @FXML
    private void handlePlaceCardButton(ActionEvent event) {
        String submittedPlaceCard = placeCardTextField.getText();

        guiView.setRead("-p " + submittedPlaceCard);

        /*
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

         */
    }

    @FXML
    private void handleDrawCardButton(ActionEvent event) {
        String submittedDrawCard = drawCardTextField.getText();

        guiView.setRead("-d " + submittedDrawCard);

        /*
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

         */
    }

    public void updateCurrentPlayer() {
        turnLabel.setText("Player \"" + guiView.getCurrentPlayer() + "\" turn");
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
        scoresLabel.setText(guiView.getScoresText());
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
        gridPane.setLayoutX(200);
        gridPane.setLayoutY(600);

        for (int cardsCounter = 0; cardsCounter < hand.size(); cardsCounter++) {

            gridPane.add(printPlayableCard(guiView.getPlayerHand().get(cardsCounter).getFront(), 0, 0),
                    cardsCounter, 0);
            gridPane.add(printPlayableCard(guiView.getPlayerHand().get(cardsCounter).getBack(), 0, 0),
                    cardsCounter, 1);
        }
        return gridPane;
    }

    public void updateDecks() {
    }

    public void updateChat() {

    }

    private void activateButton(Button button) {
        button.setDisable(false);
        button.setOpacity(1);
    }

    private void deactivateButton(Button button) {
        button.setDisable(true);
        button.setOpacity(0.3);
    }
}
