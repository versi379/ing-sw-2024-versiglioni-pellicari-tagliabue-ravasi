package it.polimi.sw.gianpaolocugola50.model.chat;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import it.polimi.sw.gianpaolocugola50.model.game.Player;

/**
 * This class is for the player chat so that the players can send each other messages while playing
 */
public class Chat {

    @Expose
    private final List<Message> messages;

    public Chat() {
        this.messages = new ArrayList<Message>();
    }

    /**
     * This method stores every message sent in an ArrayList
     */
    public void addMessage(Message message/*,VirtualView... virtualViews*/){
        this.messages.add(message);
        // update view through a Model-View event
    }

    /**
     * This method gives the player all the messages that he sends and that are sent to him
     */
    public List<Message> getMessages(Player player) {
        List<Message> copyMessages = new ArrayList<Message>();
        // messages where sender is (input) player
        copyMessages.addAll(this.messages.stream().filter(t->t.getSender().equals(player)).toList());
        // messages where receiver is null (broadcast)
        copyMessages.addAll(this.messages.stream().filter(t-> t.getReceiver() == null).toList());
        // messages where receiver is (input) player
        copyMessages.addAll(this.messages.stream().filter(t->(t.getReceiver() != null && t.getReceiver().equals(player))).toList());
        return copyMessages;
    }
    
}
