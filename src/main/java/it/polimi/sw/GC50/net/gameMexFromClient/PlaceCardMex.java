package it.polimi.sw.GC50.net.gameMexFromClient;

import java.io.Serializable;

public class PlaceCardMex implements Serializable {
    private int indexinhand;
    private boolean face;
    private int x;
    private int y;

    public PlaceCardMex(boolean face, int indexinhand, int x, int y) {
        this.face = face;
        this.indexinhand = indexinhand;
        this.x = x;
        this.y = y;
    }

    public int getIndexinhand() {
        return indexinhand;
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
