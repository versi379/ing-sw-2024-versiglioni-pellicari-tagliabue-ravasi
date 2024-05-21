package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.game.CardsMatrix;

import java.io.Serializable;

public class PlayerDataView implements Serializable {
    private final CardsMatrix cardsMatrix;
    private int totalScore;
    private int objectivesScore;

    public PlayerDataView(CardsMatrix cardsMatrix, int totalScore) {
        this.cardsMatrix = cardsMatrix.copy();
        this.totalScore = totalScore;
    }

    public CardsMatrix getCardsMatrix() {
        return cardsMatrix;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getObjectivesScore() {
        return objectivesScore;
    }
}
