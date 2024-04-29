package it.polimi.sw.GC50.net.util;

import com.google.gson.Gson;

public class Message {
    private final Request request;
    private final Gson gson;



    public Message(Request request, Gson gson) {
        this.request = request;
        this.gson = gson;
    }
    public Message(Request request, String string) {
        this.request = request;
        this.gson = new Gson();
        this.gson.toJson(string);
    }
    public Request getRequest() {
        return request;
    }

    public Gson getGson() {
        return gson;
    }
    public String getGsonToString(){
        return gson.toString();
    }

    public static class MessageSCK extends Message {
        private final int matchCode,clientCode;

        public MessageSCK(Request request, Gson gson, int matchCode, int clientCode) {
            super(request, gson);
            this.matchCode = matchCode;
            this.clientCode = clientCode;
        }

        public MessageSCK(Request request, String string, int matchCode, int clientCode) {
            super(request, string);
            this.matchCode = matchCode;
            this.clientCode = clientCode;
        }

        public int getMatchCode() {
            return matchCode;
        }

        public int getClientCode() {
            return clientCode;
        }
    }
}
