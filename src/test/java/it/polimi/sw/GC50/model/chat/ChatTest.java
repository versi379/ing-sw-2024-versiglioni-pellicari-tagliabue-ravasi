package it.polimi.sw.GC50.model.chat;

import it.polimi.sw.GC50.model.lobby.Player;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatTest {

    @Test
    void Chat(){
        List<ChatMessage> chatMessages =  new ArrayList<ChatMessage>();
        Player player = new Player("XXX");
        ChatMessage m = new ChatMessage(player, "message1", LocalTime.now());
        chatMessages.add(m);
        assertEquals(chatMessages.get(0).getContent(),m.getContent());

    }
    @Test
    void addMessage() {

    }


}