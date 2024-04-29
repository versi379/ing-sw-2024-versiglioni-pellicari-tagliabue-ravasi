package it.polimi.sw.GC50.net.socket;

import com.google.gson.Gson;
import it.polimi.sw.GC50.net.Message;
import it.polimi.sw.GC50.net.Request;

public class MessageSCK extends Message {
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
