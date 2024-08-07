package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.cards.*;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objectives.IdenticalResourcesObjective;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static it.polimi.sw.GC50.model.cards.PlayableCardTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    void testGameConstructor() {
        int numPlayers = 3;
        int endScore = 20;
        Player player = new Player("Creator");
        Game game = new Game(numPlayers, endScore);
        game.addPlayer(player);

        assertEquals(numPlayers, game.getNumPlayers());
        assertEquals(GameStatus.WAITING, game.getStatus());
        assertEquals(1, game.getPlayerList().size());
        assertEquals(player, game.getPlayerList().getFirst());
        assertFalse(game.isLastRound());
        assertNull(game.getDecksTop());
    }

    // PLAYERS MANAGEMENT //////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    void testAddPlayer() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game(2, 20);
        game.addPlayer(player1);

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
        Game game = new Game(3, 20);
        game.addPlayer(player1);
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
        Game game = new Game(3, 20);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        assertEquals(expectedPlayerList, game.getPlayerList());
    }

    @Test
    void testGetCurrentPlayer() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Player player3 = new Player("Player3");
        Game game = new Game(3, 20);
        game.addPlayer(player1);
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
        Game game = new Game(2, 20);
        game.addPlayer(player1);

        assertEquals(game.getPlayerData(player1), game.getPlayerData("Player1"));
        assertNull(game.getPlayerData("Player2"));


        game.addPlayer(player2);

        assertEquals(game.getPlayerData(player2), game.getPlayerData("Player2"));
    }

    // SETUP PHASE /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    void testGetStartingChoices() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);

        assertEquals(CardType.STARTER, game.getStarterCard(player).getCardType());
        assertEquals(2, game.getSecretObjectivesSelection(player).size());
        game.getSecretObjectivesSelection(player).stream()
                .forEach(x -> assertEquals(CardType.OBJECTIVE, x.getCardType()));
    }

    @Test
    void testSetStarterCard() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);
        game.setStarterCard(player, whitePlayableCard);

        assertEquals(whitePlayableCard, game.getPlayerData(player).getCard(40, 40));
    }

    @Test
    void testSetSecretObjective() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);
        ObjectiveCard secretObjective = new ObjectiveCard(2,
                new IdenticalResourcesObjective(Resource.ANIMAL, 3));
        game.setSecretObjective(player, secretObjective);

        assertEquals(secretObjective, game.getPlayerData(player).getSecretObjective());
    }

    @Test
    void testIsReady() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game(2, 20);
        game.addPlayer(player1);
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

    // PLAYING PHASE ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    void testPickCardPositions() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);
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
    void testPickCardEmptyDecks1() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);


        IntStream.range(0, game.resourceDeckSize())
                .mapToObj(i -> game.pickCard(DrawingPosition.RESOURCEDECK))
                .forEach(x -> assertEquals(CardType.RESOURCE, x.getCardType()));
        assertNull(game.pickCard(DrawingPosition.RESOURCEDECK));
        assertFalse(game.isLastRound());


        IntStream.range(0, game.goldDeckSize())
                .mapToObj(i -> game.pickCard(DrawingPosition.GOLDDECK))
                .forEach(x -> assertEquals(CardType.GOLD, x.getCardType()));
        assertNull(game.pickCard(DrawingPosition.GOLDDECK));
        assertTrue(game.isLastRound());


        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.RESOURCE1).getCardType());
        assertNull(game.pickCard(DrawingPosition.RESOURCE1));
        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.RESOURCE2).getCardType());
        assertNull(game.pickCard(DrawingPosition.RESOURCE2));


        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.GOLD1).getCardType());
        assertNull(game.pickCard(DrawingPosition.GOLD1));
        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.GOLD2).getCardType());
        assertNull(game.pickCard(DrawingPosition.GOLD2));
    }

    @Test
    void testPickCardEmptyDecks2() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);


        IntStream.range(0, game.goldDeckSize())
                .mapToObj(i -> game.pickCard(DrawingPosition.GOLDDECK))
                .forEach(x -> assertEquals(CardType.GOLD, x.getCardType()));
        assertNull(game.pickCard(DrawingPosition.GOLDDECK));
        assertFalse(game.isLastRound());


        IntStream.range(0, game.resourceDeckSize())
                .mapToObj(i -> game.pickCard(DrawingPosition.RESOURCEDECK))
                .forEach(x -> assertEquals(CardType.RESOURCE, x.getCardType()));
        assertNull(game.pickCard(DrawingPosition.RESOURCEDECK));
        assertTrue(game.isLastRound());


        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.RESOURCE1).getCardType());
        assertNull(game.pickCard(DrawingPosition.RESOURCE1));
        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.RESOURCE2).getCardType());
        assertNull(game.pickCard(DrawingPosition.RESOURCE2));


        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.GOLD1).getCardType());
        assertNull(game.pickCard(DrawingPosition.GOLD1));
        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.GOLD2).getCardType());
        assertNull(game.pickCard(DrawingPosition.GOLD2));
    }

    @Test
    void testPickCardEmptyResourceDeck() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);

        while (game.resourceDeckSize() > 0) {
            assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.RESOURCE1).getCardType());
        }
        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.RESOURCE1).getCardType());
        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.RESOURCE2).getCardType());
        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.RESOURCE1).getCardType());
        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.RESOURCE2).getCardType());
    }

    @Test
    void testPickCardEmptyGoldDeck() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);

        while (game.goldDeckSize() > 0) {
            assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.GOLD1).getCardType());
        }
        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.GOLD1).getCardType());
        assertEquals(CardType.GOLD, game.pickCard(DrawingPosition.GOLD2).getCardType());
        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.GOLD1).getCardType());
        assertEquals(CardType.RESOURCE, game.pickCard(DrawingPosition.GOLD2).getCardType());
    }

    @Test
    void testPlaceCard() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);
        game.setStarterCard(player, whitePlayableCard);
        game.setSecretObjective(player,
                new ObjectiveCard(2, new IdenticalResourcesObjective(Resource.ANIMAL, 3)));
        PlayableCard card = new PlayableCard(Color.RED, 20, corners);
        game.placeCard(player, card, 41, 41);

        assertEquals(card, game.getPlayerData(player).getCard(41, 41));
        assertEquals(PlayingPhase.DRAWING, game.getCurrentPhase());
        assertTrue(game.isLastRound());
    }

    @Test
    void testAddCard() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game(2, 20);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.setStarterCard(player1, whitePlayableCard);
        game.setSecretObjective(player1,
                new ObjectiveCard(2, new IdenticalResourcesObjective(Resource.ANIMAL, 3)));
        game.setStarterCard(player2, whitePlayableCard);
        game.setSecretObjective(player2,
                new ObjectiveCard(2, new IdenticalResourcesObjective(Resource.ANIMAL, 3)));
        PhysicalCard card = game.pickCard(DrawingPosition.RESOURCEDECK);
        game.addCard(player1, card);

        assertEquals(4, game.getHand(player1).size());
        assertEquals(card, game.getHand(player1).getLast());
        assertEquals(player2, game.getCurrentPlayer());


        game.addCard(player2, card);

        assertEquals(4, game.getHand(player2).size());
        assertEquals(card, game.getHand(player2).getLast());
        assertEquals(player1, game.getCurrentPlayer());
    }

    @Test
    void testRemoveCard() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);
        PhysicalCard card = game.pickCard(DrawingPosition.RESOURCEDECK);
        game.addCard(player, card);
        game.removeCard(player, 0);

        assertEquals(3, game.getHand(player).size());
        assertEquals(card, game.getHand(player).getLast());
    }

    // END PHASE ///////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    void testEndTotalWinner() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game(2, 5);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.setStarterCard(player1, whitePlayableCard);
        game.setSecretObjective(player1,
                new ObjectiveCard(2, new IdenticalResourcesObjective(Resource.ANIMAL, 1)));
        game.setStarterCard(player2, whitePlayableCard);
        game.setSecretObjective(player2,
                new ObjectiveCard(2, new IdenticalResourcesObjective(Resource.ANIMAL, 1)));

        game.placeCard(player1, new PlayableCard(Color.RED, 10, corners), 41, 41);
        game.addCard(player1, game.pickCard(DrawingPosition.RESOURCEDECK));
        game.placeCard(player2, new PlayableCard(Color.RED, 4, corners), 41, 41);
        game.addCard(player2, game.pickCard(DrawingPosition.RESOURCEDECK));

        assertEquals(GameStatus.ENDED, game.getStatus());
        assertEquals(1, game.getWinnerList().size());
        assertEquals(player1, game.getWinnerList().getFirst());
    }

    @Test
    void testEndObjectivesWinner() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game(2, 5);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.setStarterCard(player1, whitePlayableCard);
        game.setSecretObjective(player1,
                new ObjectiveCard(1, new IdenticalResourcesObjective(Resource.ANIMAL, 1)));
        game.setStarterCard(player2, whitePlayableCard);
        game.setSecretObjective(player2,
                new ObjectiveCard(1, new IdenticalResourcesObjective(Resource.ANIMAL, 1)));

        game.placeCard(player1,
                new PlayableCard(Color.RED, 10, Arrays.asList(Resource.ANIMAL, Resource.ANIMAL),
                        corners), 41, 41);
        game.addCard(player1, game.pickCard(DrawingPosition.RESOURCEDECK));
        game.placeCard(player2,
                new PlayableCard(Color.RED, 11, List.of(Resource.ANIMAL),
                        corners), 41, 41);
        game.addCard(player2, game.pickCard(DrawingPosition.RESOURCEDECK));

        assertEquals(GameStatus.ENDED, game.getStatus());
        assertEquals(1, game.getWinnerList().size());
        assertEquals(player1, game.getWinnerList().getFirst());
    }

    @Test
    void testEndDraw() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game(2, 5);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.setStarterCard(player1, whitePlayableCard);
        game.setSecretObjective(player1,
                new ObjectiveCard(1, new IdenticalResourcesObjective(Resource.ANIMAL, 1)));
        game.setStarterCard(player2, whitePlayableCard);
        game.setSecretObjective(player2,
                new ObjectiveCard(1, new IdenticalResourcesObjective(Resource.ANIMAL, 1)));

        game.placeCard(player1,
                new PlayableCard(Color.RED, 10, Arrays.asList(Resource.ANIMAL, Resource.ANIMAL),
                        corners), 41, 41);
        game.addCard(player1, game.pickCard(DrawingPosition.RESOURCEDECK));
        game.placeCard(player2,
                new PlayableCard(Color.RED, 10, Arrays.asList(Resource.ANIMAL, Resource.ANIMAL),
                        corners), 41, 41);
        game.addCard(player2, game.pickCard(DrawingPosition.RESOURCEDECK));

        assertEquals(GameStatus.ENDED, game.getStatus());
        assertEquals(2, game.getWinnerList().size());
        assertEquals(player1, game.getWinnerList().getFirst());
        assertEquals(player2, game.getWinnerList().get(1));
    }
}
