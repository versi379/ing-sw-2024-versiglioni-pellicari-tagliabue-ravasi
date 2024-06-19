package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.chat.ChatMessage;

/**
 * class that manages chat messages
 */
public class ChatMex implements Message {
    private final String sender;
    private final String receiver;
    private final String content;
    private final String time;

    /**
     * Constructs an instance of ChatMex
     * @param chatMessage a message of a chat
     */
    public ChatMex(ChatMessage chatMessage) {
        this.sender = chatMessage.getSender().getNickname();
        this.receiver = chatMessage.getReceiver() != null ? chatMessage.getReceiver().getNickname() : null;
        this.content = chatMessage.getContent();
        this.time = chatMessage.getTime();
    }

    /**
     * Returns message's sender
     * @return
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns message's receiver
     * @return
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Returns content of the message
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns time when message is sent
     * @return
     */
    public String getTime() {
        return time;
    }
}
