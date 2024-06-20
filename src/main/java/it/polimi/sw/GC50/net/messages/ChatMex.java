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
     * @return message's sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return message's receiver
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * @return content of the message
     */
    public String getContent() {
        return content;
    }

    /**
     * @return  time when message is sent
     */
    public String getTime() {
        return time;
    }
}
