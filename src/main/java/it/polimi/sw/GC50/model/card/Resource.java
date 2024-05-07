package it.polimi.sw.GC50.model.card;

import java.io.Serializable;

/**
 * Different types of game resources
 */
public enum Resource implements Serializable {
    PLANT, ANIMAL, FUNGI, INSECT, QUILL, SCROLL, INK;

    public String toStringTUI() {
        return switch (this) {
            case PLANT -> "\u001B[32mP\u001B[0m";
            case ANIMAL -> "\u001B[34mA\u001B[0m";
            case FUNGI -> "\u001B[31mF\u001B[0m";
            case INSECT -> "\u001B[35mI\u001B[0m";
            case QUILL -> "\u001B[33mQ\u001B[0m";
            case SCROLL -> "\u001B[33mS\u001B[0m";
            case INK -> "\u001B[33mI\u001B[0m";
        };
    }
}
