package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.game.CardsMatrix;

public class PlayerDataView {
    private final CardsMatrix cardsMatrix;
    private final int totalScore;
    private final int objectivesScore;
    private final boolean ready;

    /**
     * constructs an instance of player data view
     *
     * @param cardsMatrix     matrix of card
     * @param totalScore      total score of a player
     * @param objectivesScore total objectives score
     * @param ready           player status
     */
    public PlayerDataView(CardsMatrix cardsMatrix, int totalScore, int objectivesScore, boolean ready) {
        this.cardsMatrix = cardsMatrix;
        this.totalScore = totalScore;
        this.objectivesScore = objectivesScore;
        this.ready = ready;
    }

    /**
     * @return card matrix
     */
    public CardsMatrix getCardsMatrix() {
        return cardsMatrix;
    }

    /**
     * @return total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * @return objective score
     */
    public int getObjectivesScore() {
        return objectivesScore;
    }

    /**
     * @return true if the player is ready
     */
    public boolean isReady() {
        return ready;
    }
}
