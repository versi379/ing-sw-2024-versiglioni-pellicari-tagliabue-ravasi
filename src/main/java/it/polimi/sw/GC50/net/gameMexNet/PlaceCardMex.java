package it.polimi.sw.GC50.net.gameMexNet;

import java.io.Serializable;

public class PlaceCardMex implements Serializable {
    private int index;
    private boolean face;
    private int x;
    private int y;

    public PlaceCardMex(int index, boolean face, int x, int y) {
        this.index = index;
        this.face = face;
        this.x = x;
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public boolean isFace() {
        return face;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
