package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.view.TUI.PrintBoard;

import java.util.ArrayList;

public class b {

    public static void main(String[] args) {
        PlayerData pl= new PlayerData(40);
        Game game2 = new Game("Partita", 1, 20, new Player("Gianpaolo"));

        for (int i = 0; i < 2; i=i+2) {
            for (int j = 1; j < 2; j=j+2) {
                PhysicalCard card;
                card = game2.pickCard(DrawingPosition.GOLDDECK);
                pl.placeCard(card.getBack(),i,j);
            }
        }
        //mat = mat3.getAsCornerMatrixWithoutOrder();
        for (int i = 1; i < 2; i=i+2) {
            for (int j = 2; j < 4; j=j+2) {

                PhysicalCard card;
                card = game2.pickCard(DrawingPosition.GOLDDECK);
                pl.placeCard(card.getBack(),i,j);
            }
        }
        CardsMatrix mat3= pl.getCardsArea();
        PrintBoard ob= new PrintBoard(mat3);
    }

}



