package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.CardType;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.Resource;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.IdenticalResourcesObjective;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static it.polimi.sw.GC50.model.card.PlayableCardTest.redPlayableCard;
import static it.polimi.sw.GC50.model.card.PlayableCardTest.whitePlayableCard;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    void testGameConstructor() {
        String gameId = "Partita";
        int numPlayers = 3;
        int endScore = 20;
        Player player = new Player("Creator");
        Game game = new Game(gameId, numPlayers, endScore, player);

        assertEquals(gameId, game.getId());
        assertEquals(numPlayers, game.getNumPlayers());
        assertEquals(GameStatus.WAITING, game.getStatus());
        assertEquals(1, game.getPlayerList().size());
        assertEquals(player, game.getPlayerList().getFirst());
    }

    // PLAYERS MANAGEMENT ______________________________________________________________________________________________
    @Test
    void testAddPlayer() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game("Partita", 2, 20, player1);

        assertFalse(game.containsPlayer(player2));


        game.addPlayer(player2);

        assertTrue(game.containsPlayer(player2));
        assertEquals(2, game.getPlayerList().size());
        assertEquals(player2, game.getPlayerList().get(1));
        assertEquals(GameStatus.SETUP, game.getStatus());
    }

    @Test
    void testRemovePlayer() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game("Partita", 3, 20, player1);
        game.addPlayer(player2);
        game.removePlayer(player2);

        assertFalse(game.containsPlayer(player2));
        assertEquals(1, game.getPlayerList().size());
    }

    @Test
    void testGetPlayerList() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Player player3 = new Player("Player3");
        List<Player> expectedPlayerList = new ArrayList<>(Arrays.asList(player1, player2, player3));
        Game game = new Game("Partita", 3, 20, player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        assertEquals(expectedPlayerList, game.getPlayerList());
    }

    @Test
    void testGetCurrentPlayer() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Player player3 = new Player("Player3");
        Game game = new Game("Partita", 3, 20, player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        ObjectiveCard secretObjective = new ObjectiveCard(2,
                new IdenticalResourcesObjective(Resource.ANIMAL, 3));
        game.setSecretObjective(player1, secretObjective);
        game.setStarterCard(player1, whitePlayableCard);
        game.setSecretObjective(player2, secretObjective);
        game.setStarterCard(player2, whitePlayableCard);
        game.setSecretObjective(player3, secretObjective);
        game.setStarterCard(player3, whitePlayableCard);

        assertEquals(player1, game.getCurrentPlayer());


        game.addCard(player1, new PhysicalCard(CardType.RESOURCE, redPlayableCard, redPlayableCard));
        game.addCard(player2, new PhysicalCard(CardType.RESOURCE, redPlayableCard, redPlayableCard));

        assertEquals(player3, game.getCurrentPlayer());


        game.removePlayer(player3);

        assertEquals(player1, game.getCurrentPlayer());
    }

    @Test
    void testPlayerDataFromNickname() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game("Partita", 2, 20, player1);

        assertEquals(game.getPlayerData(player1), game.getPlayerData("Player1"));
        assertNull(game.getPlayerData("Player2"));


        game.addPlayer(player2);

        assertEquals(game.getPlayerData(player2), game.getPlayerData("Player2"));
    }

    // SETUP PHASE _____________________________________________________________________________________________________
    @Test
    void testStartingChoices() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        PhysicalCard starterCard = new PhysicalCard(CardType.STARTER, whitePlayableCard, whitePlayableCard);
        ObjectiveCard objective = new ObjectiveCard(2,
                new IdenticalResourcesObjective(Resource.ANIMAL, 3));
        List<ObjectiveCard> objectiveCardList = new ArrayList<>(Arrays.asList(objective, objective));
        game.setStartingChoices(player, starterCard, objectiveCardList);

        assertEquals(starterCard, game.getStarterCard(player));
        assertEquals(objectiveCardList, game.getSecretObjectivesList(player));
    }

    @Test
    void testSetStarterCard() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        game.setStarterCard(player, whitePlayableCard);

        assertEquals(whitePlayableCard, game.getPlayerData(player).getCard(40, 40));
    }

    @Test
    void testSetSecretObjective() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        ObjectiveCard secretObjective = new ObjectiveCard(2,
                new IdenticalResourcesObjective(Resource.ANIMAL, 3));
        game.setSecretObjective(player, secretObjective);

        assertEquals(secretObjective, game.getPlayerData(player).getSecretObjective());
    }

    @Test
    void testIsReady() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game("Partita", 2, 20, player1);
        game.addPlayer(player2);

        assertFalse(game.isReady(player1));


        ObjectiveCard secretObjective = new ObjectiveCard(2,
                new IdenticalResourcesObjective(Resource.ANIMAL, 3));
        game.setSecretObjective(player1, secretObjective);
        game.setStarterCard(player1, whitePlayableCard);
        game.setStarterCard(player2, whitePlayableCard);

        assertTrue(game.isReady(player1));
        assertFalse(game.isReady(player2));
        assertEquals(GameStatus.SETUP, game.getStatus());


        game.setSecretObjective(player2, secretObjective);

        assertTrue(game.isReady(player2));
        assertEquals(GameStatus.PLAYING, game.getStatus());
    }

    // PLAYING PHASE ___________________________________________________________________________________________________
    @Test
    void testPickCardPositions() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        int num;
        PhysicalCard card;


        num = game.resourceDeckSize();
        card = game.pickCard(DrawingPosition.RESOURCEDECK);
        assertEquals(CardType.RESOURCE, card.getCardType());
        assertEquals(num - 1, game.resourceDeckSize());


        num = game.resourceDeckSize();
        card = game.pickCard(DrawingPosition.RESOURCE1);
        assertEquals(CardType.RESOURCE, card.getCardType());
        assertEquals(num - 1, game.resourceDeckSize());


        num = game.resourceDeckSize();
        card = game.pickCard(DrawingPosition.RESOURCE2);
        assertEquals(CardType.RESOURCE, card.getCardType());
        assertEquals(num - 1, game.resourceDeckSize());


        num = game.goldDeckSize();
        card = game.pickCard(DrawingPosition.GOLDDECK);
        assertEquals(CardType.GOLD, card.getCardType());
        assertEquals(num - 1, game.goldDeckSize());


        num = game.goldDeckSize();
        card = game.pickCard(DrawingPosition.GOLD1);
        assertEquals(CardType.GOLD, card.getCardType());
        assertEquals(num - 1, game.goldDeckSize());


        num = game.goldDeckSize();
        card = game.pickCard(DrawingPosition.GOLD2);
        assertEquals(CardType.GOLD, card.getCardType());
        assertEquals(num - 1, game.goldDeckSize());
    }

    @Test
    void testPickCardEmptyResourceDeck() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);


        IntStream.range(0, game.resourceDeckSize())
                .mapToObj(i -> game.pickCard(DrawingPosition.RESOURCEDECK))
                .forEach(x -> assertEquals(CardType.RESOURCE, x.getCardType()));
        assertNull(game.pickCard(DrawingPosition.RESOURCEDECK));


        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.RESOURCE1).getCardType());
        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.RESOURCE1).getCardType());


        IntStream.range(0, game.goldDeckSize())
                .mapToObj(i -> game.pickCard(DrawingPosition.GOLDDECK))
                .forEach(x -> assertEquals(CardType.GOLD, x.getCardType()));
        assertNull(game.pickCard(DrawingPosition.GOLDDECK));


        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.GOLD1).getCardType());
        assertNull(game.pickCard(DrawingPosition.GOLD1));
    }

    @Test
    void testPickCardEmptyGoldDeck() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);


        IntStream.range(0, game.goldDeckSize())
                .mapToObj(i -> game.pickCard(DrawingPosition.GOLDDECK))
                .forEach(x -> assertEquals(CardType.GOLD, x.getCardType()));
        assertNull(game.pickCard(DrawingPosition.GOLDDECK));


        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.GOLD2).getCardType());
        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.GOLD2).getCardType());


        IntStream.range(0, game.resourceDeckSize())
                .mapToObj(i -> game.pickCard(DrawingPosition.RESOURCEDECK))
                .forEach(x -> assertEquals(CardType.RESOURCE, x.getCardType()));
        assertNull(game.pickCard(DrawingPosition.RESOURCEDECK));


        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.RESOURCE2).getCardType());
        assertNull(game.pickCard(DrawingPosition.RESOURCE2));
    }

    @Test
    void testPickCard() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        PhysicalCard card1c;

        for (
                int i = 0;
                i < 36; i++) {
            card1c = game.pickCard(DrawingPosition.GOLD1);
            assertEquals(CardType.GOLD, card1c.getCardType());
        }

        card1c = game.pickCard(DrawingPosition.GOLD2);

        assertEquals(CardType.GOLD, card1c.getCardType());

        card1c = game.pickCard(DrawingPosition.GOLD2);

        assertEquals(CardType.RESOURCE, card1c.getCardType());

        card1c = game.pickCard(DrawingPosition.GOLD1);

        assertEquals(CardType.RESOURCE, card1c.getCardType());

        for (
                int i = 0;
                i < 29; i++) {
            card1c = game.pickCard(DrawingPosition.RESOURCE1);
            assertEquals(CardType.RESOURCE, card1c.getCardType());
        }

        card1c = game.pickCard(DrawingPosition.RESOURCE1);

        assertEquals(null, card1c);

        card1c = game.pickCard(DrawingPosition.RESOURCE2);

        assertEquals(CardType.RESOURCE, card1c.getCardType());

        card1c = game.pickCard(DrawingPosition.RESOURCE2);

        assertEquals(null, card1c);

        card1c = game.pickCard(DrawingPosition.GOLD2);

        assertEquals(CardType.RESOURCE, card1c.getCardType());

        card1c = game.pickCard(DrawingPosition.GOLD1);

        assertEquals(CardType.RESOURCE, card1c.getCardType());

        card1c = game.pickCard(DrawingPosition.GOLD1);

        assertEquals(null, card1c);

        card1c = game.pickCard(DrawingPosition.GOLD2);

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
        PhysicalCard card1 = a.pickCard(DrawingPosition.GOLD2);
        a.addCard(pl1, card1);
        List<PhysicalCard> cards = a.getHand(pl1);

        assertEquals(card1, cards.get(3));
    }

    @Test
    void removeCard() {
        Player pl1 = new Player("TEST1");
        Player pl2 = new Player("TEST2");
        Player pl3 = new Player("TEST3");
        Game a = new Game("TEST GAME", 3, 20, pl1);
        a.addPlayer(pl2);
        a.addPlayer(pl3);
        PhysicalCard card1, card2;
        card1 = a.pickCard(DrawingPosition.GOLD2);
        card2 = a.pickCard(DrawingPosition.RESOURCE1);

        a.addCard(pl1, card1);
        a.addCard(pl1, card2);

        a.removeCard(pl1, 0);
        List<PhysicalCard> cards = a.getHand(pl1);

        assertEquals(4, cards.size());
        assertEquals(card2, cards.get(3));


        a.removeCard(pl1, 0);
        a.removeCard(pl1, 0);
        a.removeCard(pl1, 0);
        a.removeCard(pl1, 0);

        assertEquals(new ArrayList<>(), a.getHand(pl1));
    }

    @Test
    void getHand() {
        Player pl1 = new Player("TEST1");
        Player pl2 = new Player("TEST2");
        Player pl3 = new Player("TEST3");
        Game a = new Game("TEST GAME", 3, 20, pl1);
        a.addPlayer(pl2);
        a.addPlayer(pl3);
        PhysicalCard card1, card2;
        card1 = a.pickCard(DrawingPosition.GOLD2);
        card2 = a.pickCard(DrawingPosition.RESOURCE1);

        a.addCard(pl1, card1);
        a.addCard(pl1, card2);
        List<PhysicalCard> cards = a.getHand(pl1);

        assertEquals(card1, cards.get(3));
        assertEquals(card2, cards.get(4));
    }

    // END PHASE _______________________________________________________________________________________________________
    @Test
    void forceEnd() {
    }
}
