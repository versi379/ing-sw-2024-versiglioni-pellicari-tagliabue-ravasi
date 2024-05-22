package it.polimi.sw.GC50.net.util;


import java.io.Serializable;

public class Message1 implements Serializable {
    private final Request request;
    private final Object object;


    public Message1(Request request, Object object) {
        this.request = request;
        this.object = object;
    }

    public Message1(Request request, String string) {
        this.request = request;
        this.object = string;
    }

    public Request getRequest() {
        return request;
    }

    public Object getObject() {
        return object;
    }


    public static class Message1ClientToServer extends Message1 {
        private final String matchName, nickName;

        public Message1ClientToServer(Request request, Object ob, String matchName, String nickName) {
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
