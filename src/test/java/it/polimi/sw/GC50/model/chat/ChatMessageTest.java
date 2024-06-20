package it.polimi.sw.GC50.model.chat;

import it.polimi.sw.GC50.model.lobby.Player;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ChatMessageTest {

    @Test
    void testChatMessageNoReceiverConstructor() {
        Player player = new Player("Player");
        LocalTime time = LocalTime.of(8, 20, 45);
        ChatMessage chatMessage = new ChatMessage(player, "Hello world", time);

        assertEquals(player, chatMessage.getSender());
        assertNull(chatMessage.getReceiver());
        assertEquals("Hello world", chatMessage.getContent());
        assertEquals(time.toString(), chatMessage.getTime());
    }

    @Test
    void testChatMessageFullConstructor() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        LocalTime time = LocalTime.of(8, 20, 45);
        ChatMessage chatMessage = new ChatMessage(player1, player2, "Hello world", time);

        assertEquals(player1, chatMessage.getSender());
        assertEquals(player2, chatMessage.getReceiver());
        assertEquals("Hello world", chatMessage.getContent());
        assertEquals(time.toString(), chatMessage.getTime());
    }
}
