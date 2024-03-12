package it.polimi.sw.gianpaolocugola50.model;

public abstract class PlayableCard {
    private Color color;
    private Corner sw;
    private Corner nw;
    private Corner ne;
    private Corner se;

    public Color getColor() {
        return color;
    }

    public Corner getSw() {
        return sw;
    }

    public Corner getNw() {
        return nw;
    }

    public Corner getNe() {
        return ne;
    }

    public Corner getSe() {
        return se;
    }
}
