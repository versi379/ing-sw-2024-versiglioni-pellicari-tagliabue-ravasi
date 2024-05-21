package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;
import trash.PrintBoardTUI2;
import org.junit.jupiter.api.Test;

public class PrintTUITest {

    @Test
    void testPrintBoard2() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20);
        game.addPlayer(player);

        PrintBoardTUI2 ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea());
        ob.print();


        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 40, 40);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 41, 41);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 40, 42);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 39, 41);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 39, 39);

        ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea());
        ob.print();


        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        game.placeCard(player, game.pickCard(DrawingPosition.RESOURCEDECK).getFront(), 0, 0);

        ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea());
        ob.print();


        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea(), 40, 40, 2);
        ob.print();
    }

    @Test
    void testToStringTUI() {
        Game game = new Game("Partita", 1, 20);
        game.addPlayer(new Player("Player"));
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
            for (int i = card[0].length - 1; i >= 0; i--) {
                for (int j = 0; j < card.length; j++) {
                    System.out.print(card[j][i]);
                }
                System.out.println();
            }
        }
    }
}
