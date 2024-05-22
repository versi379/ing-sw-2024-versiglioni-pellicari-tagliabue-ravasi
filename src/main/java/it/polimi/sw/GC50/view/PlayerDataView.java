package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.game.CardsMatrix;

import java.io.Serializable;

public class PlayerDataView {
    private final CardsMatrix cardsMatrix;
    private final int totalScore;
    private final int objectivesScore;

    public PlayerDataView(CardsMatrix cardsMatrix, int totalScore, int objectivesScore) {
        this.cardsMatrix = cardsMatrix;
        this.totalScore = totalScore;
        this.objectivesScore = objectivesScore;
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
