package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.Corner;

import java.io.Serializable;

/**
 * Represents the status of corner in thw board (present or not),
 * and stores the associated Corner object (with further corner info)
 */
public class CornerPointer implements Serializable {
    private boolean present;
    private Corner corner;

    public CornerPointer() {
        present = false;
        corner = null;
    }

    public boolean isPresent() {
        return present;
    }

    public void setCorner(Corner corner) {
        present = true;
        this.corner = corner;
    }

    public Corner getCorner() {
        return isPresent() ? corner : null;
    }

}
