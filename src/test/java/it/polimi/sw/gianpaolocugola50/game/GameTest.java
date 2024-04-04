package it.polimi.sw.gianpaolocugola50.game;

import it.polimi.sw.gianpaolocugola50.controller.ClientController;
import it.polimi.sw.gianpaolocugola50.model.card.Corner;
import it.polimi.sw.gianpaolocugola50.model.card.PhysicalCard;
import it.polimi.sw.gianpaolocugola50.model.game.*;
import it.polimi.sw.gianpaolocugola50.model.objective.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class GameTest {

    private void printPhysicalCard(PhysicalCard card) {
        System.out.println("______________________________________________________");
        System.out.println(card.getCardType());
        System.out.println(card.getFront().getPoints());
        System.out.println(card.getFront().getColor());
        for (Corner x : card.getFront().getCorners()) {
            if (x != null) {
                if(x.isFull()) {
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

    @Test
    public void Test1() {
        ClientController controller = new ClientController(null);
        controller.setPlayer("Francesco");

        controller.createGame("a", 1);
        Game a = GamesManager.getInstance().getGame("a");

        for (int i = 0; i < a.resourceDeckSize(); i++) {
            System.out.println(i);
            PhysicalCard card = a.pickCard(DrawingPosition.RESOURCEDECK);
            printPhysicalCard(card);
        }
        IntStream.range(0, a.goldDeckSize()).mapToObj(i -> a.pickCard(DrawingPosition.GOLDDECK)).forEach(this::printPhysicalCard);
        a.getObjectives(20).stream()
                .map(ObjectiveCard::getPointsPerCompletion)
                .forEach(System.out::println);
    }

    @Test
    public void TestPiazzamentoCarte() {
        ClientController controller = new ClientController(null);
        controller.setPlayer("Francesco");

        controller.createGame("a", 1);
        Game game = GamesManager.getInstance().getGame("a");
        PlayerData board = game.getPlayerData("Francesco");

        for (PhysicalCard card : board.getHand()) {
            printPhysicalCard(card);
        }
        board.printCornersArea();
        board.getCardsArea().printCardsArea();

        controller.chooseStarterFace(true);
        controller.chooseObjective(0);

        System.out.println("\nLISTA GIOCATORI:");
        game.getPlayerList().forEach(System.out::println);

        System.out.println("\nPIAZZA CARTA 1");
        controller.placeCard(1, true, 41, 41);
        board.printCornersArea();
        board.getCardsArea().printCardsArea();

        System.out.println("\nPESCA CARTA");
        controller.drawCard(DrawingPosition.RESOURCE1);
        for (PhysicalCard card : board.getHand()) {
            printPhysicalCard(card);
        }

        System.out.println("\nPIAZZA CARTA 2");
        controller.placeCard(2, true, 42, 40);
        board.printCornersArea();
        board.getCardsArea().printCardsArea();
    }

    @Test
    public void TestMultigiocatore() {
        ClientController controller1 = new ClientController(null);
        controller1.setPlayer("Francesco");

        controller1.createGame("a", 2);
        Game game = GamesManager.getInstance().getGame("a");
        PlayerData board1 = game.getPlayerData("Francesco");

        ClientController controller2 = new ClientController(null);
        controller2.setPlayer("Pietro");
        controller2.joinGame("a");
        PlayerData board2 = game.getPlayerData("Pietro");

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

        controller1.chooseStarterFace(true);
        controller1.chooseObjective(0);

        controller2.chooseStarterFace(true);
        controller2.chooseObjective(0);

        System.out.println("\nAREA FRANCESCO:");
        board1.printCornersArea();
        board1.getCardsArea().printCardsArea();

        System.out.println("\nAREA PIETRO:");
        board2.printCornersArea();
        board2.getCardsArea().printCardsArea();

        controller1.placeCard(0, true, 41, 41);

        System.out.println("\nAREA FRANCESCO:");
        board1.printCornersArea();
        board1.getCardsArea().printCardsArea();

        System.out.println("\nFINE GIOCO");
        game.end();
    }
}
