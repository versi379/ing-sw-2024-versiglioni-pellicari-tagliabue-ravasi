package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.view.TUI.PrintBoard;
import org.junit.jupiter.api.Test;

public class PrintTUITest {

    @Test
    void testPrintBoard() {
        PlayerData pl = new PlayerData(40);
        Game game2 = new Game("Partita", 1, 20, new Player("Gianpaolo"));

        for (int i = 0; i < 2; i = i + 2) {
            for (int j = 0; j < 2; j = j + 2) {
                PhysicalCard card;
                card = game2.pickCard(DrawingPosition.GOLDDECK);
                pl.placeCard(card.getFront(), i, j);
            }
        }
        //mat = mat3.getAsCornerMatrixWithoutOrder();
        for (int i = 1; i < 2; i = i + 2) {
            for (int j = 1; j < 4; j = j + 2) {

                PhysicalCard card;
                card = game2.pickCard(DrawingPosition.GOLDDECK);
                pl.placeCard(card.getFront(), i, j);
            }
        }
        CardsMatrix mat3 = pl.getCardsArea();
        PrintBoard ob = new PrintBoard(mat3);
    }

    @Test
    void testToStringTUI() {
        Game game = new Game("Partita", 1, 20, new Player("Player"));
        while (game.goldDeckSize() > 0) {
            String[][] card = game.pickCard(DrawingPosition.GOLDDECK).getFront().toStringTUI();
            for (int i = 6; i >= 0; i--) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(card[j][i]);
                }
                System.out.println();
            }
        }
        for (int k = 0; k < 5; k++) {
            String[][] card = game.pickStarterCard().getFront().toStringTUI();
            for (int i = 6; i >= 0; i--) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(card[j][i]);
                }
                System.out.println();
            }
        }
    }
}
