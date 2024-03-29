package it.polimi.sw.gianpaolocugola50.game;

import it.polimi.sw.gianpaolocugola50.controller.PlayerController;
import it.polimi.sw.gianpaolocugola50.model.card.PhysicalCard;
import it.polimi.sw.gianpaolocugola50.model.game.DrawingPosition;
import it.polimi.sw.gianpaolocugola50.model.game.Game;
import it.polimi.sw.gianpaolocugola50.model.game.Player;
import it.polimi.sw.gianpaolocugola50.model.objective.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class GameTest {

    private void printPhysicalCard(PhysicalCard card) {
        System.out.println("______________________________________________________");
        System.out.println(card.getCardType());
        System.out.println(card.getFront().getPoints());
        System.out.println(card.getFront().getColor());
        if (card.getFront().getSwCorner().getResource() != null) {
            System.out.println(card.getFront().getSwCorner().getResource().toString());
        } else {
            System.out.println("null");
        }
        if (card.getFront().getNwCorner().getResource() != null) {
            System.out.println(card.getFront().getNwCorner().getResource().toString());
        } else {
            System.out.println("null");
        }
        if (card.getFront().getNeCorner().getResource() != null) {
            System.out.println(card.getFront().getNeCorner().getResource().toString());
        } else {
            System.out.println("null");
        }
        if (card.getFront().getSeCorner().getResource() != null) {
            System.out.println(card.getFront().getSeCorner().getResource().toString());
        } else {
            System.out.println("null");
        }
    }

    @Test
    void Test1() {
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
    public void test2() {
        Player player = new Player("Francesco");
        PlayerController controller = new PlayerController(player);

        controller.createGame("a", 3);
        controller.drawCard(DrawingPosition.RESOURCE1);
        controller.drawCard(DrawingPosition.RESOURCEDECK);
        controller.drawCard(DrawingPosition.GOLDDECK);
        for (PhysicalCard card : player.getPlayerData().getHand()) {
            printPhysicalCard(card);
        }
        player.getPlayerData().printCornersArea();
        controller.placeCard(1, true, 41, 41);
        player.getPlayerData().printCornersArea();
    }

    @Test
    public void test3() {
        Game a = new Game("a", 3, null);
        PhysicalCard physicalCard = a.drawCard(DrawingPosition.RESOURCE1);

    }
}
