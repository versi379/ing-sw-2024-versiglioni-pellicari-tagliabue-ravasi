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
    private Button showHand;

    @FXML
    void handleShowHand(ActionEvent event) {
        System.out.println(guiView.playerHand.get(0).getFront().getCode());
        for(PhysicalCard physicalCard : guiView.playerHand) {
            pane.getChildren().add(showPlayableCard(physicalCard.getFront(), 110, 110));
        }
    }

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
        pane.getChildren().add(guiView.headerLabel);
        System.out.println(guiView.getClientRmi().getGameView().getHand().get(0).getFront().getCode());
        //pane.getChildren().add(guiView.headerLabel2);
    }

    /**
     * Prints given playable card in GUI at given layout coordinates.
     * @param card
     * @param layoutX
     * @param layoutY
     */
    public ImageView showPlayableCard(PlayableCard card, int layoutX, int layoutY) {
        String cardCode = card.getCode();
        Image cardImage = new Image(String.valueOf(getClass().getResource("/cards/fronts/" + "81" + ".jpg")));
        ImageView cardImageView = new ImageView(cardImage);
        return cardImageView;
    }

}
