package it.polimi.sw.GC50.net.gameMexNet;

import java.io.Serializable;

public class PlaceCardMex implements Serializable {
    private final int index;
    private final boolean face;
    private final int x;
    private final int y;

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
