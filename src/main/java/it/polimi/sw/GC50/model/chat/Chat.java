package it.polimi.sw.GC50.model.chat;

import it.polimi.sw.GC50.model.lobby.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is for the player chat so that the players can send each other messages while playing
 */
public class Chat implements Serializable {


    private final List<ChatMessage> chatMessages;

    public Chat() {
        this.chatMessages = new ArrayList<ChatMessage>();
    }

    /**
     *  This method stores every message sent in an ArrayList
     * @param chatMessage   message associated with ChatMessage
     */
    public void addMessage(ChatMessage chatMessage){
        this.chatMessages.add(chatMessage);
    }

    /**
     * This method gives the player all the messages that he sends and that are sent to him
     * @param player    specify the player that receive messages
     * @return a copy of chat messages
     */
    public List<ChatMessage> getMessages(Player player) {
        List<ChatMessage> copyChatMessages = new ArrayList<ChatMessage>();
        // messages where sender is (input) player
        copyChatMessages.addAll(this.chatMessages.stream().filter(t->t.getSender().equals(player)).toList());
        // messages where receiver is null (broadcast)
        copyChatMessages.addAll(this.chatMessages.stream().filter(t-> t.getReceiver() == null).toList());
        // messages where receiver is (input) player
        copyChatMessages.addAll(this.chatMessages.stream().filter(t->(t.getReceiver() != null && t.getReceiver().equals(player))).toList());
        return copyChatMessages;
    }
}
