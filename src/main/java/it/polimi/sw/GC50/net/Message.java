package it.polimi.sw.GC50.net;

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
}
