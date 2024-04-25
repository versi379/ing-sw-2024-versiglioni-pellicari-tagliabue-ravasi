package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.CardType;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    void addPlayer() {
        Game a = new Game("TEST GAME", 4, 20, new Player("TEST1"));
        Player pl2 = new Player("TEST2");
        a.addPlayer(pl2);
        assertEquals(pl2, a.getPlayerList().get(1));

    }

    @Test
    void removePlayer() {
        Game a = new Game("TEST GAME", 4, 20, new Player("TEST1"));
        Player pl2 = new Player("TEST2");
        a.removePlayer(pl2);
        IntStream.range(0, a.getPlayerList().size()).forEach(i -> assertNotEquals(pl2, a.getPlayerList().get(i)));
    }

    @Test
    void getPlayerList() {
        Player pl1 = new Player("TEST1");
        Player pl2 = new Player("TEST2");
        Player pl3 = new Player("TEST3");
        Game a = new Game("TEST GAME", 4, 20, pl1);
        a.addPlayer(pl2);
        a.addPlayer(pl3);

        assertEquals(3, a.getPlayerList().size());
        assertEquals(pl1, a.getPlayerList().get(0));
        assertEquals(pl2, a.getPlayerList().get(1));
        assertEquals(pl3, a.getPlayerList().get(2));
    }


    @Test
    void getStarterCard() {
        Player pl1 = new Player("TEST1");
        Player pl2 = new Player("TEST2");
        Player pl3 = new Player("TEST3");
        Game a = new Game("TEST GAME", 3, 20, pl1);
        a.addPlayer(pl2);
        a.addPlayer(pl3);
        PhysicalCard st1 = a.getStarterCard(pl1);
        PhysicalCard st2 = a.getStarterCard(pl2);
        PhysicalCard st3 = a.getStarterCard(pl3);
        assertEquals(CardType.STARTER, st1.getCardType());
    }

    @Test
    void getSecretObjectivesList() {
        Player pl1 = new Player("TEST1");
        Player pl2 = new Player("TEST2");
        Player pl3 = new Player("TEST3");
        Game a = new Game("TEST GAME", 3, 20, pl1);
        a.addPlayer(pl2);
        a.addPlayer(pl3);
        List<ObjectiveCard> st1 = a.getSecretObjectivesList(pl1);
        assertEquals(2, st1.size());
        assertNotEquals(null, st1.get(0).getObjective());
        assertNotEquals(null, st1.get(1).getObjective());

    }

    @Test
    void pickCard() {
        Player pl1 = new Player("TEST1");
        Player pl2 = new Player("TEST2");
        Player pl3 = new Player("TEST3");
        int num;
        Game a = new Game("TEST GAME", 3, 20, pl1);
        a.addPlayer(pl2);
        a.addPlayer(pl3);

        num = a.goldDeckSize();
        PhysicalCard card1 = a.pickCard(DrawingPosition.GOLDDECK);
        assertEquals(CardType.GOLD, card1.getCardType());
        assertEquals(num - 1, a.goldDeckSize());

        num = a.resourceDeckSize();
        PhysicalCard card2 = a.pickCard(DrawingPosition.RESOURCEDECK);
        assertEquals(CardType.RESOURCE, card2.getCardType());
        assertEquals(num - 1, a.resourceDeckSize());

        num = a.resourceDeckSize();
        PhysicalCard card3 = a.pickCard(DrawingPosition.RESOURCE1);
        assertEquals(CardType.RESOURCE, card3.getCardType());
        assertEquals(num - 1, a.resourceDeckSize());

        num = a.resourceDeckSize();
        PhysicalCard card4 = a.pickCard(DrawingPosition.RESOURCE2);
        assertEquals(CardType.RESOURCE, card4.getCardType());
        assertEquals(num - 1, a.resourceDeckSize());

        num = a.goldDeckSize();
        PhysicalCard card5 = a.pickCard(DrawingPosition.GOLD1);
        assertEquals(CardType.GOLD, card5.getCardType());
        assertEquals(num - 1, a.goldDeckSize());

        num = a.goldDeckSize();
        PhysicalCard card6 = a.pickCard(DrawingPosition.GOLD2);
        assertEquals(CardType.GOLD, card6.getCardType());
        assertEquals(num - 1, a.goldDeckSize());

        Game b = new Game("TEST GAME", 3, 20, pl1);
        b.addPlayer(pl2);
        b.addPlayer(pl3);

        IntStream.range(0, b.goldDeckSize()).mapToObj(i -> b.pickCard(DrawingPosition.GOLDDECK)).forEach(card1b -> assertEquals(CardType.GOLD, card1b.getCardType()));
        PhysicalCard card1b = b.pickCard(DrawingPosition.GOLDDECK);
        assertEquals(null, card1b);

        IntStream.range(0, b.resourceDeckSize()).mapToObj(i -> b.pickCard(DrawingPosition.RESOURCEDECK)).forEach(card2b -> assertEquals(CardType.RESOURCE, card2b.getCardType()));
        PhysicalCard card2b = b.pickCard(DrawingPosition.RESOURCEDECK);
        assertEquals(null, card2b);

        Game c = new Game("TEST GAME", 3, 20, pl1);
        c.addPlayer(pl2);
        c.addPlayer(pl3);
        PhysicalCard card1c;

        for (int i = 0; i < 36; i++) {
            card1c = c.pickCard(DrawingPosition.GOLD1);
            assertEquals(CardType.GOLD, card1c.getCardType());
        }
        card1c = c.pickCard(DrawingPosition.GOLD2);
        assertEquals(CardType.GOLD, card1c.getCardType());

        card1c = c.pickCard(DrawingPosition.GOLD2);
        assertEquals(CardType.RESOURCE, card1c.getCardType());

        card1c = c.pickCard(DrawingPosition.GOLD1);
        assertEquals(CardType.RESOURCE, card1c.getCardType());

        for (int i = 0; i < 29; i++) {
            card1c = c.pickCard(DrawingPosition.RESOURCE1);
            assertEquals(CardType.RESOURCE, card1c.getCardType());
        }
        card1c = c.pickCard(DrawingPosition.RESOURCE1);
        assertEquals(null, card1c);

        card1c = c.pickCard(DrawingPosition.RESOURCE2);
        assertEquals(CardType.RESOURCE, card1c.getCardType());

        card1c = c.pickCard(DrawingPosition.RESOURCE2);
        assertEquals(null, card1c);

        card1c = c.pickCard(DrawingPosition.GOLD2);
        assertEquals(CardType.RESOURCE, card1c.getCardType());

        card1c = c.pickCard(DrawingPosition.GOLD1);
        assertEquals(CardType.RESOURCE, card1c.getCardType());

        card1c = c.pickCard(DrawingPosition.GOLD1);
        assertEquals(null, card1c);
        card1c = c.pickCard(DrawingPosition.GOLD2);
        assertEquals(null, card1c);

    }

    @Test
    void placeCard() {
        Player pl1 = new Player("TEST1");
        Player pl2 = new Player("TEST2");
        Player pl3 = new Player("TEST3");
        Game a = new Game("TEST GAME", 3, 20, pl1);
        a.addPlayer(pl2);
        a.addPlayer(pl3);
        PhysicalCard card1;

        card1 = a.pickCard(DrawingPosition.RESOURCE2);
        a.placeCard(pl1, card1.getFront(), 1, 1);
        assertEquals(card1.getFront(), a.getPlayerData(pl1).getCard(1, 1));

        card1 = a.pickCard(DrawingPosition.RESOURCE1);
        a.placeCard(pl1, card1.getFront(), 10, 10);
        assertEquals(card1.getFront(), a.getPlayerData(pl1).getCard(10, 10));

        card1 = a.pickCard(DrawingPosition.RESOURCE2);
        a.placeCard(pl1, card1.getBack(), 1, 1);
        assertEquals(card1.getBack(), a.getPlayerData(pl1).getCard(1, 1));

        card1 = a.pickCard(DrawingPosition.RESOURCE1);
        a.placeCard(pl1, card1.getBack(), 10, 10);
        assertEquals(card1.getBack(), a.getPlayerData(pl1).getCard(10, 10));


    }

    @Test
    void addCard() {
        Player pl1 = new Player("TEST1");
        Player pl2 = new Player("TEST2");
        Player pl3 = new Player("TEST3");
        Game a = new Game("TEST GAME", 3, 20, pl1);
        a.addPlayer(pl2);
        a.addPlayer(pl3);
        PhysicalCard card1;
        card1= a.pickCard(DrawingPosition.GOLD2);
        a.addCard(pl1,card1);
        List<PhysicalCard> cards= a.getHand(pl1);
        assertEquals(card1,cards.get(0));


    }

    @Test
    void removeCard() {
        Player pl1 = new Player("TEST1");
        Player pl2 = new Player("TEST2");
        Player pl3 = new Player("TEST3");
        Game a = new Game("TEST GAME", 3, 20, pl1);
        a.addPlayer(pl2);
        a.addPlayer(pl3);
        PhysicalCard card1,card2;
        card1= a.pickCard(DrawingPosition.GOLD2);
        card2= a.pickCard(DrawingPosition.RESOURCE1);

        a.addCard(pl1,card1);
        a.addCard(pl1,card2);

        a.removeCard(pl1,0);
        List<PhysicalCard> cards= a.getHand(pl1);
        assertEquals(card2,cards.get(0));
        assertEquals(1,cards.size());
        a.addCard(pl1,card1);
        a.removeCard(pl1,1);
        a.removeCard(pl1,0);
        assertEquals(null,a.getHand(pl1));

    }

    @Test
    void getHand() {
        Player pl1 = new Player("TEST1");
        Player pl2 = new Player("TEST2");
        Player pl3 = new Player("TEST3");
        Game a = new Game("TEST GAME", 3, 20, pl1);
        a.addPlayer(pl2);
        a.addPlayer(pl3);
        PhysicalCard card1,card2;
        card1= a.pickCard(DrawingPosition.GOLD2);
        card2= a.pickCard(DrawingPosition.RESOURCE1);

        a.addCard(pl1,card1);
        a.addCard(pl1,card2);
        List<PhysicalCard> cards= a.getHand(pl1);
        assertEquals(card1,cards.get(0));
        assertEquals(card2,cards.get(1));


    }

    @Test
    void forceEnd() {
    }
}