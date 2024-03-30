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
        Game a = new Game("a", 3, new Player("Pietro"));

        for (int i = 0; i < a.resourceDeckSize(); i++) {
            System.out.println(i);
            PhysicalCard card = a.drawCard(DrawingPosition.RESOURCEDECK);
            printPhysicalCard(card);
        }
        IntStream.range(0, a.goldDeckSize()).mapToObj(i -> a.drawCard(DrawingPosition.GOLDDECK)).forEach(this::printPhysicalCard);
        for (int i = 0; i < 13; i++) {
            ObjectiveCard op = a.getSecreteObjective2();
            System.out.println(op.getPointsPerCompletion());
        }
    }

    @Test
    public void Test2() {
        ClientController controller = new ClientController(null);
        controller.setPlayer("Francesco");

        controller.createGame("a", 1);
        Game game = GamesManager.getInstance().getGame("a");
        PlayerData board = game.getPlayerData("Francesco");

        controller.drawCard(DrawingPosition.RESOURCE1);
        controller.drawCard(DrawingPosition.RESOURCEDECK);
        controller.drawCard(DrawingPosition.GOLDDECK);

        for (PhysicalCard card : board.getHand()) {
            printPhysicalCard(card);
        }
        board.printCornersArea();
        board.getCardsArea().printCardsArea();

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
}
