package it.polimi.sw.GC50.net.Messages;

import it.polimi.sw.GC50.model.chat.ChatMessage;

public class ChatMex implements Message {
    private final ChatMessage chatMessage;

    public ChatMex(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }
}
