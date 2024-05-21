package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;
import trash.PrintBoardTUI2;
import org.junit.jupiter.api.Test;

class PrintBoardTUI2Test {
    @Test
    void testPrintBoard2FullBoard() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20);
        game.addPlayer(player);

        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////");
        PrintBoardTUI2 ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea());
        ob.print();


        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 40, 40);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 41, 41);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 40, 42);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 39, 41);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 39, 39);

        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////");
        ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea());
        ob.print();


        game.placeCard(player, game.pickCard(DrawingPosition.RESOURCEDECK).getFront(), 0, 0);

        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////");
        ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea());
        ob.print();
    }

    @Test
    void testPrintBoard2SelectedArea() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20);
        game.addPlayer(player);

        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////");
        PrintBoardTUI2 ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea(), 40, 40, 82);
        ob.print();


        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 40, 40);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 41, 41);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 40, 42);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 39, 41);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 39, 39);

        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////");
        ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea(), 40, 40, 10);
        ob.print();


        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////");
        ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea(), 39, 41, 2);
        ob.print();


        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////");
        ob = new PrintBoardTUI2(game.getPlayerData(player).getCardsArea(), 0, 0, 2);
        ob.print();
    }
}
