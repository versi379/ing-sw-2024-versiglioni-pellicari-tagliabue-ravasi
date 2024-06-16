package it.polimi.sw.GC50.model.objectives;

import java.io.Serializable;

/**
 * Possible cave orientations
 */
public enum CaveOrientation implements Serializable {
    UPRIGHTL, UPRIGHTJ, INVERTEDL, INVERTEDJ;

    public String toStringTUI() {
        return switch (this) {
            case UPRIGHTL -> "upright L";
            case UPRIGHTJ -> "upright J";
            case INVERTEDL -> "inverted L";
            case INVERTEDJ -> "inverted J";
        };
    }
}
