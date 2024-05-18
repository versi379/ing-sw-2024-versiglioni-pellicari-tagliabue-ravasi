package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.game.CardsMatrix;

public class PlayerArea1 {
    private final String nickname;
    private final Color color;
    private final CardsMatrix cardsMatrix;
    private int points;

    public PlayerArea1(String nickname, Color color, CardsMatrix cardsMatrix, int points) {
        this.nickname = nickname;
        this.color = color;
        this.cardsMatrix = cardsMatrix.copy();
        this.points = points;
    }

    public String getNickname() {
        return nickname;
    }

    public Color getColor() {
        return color;
    }

    public CardsMatrix getCardsMatrix() {
        return cardsMatrix;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
