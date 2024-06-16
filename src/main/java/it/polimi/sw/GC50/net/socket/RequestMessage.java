package it.polimi.sw.GC50.net.socket;

import java.io.Serializable;

public class RequestMessage implements Serializable {
    private final Request request;
    private final Object content;

    public RequestMessage(Request request, Object content) {
        this.request = request;
        this.content = content;
    }

    public Request getRequest() {
        return request;
    }

    public Object getContent() {
        return content;
    }
}
