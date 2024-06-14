package it.polimi.sw.GC50.net.Requests;
import java.io.Serializable;

public class PlaceCardRequest implements Serializable {
    private final int index;
    private final int face;
    private final int x;
    private final int y;

    public PlaceCardRequest(int index, int face, int x, int y) {
        this.index = index;
        this.face = face;
        this.x = x;
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public int getFace() {
        return face;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
