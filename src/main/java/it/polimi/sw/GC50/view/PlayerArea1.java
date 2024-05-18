package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;

public class PlayerArea1 {
    private final String nickname;
    private final Color color;
    private final ObjectiveCard secretObjective;
    private final CardsMatrix cardsMatrix;
    private final PhysicalCard[] hand;
    private int points;

    public PlayerArea1(String nickname, Color color, CardsMatrix cardsMatrix, ObjectiveCard secretObjective) {
        this.nickname = nickname;
        this.color = color;
        this.secretObjective = secretObjective;
        this.cardsMatrix = cardsMatrix.copy();
        hand = new PhysicalCard[3];
        points = 0;
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
