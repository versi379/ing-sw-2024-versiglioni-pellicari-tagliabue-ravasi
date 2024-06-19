package it.polimi.sw.GC50.model.objectives;

import java.io.Serializable;

/**
 * Possible monolith orientations
 */
public enum MonolithOrientation implements Serializable {
    RIGHTDIAGONAL, LEFTDIAGONAL;

    public String toStringTUI() {
        return switch (this) {
            case RIGHTDIAGONAL -> "diagonal left to right (\\)";
            case LEFTDIAGONAL -> "diagonal right to left (/)";
        };
    }
}
