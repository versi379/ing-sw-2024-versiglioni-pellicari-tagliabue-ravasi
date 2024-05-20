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
        List<Message> messages =  new ArrayList<Message>();
        Player player = new Player("XXX");
        Message m = new Message(player, "message1", LocalTime.now());
        messages.add(m);
        assertEquals(messages.get(0).getContent(),m.getContent());

    }
    @Test
    void addMessage() {

    }


}