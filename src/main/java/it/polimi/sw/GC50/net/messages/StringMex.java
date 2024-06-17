package it.polimi.sw.GC50.net.messages;

public class StringMex implements Message {
    private final String string;

    public StringMex(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}