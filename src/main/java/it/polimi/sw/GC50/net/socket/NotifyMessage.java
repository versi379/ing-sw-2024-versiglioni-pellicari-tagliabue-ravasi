package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.Messages.Message;
import it.polimi.sw.GC50.net.util.Notify;

import java.io.Serializable;

public class NotifyMessage implements Serializable {
    private final Notify notify;
    private final Message content;

    public NotifyMessage(Notify notify, Message content) {
        this.notify = notify;
        this.content = content;
    }

    public Notify getNotify() {
        return notify;
    }

    public Message getContent() {
        return content;
    }
}
