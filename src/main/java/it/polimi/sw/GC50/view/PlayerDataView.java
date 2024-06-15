package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.game.CardsMatrix;

import java.io.Serializable;

public class PlayerDataView {
    private final CardsMatrix cardsMatrix;
    private final int totalScore;
    private final int objectivesScore;
    private final boolean ready;

    public PlayerDataView(CardsMatrix cardsMatrix, int totalScore, int objectivesScore, boolean ready) {
        this.cardsMatrix = cardsMatrix;
        this.totalScore = totalScore;
        this.objectivesScore = objectivesScore;
        this.ready = ready;
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
    public boolean isReady() {
        return ready;
    }
}
