package it.polimi.sw.GC50.model.chat;

import it.polimi.sw.GC50.model.lobby.Player;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This class defines messages that players can send to each other
 */
public class Message {

    private final Player sender;
    private final String content;
    private final String time;
    private final Player receiver;

    /**
     * This constructor creates a broadcast message (to all other players)
     */
    public Message(Player sender, String content, LocalTime time) {
        this.sender = sender;
        this.content = content;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        this.time = time.format(formatter);
        this.receiver = null;
    }

    /**
     * This constructor creates a message between sender and receiver
     */
    public Message(Player sender, String content, LocalTime time, Player receiver) throws UnsupportedOperationException {
        if(sender.equals(receiver)) throw new UnsupportedOperationException("You can't send a message to yourself!");
        this.sender = sender;
        this.content = content;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        this.time = time.format(formatter);
        this.receiver = receiver;
    }

    public Player getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return this.time;
    }

    public Player getReceiver() {
        return receiver;
    }
    
}
