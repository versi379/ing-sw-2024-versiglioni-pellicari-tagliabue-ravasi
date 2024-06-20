package trash;

import it.polimi.sw.GC50.model.chat.ChatMessage;
import it.polimi.sw.GC50.model.lobby.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is for the player chat so that the players can send each other messages while playing
 */
public class Chat {
    private final List<ChatMessage> chatMessages;

    public Chat() {
        chatMessages = new ArrayList<>();
    }

    /**
     *  This method stores every message sent in an ArrayList
     * @param chatMessage   message associated with ChatMessage
     */
    public void addMessage(ChatMessage chatMessage){
        chatMessages.add(chatMessage);
    }

    /**
     * This method gives the player all the messages that he sends and that are sent to him
     * @param player    specify the player that receive messages
     * @return a copy of chat messages
     */
    public List<ChatMessage> getMessages(Player player) {
        List<ChatMessage> copyChatMessages = new ArrayList<>();
        // messages where sender is (input) player
        copyChatMessages.addAll(chatMessages.stream().filter(t->t.getSender().equals(player)).toList());
        // messages where receiver is null (broadcast)
        copyChatMessages.addAll(chatMessages.stream().filter(t-> t.getReceiver() == null).toList());
        // messages where receiver is (input) player
        copyChatMessages.addAll(chatMessages.stream().filter(t->(t.getReceiver() != null && t.getReceiver().equals(player))).toList());
        return copyChatMessages;
    }
}
