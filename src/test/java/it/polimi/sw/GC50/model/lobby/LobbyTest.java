package it.polimi.sw.GC50.model.lobby;

import it.polimi.sw.GC50.net.MockClient;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LobbyTest {

    @Test
    void testLobbyConstructor() {
        Lobby lobby = new Lobby();

        assertEquals(new HashMap<>(), lobby.getFreeGames());
    }

    @Test
    void testAddPlayer() {
        Lobby lobby = new Lobby();
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();

        assertNull(lobby.addPlayer(client1, null));
        assertNull(lobby.addPlayer(client1, ""));
        assertEquals("Player1", lobby.addPlayer(client1, "Player1"));


        assertNull(lobby.addPlayer(client2, "Player1"));
        assertEquals("Player2", lobby.addPlayer(client2, "Player2"));
    }

    @Test
    void testRemovePlayer() {
        Lobby lobby = new Lobby();
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        lobby.addPlayer(client1, "Player1");
        lobby.removePlayer(client1);

        assertEquals("Player1", lobby.addPlayer(client2, "Player1"));
    }

    @Test
    void testCreateGame() {
        Lobby lobby = new Lobby();
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();


        assertNull(lobby.createGame(client1, "Game", 2, 20));
        assertEquals(new HashMap<>(), lobby.getFreeGames());


        lobby.addPlayer(client1, "Player1");
        assertNull(lobby.createGame(client1, null, 2, 20));
        assertNull(lobby.createGame(client1, "", 2, 20));
        lobby.createGame(client1, "Game", 2, 20);
        assertEquals(List.of("Player1"), lobby.getFreeGames().values().stream().toList().getFirst());


        lobby.addPlayer(client2, "Player2");
        assertNull(lobby.createGame(client2, "Game", 2, 20));
        assertEquals(1, lobby.getFreeGames().size());
    }

    @Test
    void testJoinGame() {
        Lobby lobby = new Lobby();
        MockClient client1 = new MockClient();
        MockClient client2 = new MockClient();
        MockClient client3 = new MockClient();
        lobby.addPlayer(client1, "Player1");
        lobby.createGame(client1, "Game", 3, 20);

        assertNull(lobby.joinGame(client2, "Game"));
        assertEquals(List.of("Player1"), lobby.getFreeGames().values().stream().toList().getFirst());


        lobby.addPlayer(client2, "Player2");

        assertNull(lobby.joinGame(client2, "Game1"));


        lobby.joinGame(client2, "Game");

        assertEquals(List.of("Player1", "Player2"), lobby.getFreeGames().values().stream().toList().getFirst());


        lobby.addPlayer(client3, "Player3");
        lobby.joinGame(client3, "Game");

        assertEquals(new HashMap<>(), lobby.getFreeGames());
    }
}
