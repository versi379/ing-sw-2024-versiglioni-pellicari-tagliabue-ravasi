package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.chat.ChatMessage;

public class ChatMex implements Message {
    private final String sender;
    private final String receiver;
    private final String content;
    private final String time;

    public ChatMex(ChatMessage chatMessage) {
        this.sender = chatMessage.getSender().getNickname();
        this.receiver = chatMessage.getReceiver() != null ? chatMessage.getReceiver().getNickname() : null;
        this.content = chatMessage.getContent();
        this.time = chatMessage.getTime();
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
