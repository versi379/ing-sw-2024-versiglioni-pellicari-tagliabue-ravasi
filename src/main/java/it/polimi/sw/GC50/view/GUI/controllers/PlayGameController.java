package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.PlayerDataView;
import javafx.application.Platform;
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

import java.util.ArrayList;
import java.util.List;

public class PlayGameController {

    @FXML
    public AnchorPane pane;

    private GuiView guiView;

    @FXML
    private Button drawCardButton;

    @FXML
    private TextField drawCardTextField;

    @FXML
    private Button placeCardButton;

    @FXML
    private TextField placeCardTextField;

    public GridPane playerHandGrid;
    public GridPane playerAreaGrid;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
        guiView.headerTurnLabel.setLayoutX(0);
        guiView.headerTurnLabel.setLayoutY(0);
        guiView.headerTurnLabel.setPrefWidth(200);
        guiView.headerTurnLabel.setPrefHeight(25);
        pane.getChildren().add(guiView.headerTurnLabel);
        guiView.headerMessageLabel.setLayoutX(0);
        guiView.headerMessageLabel.setLayoutY(100);
        guiView.headerMessageLabel.setPrefWidth(200);
        guiView.headerMessageLabel.setPrefHeight(25);
        pane.getChildren().add(guiView.headerMessageLabel);

        printPlayerHand();

        playerAreaGrid = printPlayerArea(guiView.playerArea);
        pane.getChildren().add(playerAreaGrid);

        guiView.playerAreaUpdated = false;
        guiView.playerHandUpdated = false;
        guiView.headerTurnUpdated = false;
        guiView.headerMessageUpdated = false;
    }

    // NB tutto quello che metto dentro questo metodo viene eseguito dopo che l'utente di fatto
    // termina il proprio turno, visto che pesca una carta (dopo averne piazzata una)
    // quindi tutti i trigger collegati a NOTIFY NECT TURN vanno messi qui
    // e.g. label che cambiano
    @FXML
    void handleDrawCardButton(ActionEvent event) {
        String drawnCardIndex = drawCardTextField.getText();
        guiView.read = drawnCardIndex;
        pane.getChildren().remove(guiView.headerTurnLabel);
        pane.getChildren().remove(guiView.headerMessageLabel);
        while (!guiView.playerHandUpdated || !guiView.headerTurnUpdated || !guiView.headerMessageUpdated) {
            System.out.println("wait");
        }
        System.out.println("qui sotto richiamo la player hand (aggiornata) per stamparla");
        printPlayerHand();
        pane.getChildren().add(guiView.headerTurnLabel);
        pane.getChildren().add(guiView.headerMessageLabel);
        guiView.playerAreaUpdated = false;
        guiView.playerHandUpdated = false;
        guiView.headerTurnUpdated = false;
        guiView.headerMessageUpdated = false;
    }

    @FXML
    void handlePlaceCardButton(ActionEvent event) {
        pane.getChildren().remove(playerAreaGrid);
        // in futuro possiamo mettere pulsanti sopra le 6 carte per ottenere la scelta
        String placedCardInfo = placeCardTextField.getText();
        guiView.read = placedCardInfo;
        // attendo che player area sia updated per ristamparla
        while(!guiView.playerAreaUpdated) {
            System.out.println("aspetta");
        }
        System.out.println("qui sotto richiamo la player area (aggiornata) per stamparla");
        playerAreaGrid = printPlayerArea(guiView.playerArea);
        pane.getChildren().add(playerAreaGrid);
        guiView.playerAreaUpdated = false; // messa una carta rimetto la cosa non aggiornata per i prossimi piazzamenti
    }


    public ImageView printPhysicalCardFront(PhysicalCard card, int layoutX, int layoutY) {
        String cardCode = card.getFront().getCode();
        Image cardImage = new Image(String.valueOf(getClass().getResource("/cards/fronts/" + cardCode + ".jpg")));
        ImageView cardImageView = new ImageView(cardImage);
        Rectangle2D viewport = new Rectangle2D(100, 100, 850, 570);
        cardImageView.setViewport(viewport);
        cardImageView.setFitWidth(80);
        cardImageView.setFitHeight(40);
        cardImageView.setLayoutX(layoutX);
        cardImageView.setLayoutY(layoutY);
        return cardImageView;
    }

    public ImageView printPhysicalCardBack(PhysicalCard card, int layoutX, int layoutY) {
        String cardCode = card.getBack().getCode();
        Image cardImage = new Image(String.valueOf(getClass().getResource("/cards/backs/" + cardCode + ".jpg")));
        ImageView cardImageView = new ImageView(cardImage);
        Rectangle2D viewport = new Rectangle2D(100, 100, 850, 570);
        cardImageView.setViewport(viewport);
        cardImageView.setFitWidth(80);
        cardImageView.setFitHeight(40);
        cardImageView.setLayoutX(layoutX);
        cardImageView.setLayoutY(layoutY);
        return cardImageView;
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
                ImageView cardImageView = printPlayableCard(cardsMatrix.get(actualX, actualY),0,0);

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
        Image cardImage = new Image(String.valueOf(getClass().getResource("/cards/fronts/" + cardCode + ".jpg")));
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
            if(i < 3) { // print front
                gridPane.add(printPhysicalCardFront(guiView.playerHand.get(i),0,0), col, row);
            } else { // print back
                gridPane.add(printPhysicalCardBack(guiView.playerHand.get(i - 3),0,0), col, row);

            }
        }
        pane.getChildren().add(gridPane);
    }

}
