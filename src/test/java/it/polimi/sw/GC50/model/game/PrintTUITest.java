package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.view.TUI.PrintBoard;
import org.junit.jupiter.api.Test;

public class PrintTUITest {

    @Test
    void testPrintBoard() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);

        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 40, 40);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 41, 41);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 40, 42);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 39, 41);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 39, 39);

        PrintBoard ob = new PrintBoard(game.getPlayerData(player).getCardsArea());

        ob.print();
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
