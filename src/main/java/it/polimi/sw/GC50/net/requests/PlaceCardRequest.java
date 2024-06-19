package it.polimi.sw.GC50.net.requests;
import java.io.Serializable;

public class PlaceCardRequest implements Serializable {
    private final int index;
    private final int face;
    private final int x;
    private final int y;

    /**
     * Constructs an instance of PlaceCardRequest
     * @param index         index of the card
     * @param face          face of the card(front or back)
     * @param x             X coordinates in the matrix
     * @param y             Y coordinates in the matrix
     */
    public PlaceCardRequest(int index, int face, int x, int y) {
        this.index = index;
        this.face = face;
        this.x = x;
        this.y = y;
    }

    /**
     * @return index of the card
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return face of the card
     */
    public int getFace() {
        return face;
    }

    /**
     *
     * @return X coordinates in the matrix
     */
    public int getX() {
        return x;
    }
    /**
     *
     * @return Y coordinates in the matrix
     */
    public int getY() {
        return y;
    }
}
