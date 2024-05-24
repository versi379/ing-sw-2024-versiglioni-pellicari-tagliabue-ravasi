package it.polimi.sw.GC50.net.util;


import java.io.Serializable;

public class Message1 implements Serializable {
    private final Notify notify;
    private final Object object;


    public Message1(Notify notify, Object object) {
        this.notify = notify;
        this.object = object;
    }

    public Message1(Notify notify, String string) {
        this.notify = notify;
        this.object = string;
    }

    public Notify getRequest() {
        return notify;
    }

    public Object getObject() {
        return object;
    }


    public static class Message1ClientToServer extends Message1 {
        private final String matchName, nickName;

        public Message1ClientToServer(Notify notify, Object ob, String matchName, String nickName) {
            super(notify, ob);
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
