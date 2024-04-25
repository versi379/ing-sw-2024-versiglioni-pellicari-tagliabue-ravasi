package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.model.card.Corner;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.game.*;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import org.junit.jupiter.api.Test;

/**
 * !!!!!!!!! CLASSE PROVVISORIA GIUSTO PER AVERE SOTTO MANO QUESTI TEST !!!!!!!!!
 */
public class GameControllerTest {
    @Test
    public void testCardsVisualization() {
        Player player = new Player("Francesco");
        Game game = new Game("Partita", 1, 20, player);

        for (int i = 0; i < game.resourceDeckSize(); i++) {
            System.out.println(i);
            PhysicalCard card = game.pickCard(DrawingPosition.RESOURCEDECK);
            printPhysicalCard(card);
        }
        for (int i = 0; i < game.goldDeckSize(); i++) {
            System.out.println(i);
            PhysicalCard card = game.pickCard(DrawingPosition.GOLDDECK);
            printPhysicalCard(card);
        }
        game.getObjectives(20).stream()
                .map(ObjectiveCard::getPointsPerCompletion)
                .forEach(System.out::println);
    }

    @Test
    public void testCardsPlacement() {
        Player player = new Player("Francesco");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);

        PlayerData board = game.getPlayerData(player);

        for (PhysicalCard card : board.getHand()) {
            printPhysicalCard(card);
        }
        board.printCornersArea();
        board.getCardsArea().printCardsArea();

        controller.chooseStarterFace(player, true);
        controller.chooseObjective(player, 0);

        System.out.println("\nLISTA GIOCATORI:");
        game.getPlayerList().forEach(System.out::println);

        System.out.println("\nPIAZZA CARTA 1");
        controller.placeCard(player, 1, true, 41, 41);
        board.printCornersArea();
        board.getCardsArea().printCardsArea();

        System.out.println("\nPESCA CARTA");
        controller.drawCard(player, DrawingPosition.RESOURCE1);
        for (PhysicalCard card : board.getHand()) {
            printPhysicalCard(card);
        }

        System.out.println("\nPIAZZA CARTA 2");
        controller.placeCard(player, 2, true, 42, 40);
        board.printCornersArea();
        board.getCardsArea().printCardsArea();
    }


    @Test
    public void testMultiplayer() {
        Player player1 = new Player("Francesco");
        Game game = new Game("Partita", 2, 20, player1);
        GameController controller = new GameController(game);

        Player player2 = new Player("Pietro");
        controller.addPlayer(player2);

        PlayerData board1 = game.getPlayerData(player1);
        PlayerData board2 = game.getPlayerData(player2);

        System.out.println("\nLISTA GIOCATORI:");
        game.getPlayerList().forEach(System.out::println);

        System.out.println("\nCARTE FRANCESCO:");
        for (PhysicalCard card : board1.getHand()) {
            printPhysicalCard(card);
        }

        System.out.println("\nCARTE PIETRO:");
        for (PhysicalCard card : board2.getHand()) {
            printPhysicalCard(card);
        }

        controller.chooseStarterFace(player1, true);
        controller.chooseObjective(player1, 0);

        controller.chooseStarterFace(player2, true);
        controller.chooseObjective(player2, 0);

        System.out.println("\nAREA FRANCESCO:");
        board1.printCornersArea();
        board1.getCardsArea().printCardsArea();

        System.out.println("\nAREA PIETRO:");
        board2.printCornersArea();
        board2.getCardsArea().printCardsArea();

        controller.placeCard(player1, 0, true, 41, 41);

        System.out.println("\nAREA FRANCESCO:");
        board1.printCornersArea();
        board1.getCardsArea().printCardsArea();

        System.out.println("\nFINE GIOCO");
        game.forceEnd();
    }

    // Test Methods ____________________________________________________________________________________________________
    private void printPhysicalCard(PhysicalCard card) {
        System.out.println("______________________________________________________");
        System.out.println(card.getCardType());
        System.out.println(card.getFront().getPoints());
        System.out.println(card.getFront().getColor());
        for (Corner x : card.getFront().getCorners()) {
            if (x != null) {
                if (x.isFull()) {
                    System.out.println(x.getResource().toString());
                } else if (x.isVisible()) {
                    System.out.println("EMPTY");
                } else {
                    System.out.println("HIDDEN");
                }
            } else {
                System.out.println("null");
            }
        }
    }
}
