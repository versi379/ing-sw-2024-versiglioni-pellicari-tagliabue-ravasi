package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.model.card.CardType;
import it.polimi.sw.GC50.model.card.Corner;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.game.*;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    @Test
    void testGameControllerConstructor() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);

        assertEquals(game, controller.getGame());
    }

    @Test
    void testAddPlayerNotWaiting() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game("Partita", 1, 20, player1);
        GameController controller = new GameController(game);
        controller.addPlayer(player2);

        assertEquals("Partita già iniziata", player2.getLatestError());
    }

    @Test
    void testAddPlayer() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game("Partita", 2, 20, player1);
        GameController controller = new GameController(game);
        controller.addPlayer(player2);

        assertEquals(2, game.getPlayerList().size());
        assertEquals(player2, game.getPlayerList().get(1));
    }

    @Test
    void testRemovePlayer() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game("Partita", 2, 20, player1);
        GameController controller = new GameController(game);
        controller.addPlayer(player2);
        controller.removePlayer(player2);

        assertEquals(1, game.getPlayerList().size());
    }

    @Test
    void testChooseStarterFaceNotStarting() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 2, 20, player);
        GameController controller = new GameController(game);
        controller.chooseStarterFace(player, true);

        assertEquals("Operazione non disponibile", player.getLatestError());
    }

    @Test
    void testChooseStarterFace() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);
        PhysicalCard starterCard = game.getStarterCard(player);
        controller.chooseStarterFace(player, true);

        assertEquals(starterCard.getFront(), game.getPlayerData(player).getCard(40, 40));
    }

    @Test
    void testChooseObjectiveNotStarting() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 2, 20, player);
        GameController controller = new GameController(game);
        controller.chooseObjective(player, 0);

        assertEquals("Operazione non disponibile", player.getLatestError());
    }

    @Test
    void testChooseObjectiveInvalidIndex() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);
        controller.chooseObjective(player, 100);

        assertEquals("Indice non valido", player.getLatestError());
    }

    @Test
    void testChooseObjective() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);
        ObjectiveCard secretObjective = game.getSecretObjectivesList(player).getFirst();
        controller.chooseObjective(player, 0);

        assertEquals(secretObjective, game.getPlayerData(player).getSecretObjective());
    }

    @Test
    void testPlaceCardNotPlaying() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 2, 20, player);
        GameController controller = new GameController(game);
        controller.placeCard(player, 0, true, 41, 41);

        assertEquals("Operazione non disponibile", player.getLatestError());
    }

    @Test
    void testPlaceCardNotPlayerTurn() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game("Partita", 2, 20, player1);
        GameController controller = new GameController(game);
        controller.addPlayer(player2);
        controller.chooseStarterFace(player1, true);
        controller.chooseObjective(player1, 0);
        controller.chooseStarterFace(player2, true);
        controller.chooseObjective(player2, 0);
        controller.placeCard(player2, 0, true, 41, 41);

        assertEquals("Operazione non disponibile", player2.getLatestError());
    }

    @Test
    void testPlaceCardInvalidIndex() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);
        controller.chooseStarterFace(player, true);
        controller.chooseObjective(player, 0);
        controller.placeCard(player, 100, true, 41, 41);

        assertEquals("Indice non valido", player.getLatestError());
    }

    @Test
    void testPlaceCardInvalidPosition() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);
        controller.chooseStarterFace(player, true);
        controller.chooseObjective(player, 0);
        controller.placeCard(player, 0, true, 100, 100);

        assertEquals("Carta non piazzabile", player.getLatestError());
    }

    @Test
    void testPlaceCard() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);
        controller.chooseStarterFace(player, true);
        controller.chooseObjective(player, 0);
        PhysicalCard card = game.getHand(player).getFirst();
        controller.placeCard(player, 0, true, 41, 41);

        assertEquals(card.getFront(), game.getPlayerData(player).getCard(41, 41));
    }

    @Test
    void testPlaceCardNotPlacingPhase() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);
        controller.chooseStarterFace(player, true);
        controller.chooseObjective(player, 0);
        controller.placeCard(player, 0, true, 41, 41);
        controller.placeCard(player, 0, true, 42, 42);

        assertEquals("Operazione non disponibile", player.getLatestError());
    }

    @Test
    void testDrawCardNotPlaying() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 2, 20, player);
        GameController controller = new GameController(game);
        controller.drawCard(player, DrawingPosition.RESOURCEDECK);

        assertEquals("Operazione non disponibile", player.getLatestError());
    }

    @Test
    void testDrawCardNotPlayerTurn() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Game game = new Game("Partita", 2, 20, player1);
        GameController controller = new GameController(game);
        controller.addPlayer(player2);
        controller.chooseStarterFace(player1, true);
        controller.chooseObjective(player1, 0);
        controller.chooseStarterFace(player2, true);
        controller.chooseObjective(player2, 0);
        controller.drawCard(player2, DrawingPosition.RESOURCEDECK);

        assertEquals("Operazione non disponibile", player2.getLatestError());
    }

    @Test
    void testDrawCardNotDrawingPhase() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);
        controller.chooseStarterFace(player, true);
        controller.chooseObjective(player, 0);
        controller.drawCard(player, DrawingPosition.RESOURCEDECK);

        assertEquals("Operazione non disponibile", player.getLatestError());
    }

    @Test
    void testDrawCardInvalidPosition() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);
        controller.chooseStarterFace(player, true);
        controller.chooseObjective(player, 0);
        controller.placeCard(player, 0, false, 41, 39);
        for (int i = 0; i < 36; i++) {
            int position = (i % 2 == 0) ? (40 + ((i / 2) + 1)) : (40 - ((i / 2) + 1));
            controller.drawCard(player, DrawingPosition.RESOURCEDECK);
            controller.placeCard(player, 2, false, position, position);
        }
        controller.drawCard(player, DrawingPosition.RESOURCEDECK);

        assertEquals("Posizione non disponibile", player.getLatestError());
    }

    @Test
    void testDrawCard() {
        Player player = new Player("Player");
        Game game = new Game("Partita", 1, 20, player);
        GameController controller = new GameController(game);
        controller.chooseStarterFace(player, true);
        controller.chooseObjective(player, 0);
        controller.placeCard(player, 0, true, 41, 41);
        controller.drawCard(player, DrawingPosition.RESOURCEDECK);

        assertEquals(3, game.getHand(player).size());
        assertEquals(CardType.RESOURCE, game.getHand(player).getLast().getCardType());
    }

    // OTHER ___________________________________________________________________________________________________________
    @Test
    void testCardsVisualization() {
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
    void testCardsPlacement() {
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
    void testMultiplayer() {
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

    // TEST METHODS ____________________________________________________________________________________________________
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
