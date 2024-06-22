package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.net.messages.*;
import it.polimi.sw.GC50.net.requests.ChatMessageRequest;
import it.polimi.sw.GC50.net.client.MockClient;
import it.polimi.sw.GC50.net.messages.Notify;
import it.polimi.sw.GC50.net.requests.PlaceCardRequest;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    @Test
    void testGameControllerConstructor() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 2, 20, "Player");

        assertEquals("Game", controller.getGameId());
        assertEquals(List.of("Player"), controller.getPlayerList());
        assertEquals(Notify.NOTIFY_PLAYER_JOINED_GAME, client.getNotify());
        assertEquals("Player", ((PlayerJoinedMex) client.getMessage()).getNickname());
    }

    @Test
    void testIsFree() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 2, 20, "Player1");

        assertTrue(controller.isFree());


        controller.addPlayer(client2, "Player2");

        assertFalse(controller.isFree());
    }

    @Test
    void testIsEmpty() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 2, 20, "Player");

        assertFalse(controller.isEmpty());


        controller.removePlayer(client);

        assertTrue(controller.isEmpty());


        controller.addPlayer(client, "Player");

        assertFalse(controller.isEmpty());
    }

    @Test
    void testAddPlayerNotWaiting() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 1, 20, "Player1");

        assertFalse(controller.addPlayer(client2, "Player2"));
        assertEquals(List.of("Player1"), controller.getPlayerList());
    }

    @Test
    void testAddPlayer() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 3, 20, "Player1");
        controller.addPlayer(client2, "Player2");

        assertEquals(List.of("Player1", "Player2"), controller.getPlayerList());
        assertEquals(Notify.NOTIFY_PLAYER_JOINED_GAME, client1.getNotify());
        assertEquals("Player2", ((PlayerJoinedMex) client1.getMessage()).getNickname());
    }

    @Test
    void testAllPlayersJoined() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 2, 20, "Player1");
        controller.addPlayer(client2, "Player2");

        assertEquals(Notify.NOTIFY_GAME_SETUP, client1.getNotify());
    }

    @Test
    void testRemovePlayer() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 3, 20, "Player1");
        controller.addPlayer(client2, "Player2");
        controller.removePlayer(client1);

        assertEquals(List.of("Player2"), controller.getPlayerList());
        assertEquals(Notify.NOTIFY_PLAYER_LEFT_GAME, client2.getNotify());
        assertEquals("Player1", ((PlayerMex) client2.getMessage()).getNickname());
    }

    @Test
    void testChooseStarterFaceNotStarting() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 2, 20, "Player");
        controller.selectStarterFace(client, 1);

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testChooseStarterFaceInvalidIndex() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectStarterFace(client, 10);

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testChooseStarterFace() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectStarterFace(client, 1);

        assertEquals(Notify.NOTIFY_GAME_SETUP, client.getNotify());
    }

    @Test
    void testChooseObjectiveNotStarting() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 2, 20, "Player");
        controller.selectSecretObjective(client, 0);

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testChooseObjectiveInvalidIndex() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectSecretObjective(client, 10);

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testChooseObjective() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectSecretObjective(client, 0);

        assertEquals(Notify.NOTIFY_GAME_SETUP, client.getNotify());
    }

    @Test
    void testAllPlayersReady() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 2, 20, "Player1");
        controller.addPlayer(client2, "Player2");
        controller.selectSecretObjective(client1, 0);
        controller.selectStarterFace(client1, 1);

        assertEquals(Notify.NOTIFY_PLAYER_READY, client1.getNotify());
        assertEquals("Player1", ((PlayerReadyMex) client1.getMessage()).getNickname());


        controller.selectSecretObjective(client2, 0);
        controller.selectStarterFace(client2, 1);

        assertEquals(Notify.NOTIFY_GAME_STARTED, client1.getNotify());
        assertEquals("Player1", ((PlayerMex) client1.getMessage()).getNickname());
    }

    @Test
    void testPlaceCardNotPlaying() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.placeCard(client, new PlaceCardRequest(0, 0, 41, 41));

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testPlaceCardNotPlayerTurn() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 2, 20, "Player1");
        controller.addPlayer(client2, "Player2");
        controller.selectSecretObjective(client1, 0);
        controller.selectStarterFace(client1, 1);
        controller.selectSecretObjective(client2, 0);
        controller.selectStarterFace(client2, 1);
        controller.placeCard(client2, new PlaceCardRequest(0, 0, 41, 41));

        assertEquals(Notify.NOTIFY_ERROR, client2.getNotify());
    }

    @Test
    void testPlaceCardNotPlacingPhase() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectSecretObjective(client, 0);
        controller.selectStarterFace(client, 1);
        controller.placeCard(client, new PlaceCardRequest(0, 0, 41, 41));
        controller.placeCard(client, new PlaceCardRequest(0, 0, 42, 42));

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testPlaceCardInvalidCardIndex() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectSecretObjective(client, 0);
        controller.selectStarterFace(client, 1);
        controller.placeCard(client, new PlaceCardRequest(10, 0, 41, 41));

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testPlaceCardInvalidFaceIndex() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectSecretObjective(client, 0);
        controller.selectStarterFace(client, 1);
        controller.placeCard(client, new PlaceCardRequest(0, 10, 41, 41));

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testPlaceCardInvalidPosition() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectSecretObjective(client, 0);
        controller.selectStarterFace(client, 1);
        controller.placeCard(client, new PlaceCardRequest(0, 0, 100, 100));

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testPlaceCard() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectSecretObjective(client, 0);
        controller.selectStarterFace(client, 1);
        controller.placeCard(client, new PlaceCardRequest(0, 0, 41, 41));

        assertEquals(Notify.NOTIFY_CARD_PLACED, client.getNotify());
        assertEquals("Player", ((BoardUpdateMex) client.getMessage()).getNickname());
    }

    @Test
    void testDrawCardNotPlaying() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.drawCard(client, 0);

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testDrawCardNotPlayerTurn() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 2, 20, "Player1");
        controller.addPlayer(client2, "Player2");
        controller.selectSecretObjective(client1, 0);
        controller.selectStarterFace(client1, 1);
        controller.selectSecretObjective(client2, 0);
        controller.selectStarterFace(client2, 1);
        controller.drawCard(client2, 0);

        assertEquals(Notify.NOTIFY_ERROR, client2.getNotify());
    }

    @Test
    void testDrawCardNotDrawingPhase() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectSecretObjective(client, 0);
        controller.selectStarterFace(client, 1);
        controller.drawCard(client, 0);

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testDrawCardInvalidPosition() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectSecretObjective(client, 0);
        controller.selectStarterFace(client, 1);
        controller.placeCard(client, new PlaceCardRequest(0, 0, 41, 39));
        controller.drawCard(client, 10);

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());


        for (int i = 0; i < 36; i++) {
            int x = (i % 2 == 0) ? (40 + ((i / 2) + 1)) : (40 - ((i / 2) + 1));
            controller.drawCard(client, 0);
            controller.placeCard(client, new PlaceCardRequest(2, 1, x, x));
        }
        controller.drawCard(client, 0);

        assertEquals(Notify.NOTIFY_ERROR, client.getNotify());
    }

    @Test
    void testDrawCard() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.selectSecretObjective(client, 0);
        controller.selectStarterFace(client, 1);
        controller.placeCard(client, new PlaceCardRequest(0, 0, 41, 39));
        controller.drawCard(client, 0);

        assertEquals(Notify.NOTIFY_NEXT_TURN, client.getNotify());
    }

    @Test
    void testChatMessagePublic() throws RemoteException {
        MockClient client = new MockClient();
        GameController controller = new GameController(client, "Game", 1, 20, "Player");
        controller.sendChatMessage(client, new ChatMessageRequest("Hello world"));

        assertEquals(Notify.NOTIFY_CHAT_MESSAGE, client.getNotify());
        assertEquals("Hello world", ((ChatMex) client.getMessage()).getContent());
    }

    @Test
    void testChatMessagePrivateInvalidReceiver() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 2, 20, "Player1");
        controller.addPlayer(client2, "Player2");
        controller.sendChatMessage(client1, new ChatMessageRequest("Player3", "Hello world"));

        assertEquals(Notify.NOTIFY_ERROR, client1.getNotify());


        controller.sendChatMessage(client1, new ChatMessageRequest("Player1", "Hello world"));

        assertEquals(Notify.NOTIFY_ERROR, client1.getNotify());
    }

    @Test
    void testChatMessagePrivate() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 2, 20, "Player1");
        controller.addPlayer(client2, "Player2");
        controller.sendChatMessage(client1, new ChatMessageRequest("Player2", "Hello world"));

        assertEquals(Notify.NOTIFY_CHAT_MESSAGE, client1.getNotify());
        assertEquals("Player2", ((ChatMex) client1.getMessage()).getReceiver());
        assertEquals("Hello world", ((ChatMex) client1.getMessage()).getContent());
    }

    @Test
    void testLeaveGame() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 3, 20, "Player1");
        controller.addPlayer(client2, "Player2");
        controller.leaveGame(client1);

        assertEquals(List.of("Player2"), controller.getPlayerList());
        assertEquals(Notify.NOTIFY_PLAYER_LEFT_GAME, client2.getNotify());
        assertEquals("Player1", ((PlayerMex) client2.getMessage()).getNickname());
    }

    @Test
    void testUnknownPlayer() throws RemoteException {
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        GameController controller = new GameController(client1, "Game", 1, 20, "Player1");

        assertThrowsExactly(RemoteException.class,
                () -> controller.sendChatMessage(client2, new ChatMessageRequest("Player2", "Hello world")));
    }
}
