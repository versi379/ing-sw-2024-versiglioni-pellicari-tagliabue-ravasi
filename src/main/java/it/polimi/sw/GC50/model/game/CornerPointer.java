package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.Corner;

public class CornerPointer {
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
