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

public class PlayGameController {

    @FXML
    public AnchorPane pane;

    private GuiView guiView;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
        pane.getChildren().add(guiView.headerLabel);
        for (PhysicalCard physicalCard : guiView.playerHand) {
            pane.getChildren().add(printPhysicalCardFront(physicalCard, 110, 110));
            pane.getChildren().add(printPhysicalCardBack(physicalCard, 110, 110));
        }
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
        return cardImageView;
    }

}
