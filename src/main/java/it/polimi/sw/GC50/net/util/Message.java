package it.polimi.sw.GC50.net.util;


import java.io.Serializable;

public class Message implements Serializable {
    private final Request request;
    private final Object object;


    public Message(Request request, Object object) {
        this.request = request;
        this.object = object;
    }

    public Message(Request request, String string) {
        this.request = request;
        this.object = string;
    }

    public Request getRequest() {
        return request;
    }

    public Object getObject() {
        return object;
    }


    public static class MessageClientToServer extends Message {
        private final String matchName, nickName;

        public MessageClientToServer(Request request, Object ob, String matchName, String nickName) {
            super(request, ob);
            this.matchName = matchName;
            this.nickName = nickName;
        }

        public String getMatchName() {
            return matchName;
        }

        public String getNickName() {
            return nickName;
        }
    }
}
