package it.polimi.sw.GC50.model.lobby;

import it.polimi.sw.GC50.model.game.PlayerStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testPlayerConstructor() {
        Player player = new Player("Player");

        assertEquals("Player", player.getNickname());
        assertEquals(PlayerStatus.DISCONNECTED, player.getStatus());
    }

    @Test
    void setStatus() {
        Player player = new Player("Player");
        player.setStatus(PlayerStatus.CONNECTED);

        assertEquals(PlayerStatus.CONNECTED, player.getStatus());
    }

    @Test
    void testEqualsFalse() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        assertFalse(player1.equals(new Object()));
        assertNotEquals(player1.hashCode(), new Object().hashCode());
        assertFalse(player1.equals(player2));
        assertNotEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    void testEqualsTrue() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player1");

        assertTrue(player1.equals(player1));
        assertEquals(player1.hashCode(), player1.hashCode());
        assertTrue(player1.equals(player2));
        assertEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    void testToString() {
        Player player = new Player("Player");

        assertEquals("Player", player.toString());
    }
}
