package it.polimi.sw.GC50.net.messages;

public class StringMex implements Message {
    private final String string;

    /**
     * Constructs an instance of StringMex
     *
     * @param string message
     */
    public StringMex(String string) {
        this.string = string;
    }

    /**
     * @return a string
     */
    public String getString() {
        return string;
    }
}
