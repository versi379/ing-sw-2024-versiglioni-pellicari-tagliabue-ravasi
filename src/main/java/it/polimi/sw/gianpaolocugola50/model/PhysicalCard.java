package it.polimi.sw.gianpaolocugola50.model;

public class PhysicalCard {
    private final PlayableCard front;
    private final PlayableCard back;

    public PhysicalCard(PlayableCard front, PlayableCard back) {
        this.front = front;
        this.back = back;
    }
    public PlayableCard getFront() {
        return front;
    }
    public PlayableCard getBack() {
        return back;
    }
}
