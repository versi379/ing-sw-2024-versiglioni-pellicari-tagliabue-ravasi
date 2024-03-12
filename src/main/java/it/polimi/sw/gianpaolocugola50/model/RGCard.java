package it.polimi.sw.gianpaolocugola50.model;

public abstract class RGCard extends PlayableCard {
    private int score;

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
