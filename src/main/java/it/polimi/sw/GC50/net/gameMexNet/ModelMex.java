package it.polimi.sw.GC50.net.gameMexNet;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.Resource;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.CornerPointer;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ModelMex implements Serializable {
    private final int boardSize;
    private final CornerPointer[][] cornersArea;
    private final CardsMatrix cardsArea;
    private final List<PhysicalCard> hand;
    private final Map<Resource, Integer> numOfResources;
    private ObjectiveCard secretObjective;
    private int totalScore;
    private int objectivesScore;
    private PhysicalCard starterCard;
    private List<ObjectiveCard> secretObjectivesList;

    public ModelMex(int boardSize, CornerPointer[][] cornersArea, CardsMatrix cardsArea, List<PhysicalCard> hand, Map<Resource, Integer> numOfResources, ObjectiveCard secretObjective, int totalScore, int objectivesScore, PhysicalCard starterCard, List<ObjectiveCard> secretObjectivesList) {
        this.boardSize = boardSize;
        this.cornersArea = cornersArea;
        this.cardsArea = cardsArea;
        this.hand = hand;
        this.numOfResources = numOfResources;
        this.secretObjective = secretObjective;
        this.totalScore = totalScore;
        this.objectivesScore = objectivesScore;
        this.starterCard = starterCard;
        this.secretObjectivesList = secretObjectivesList;
    }


    public int getBoardSize() {
        return boardSize;
    }

    public CornerPointer[][] getCornersArea() {
        return cornersArea;
    }

    public CardsMatrix getCardsArea() {
        return cardsArea;
    }

    public List<PhysicalCard> getHand() {
        return hand;
    }

    public Map<Resource, Integer> getNumOfResources() {
        return numOfResources;
    }

    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getObjectivesScore() {
        return objectivesScore;
    }

    public PhysicalCard getStarterCard() {
        return starterCard;
    }

    public List<ObjectiveCard> getSecretObjectivesList() {
        return secretObjectivesList;
    }
}
