package it.polimi.sw.GC50.net.socket;

import java.io.Serializable;

public class RequestMessage implements Serializable {
    private final Request request;
    private final Object content;

    /**
     * Constructs an instance of RequestMessage
     *
     * @param request of the message
     * @param content of the message
     */
    public RequestMessage(Request request, Object content) {
        this.request = request;
        this.content = content;
    }

    /**
     * @return message request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * @return content of the message
     */
    public Object getContent() {
        return content;
    }
}
