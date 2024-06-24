package it.polimi.sw.GC50.net.requests;

import java.io.Serializable;

public class ChatMessageRequest implements Serializable {
    private final String receiver;
    private final String content;

    /**
     * Constructs an instance of ChatMessageRequest with
     *
     * @param receiver nickname of receiver
     * @param content  content of the message
     */
    public ChatMessageRequest(String receiver, String content) {
        this.receiver = receiver;
        this.content = content;
    }

    /**
     * Constructs an instance of ChatMessageRequest with
     *
     * @param content content of the message
     */
    public ChatMessageRequest(String content) {
        receiver = null;
        this.content = content;
    }

    /**
     * @return nickname of the receiver
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
}
