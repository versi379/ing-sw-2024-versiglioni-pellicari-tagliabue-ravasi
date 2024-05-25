package it.polimi.sw.GC50.net.util;

import java.io.Serializable;

public class ChatMessageRequest implements Serializable {
    private final String receiver;
    private final String content;

    public ChatMessageRequest(String receiver, String content) {
        this.receiver = receiver;
        this.content = content;
    }

    public ChatMessageRequest(String content) {
        receiver = null;
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }
}
