package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
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
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
        pane.getChildren().add(guiView.headerLabel);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setLayoutX(300);
        gridPane.setLayoutY(500);

        // Add ImageView objects to the GridPane
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

    /**
     * Prints given playable card in GUI at given layout coordinates.
     *
     * @param card
     * @param layoutX
     * @param layoutY
     */
    public ImageView printPhysicalCardFront(PhysicalCard card, int layoutX, int layoutY) {
        String cardCode = card.getCode();
        Image cardImage = new Image(String.valueOf(getClass().getResource("/cards/fronts/" + cardCode + ".jpg")));
        ImageView cardImageView = new ImageView(cardImage);
        Rectangle2D viewport = new Rectangle2D(100, 100, 850, 570);
        cardImageView.setViewport(viewport);
        cardImageView.setFitWidth(150);
        cardImageView.setFitHeight(75);
        cardImageView.setLayoutX(layoutX);
        cardImageView.setLayoutY(layoutY);
        return cardImageView;
    }

    /**
     * Prints given playable card in GUI at given layout coordinates.
     *
     * @param card
     * @param layoutX
     * @param layoutY
     */
    public ImageView printPhysicalCardBack(PhysicalCard card, int layoutX, int layoutY) {
        String cardCode = card.getCode();
        Image cardImage = new Image(String.valueOf(getClass().getResource("/cards/backs/" + cardCode + ".jpg")));
        ImageView cardImageView = new ImageView(cardImage);
        Rectangle2D viewport = new Rectangle2D(100, 100, 850, 570);
        cardImageView.setViewport(viewport);
        cardImageView.setFitWidth(150);
        cardImageView.setFitHeight(75);
        cardImageView.setLayoutX(layoutX);
        cardImageView.setLayoutY(layoutY);
        return cardImageView;
    }

}
