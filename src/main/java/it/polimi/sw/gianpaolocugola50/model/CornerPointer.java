package it.polimi.sw.gianpaolocugola50.model;

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
        return corner;
    }
}
