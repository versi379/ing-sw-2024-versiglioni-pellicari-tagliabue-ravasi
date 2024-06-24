package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.messages.Message;
import it.polimi.sw.GC50.net.messages.Notify;

import java.io.Serializable;

public class NotifyMessage implements Serializable {
    private final Notify notify;
    private final Message content;

    /**
     * Constructs an instance of NotifyMessage
     *
     * @param notify  notify of the message
     * @param content content of the message
     */
    public NotifyMessage(Notify notify, Message content) {
        this.notify = notify;
        this.content = content;
    }

    /**
     * @return notify
     */
    public Notify getNotify() {
        return notify;
    }

    /**
     * @return content of the message
     */
    public Message getContent() {
        return content;
    }
}
