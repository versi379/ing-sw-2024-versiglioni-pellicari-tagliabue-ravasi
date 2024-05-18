package it.polimi.sw.GC50.model.card;

import java.io.Serializable;

/**
 * Different types of PlayableCard colors
 */
public enum Color implements Serializable {
    WHITE, GREEN, BLUE, RED, PURPLE;

    /**
     * Returns the String representing this color's code in the TUI graphics
     *
     * @return String[][] containing the code associated with this color
     */
    public String toStringTUI() {
        return switch (this) {
            case WHITE -> "\u001B[37m";
            case GREEN -> "\u001B[32m";
            case BLUE -> "\u001B[34m";
            case RED -> "\u001B[31m";
            case PURPLE -> "\u001B[35m";
        };
    }
}
