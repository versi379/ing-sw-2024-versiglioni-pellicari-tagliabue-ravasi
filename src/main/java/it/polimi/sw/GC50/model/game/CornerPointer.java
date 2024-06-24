package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.cards.Corner;

/**
 * Represents the status of corner in thw board (present or not),
 * and stores the associated Corner object (with further corner info)
 */
public class CornerPointer {
    private boolean present;
    private Corner corner;

    /**
     * constructs a new cornerPointer instance
     */
    public CornerPointer() {
        present = false;
        corner = null;
    }

    /**
     * @return present if the corner is present
     */
    public boolean isPresent() {
        return present;
    }

    /**
     * Set a new corner
     *
     * @param corner represents corner of the card
     */
    public void setCorner(Corner corner) {
        present = true;
        this.corner = corner;
    }

    /**
     * @return corner if isPresent() returns true
     */
    public Corner getCorner() {
        return isPresent() ? corner : null;
    }
}
