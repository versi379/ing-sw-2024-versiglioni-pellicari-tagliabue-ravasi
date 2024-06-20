package it.polimi.sw.GC50.model.chat;

import it.polimi.sw.GC50.model.lobby.Player;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This class defines messages that players can send to each other
 */
public class ChatMessage implements Serializable {
    private final Player sender;
    private final Player receiver;
    private final String content;
    private final String time;

    /**
     * This constructor creates a broadcast message (to all other players)
     *
     * @param sender  identify the sender of the message
     * @param content contains the text of the message
     * @param time    specify when the message is sent
     */
    public ChatMessage(Player sender, String content, LocalTime time) {
        this.sender = sender;
        this.content = content;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        this.time = time.format(formatter);
        this.receiver = null;
    }

    /**
     * This constructor creates a message between sender and receiver
     *
     * @param sender   identify the sender of the message
     * @param receiver identify the sender of the message
     * @param content  contains the text of the message
     * @param time     specify when the message is sent
     * @throws UnsupportedOperationException if someone tries to send a message to itself
     */
    public ChatMessage(Player sender, Player receiver, String content, LocalTime time) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        this.time = time.format(formatter);
    }

    /**
     * Returns the sender of the message
     *
     * @return player
     */
    public Player getSender() {
        return sender;
    }

    /**
     * Return the receiver of the message
     *
     * @return player
     */
    public Player getReceiver() {
        return receiver;
    }

    /**
     * Returns the content of the message
     *
     * @return a string
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns when the message is sent
     *
     * @return a string
     */
    public String getTime() {
        return this.time;
    }
}
