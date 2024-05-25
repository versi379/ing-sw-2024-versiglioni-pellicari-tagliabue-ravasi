package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.Corner;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.card.Resource;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Comprises game info with respect to a player, e.g. board, hand, score
 */
public class PlayerData {

    private final int boardSize;
    private final CornerPointer[][] cornersArea;
    private final CardsMatrix cardsArea;
    private final List<PhysicalCard> hand;
    private final Map<Resource, Integer> numOfResources;
    private ObjectiveCard secretObjective;
    private int totalScore;
    private int objectivesScore;

    /**
     * Utilized for setup phase
     */
    private boolean ready;
    private PhysicalCard starterCard;
    private List<ObjectiveCard> secretObjectivesList;

    /**
     * Constructor to build player area
     * @param deckSize size of the deck
     */
    public PlayerData(int deckSize) {
        boardSize = (deckSize * 2) + 2;

        cornersArea = new CornerPointer[boardSize()][boardSize()];
        for (int i = 0; i < boardSize(); i++) {
            for (int j = 0; j < boardSize(); j++) {
                cornersArea[i][j] = new CornerPointer();
            }
        }
        cardsArea = new CardsMatrix(boardSize() - 1);
        hand = new ArrayList<>();

        numOfResources = new EnumMap<>(Resource.class);
        for (Resource resource : Resource.values()) {
            numOfResources.put(resource, 0);
        }
        secretObjective = null;
        totalScore = 0;
        objectivesScore = 0;
    }

    // SETUP PHASE /////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * given a starter card and a list of secret objective sets a starting choice
     * @param starterCard
     * @param secretObjectivesList
     */
    public void setStartingChoices(PhysicalCard starterCard, List<ObjectiveCard> secretObjectivesList) {
        ready = false;
        this.starterCard = starterCard;
        this.secretObjectivesList = secretObjectivesList;
    }

    /**
     * @return a starter card
     */
    public PhysicalCard getStarterCard() {
        return starterCard;
    }

    /*
     * @return a secret objective list
     */
    public List<ObjectiveCard> getSecretObjectivesList() {
        return secretObjectivesList;
    }

    /**
     * Sets a secret objective list
     * @param secretObjective
     */
    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }

    /**
     * verify when a player become ready
     * (a player is ready when choose a starter card or an objective)
     */
    public void checkPreparation() {
        if (getCard((boardSize() / 2) - 1, (boardSize() / 2) - 1) != null && secretObjective != null) {
            ready = true;
        }
    }

    /**
     * Verify if a player is ready
     * @return a boolean ( true if is ready)
     */
    public boolean isReady() {
        return ready;
    }

    // BOARD MANAGEMENT ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return board size
     */
    public int boardSize() {
        return boardSize;
    }

    /**
     *
     * @return a copy of card area
     */
    public CardsMatrix getCardsArea() {
        return cardsArea.copy();
    }

    /**
     * given x, y coordinates shows where a card is placed
     * @param x X coordinates
     * @param y Y coordinates
     * @return the place of a card
     */
    public PlayableCard getCard(int x, int y) {
        return cardsArea.get(x, y);
    }

    /**
     *
     * @param x X coordinates
     * @param y Y coordinates
     * @return card's corner
     */
    public CornerPointer[] getTargetCorners(int x, int y) {
        CornerPointer[] result = new CornerPointer[4];

        result[0] = (cornersArea[x][y]);
        result[1] = (y < boardSize() - 1) ? cornersArea[x][y + 1] : new CornerPointer();
        result[2] = (x < boardSize() - 1 && y < boardSize() - 1) ? cornersArea[x + 1][y + 1] : new CornerPointer();
        result[3] = (x < boardSize() - 1) ? cornersArea[x + 1][y] : new CornerPointer();
        return result;
    }

    /**
     * Given x, y coordinates verify if the card is placed correctly
     * @param x X coordinates
     * @param y Y coordinates
     * @return
     */
    public boolean isPositionValid(int x, int y) {
        if (x < 0 || x >= boardSize() - 1 || y < 0 || y >= boardSize() - 1) {
            return false;
        }
        if ((x + y) % 2 != 0) {
            return false;
        }
        if (getCard(x, y) != null) {
            return false;
        }
        CornerPointer[] targetCorners = this.getTargetCorners(x, y);
        for (CornerPointer cornerPointer : targetCorners) {
            if (cornerPointer.isPresent() && cornerPointer.getCorner().isVisible()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Puts a card in a specific position
     * @param card  card played
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void placeCard(PlayableCard card, int x, int y) {
        totalScore += card.scoreIncrement(this, x, y);

        CornerPointer[] targetCorners = this.getTargetCorners(x, y);
        for (CornerPointer cornerPointer : targetCorners) {
            if (cornerPointer.isPresent() && cornerPointer.getCorner().isFull()) {
                unitaryDecrement(cornerPointer.getCorner().getResource());
            }
        }

        targetCorners[0].setCorner(card.getSwCorner());
        targetCorners[1].setCorner(card.getNwCorner());
        targetCorners[2].setCorner(card.getNeCorner());
        targetCorners[3].setCorner(card.getSeCorner());
        cardsArea.insert(card, x, y);
        for (Resource resource : Resource.values()) {
            numOfResources.replace(resource, numOfResources.get(resource) + card.resourceCount(resource));
        }
    }

    /**
     * Given a specific resource returns number of that resource
     * @param resource
     * @return
     */
    public int numOfResource(Resource resource) {
        return numOfResources.get(resource);
    }

    /**
     * Subtract 1 to numOfResource
     * @param resource
     */
    private void unitaryDecrement(Resource resource) {
        numOfResources.replace(resource, numOfResources.get(resource) - 1);
    }

    // HAND MANAGEMENT /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * add a Card to a hand
     * @param card
     */
    public void addCard(PhysicalCard card) {
        hand.add(card);
    }

    /**
     * remove a card from a hand
     * @param index index of the card that be removed
     */
    public void removeCard(int index) {
        hand.remove(index);
    }

    /**
     *
     * @return a list of card from a hand
     */
    public List<PhysicalCard> getHand() {
        return hand;
    }

    // SCORE MANAGEMENT ////////////////////////////////////////////////////////////////////////////////////////////////

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

     * @return secret objective
     */
    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    /**
     * Given a objective card return incremented score
     * @param objectiveCard
     * @return
     */
    public int objectiveIncrement(ObjectiveCard objectiveCard) {
        return objectiveCard.checkObjective(this);
    }

    /**
     * Given common objectives set the final score
     * @param commonObjectives
     */
    public void setFinalScore(List<ObjectiveCard> commonObjectives) {
        commonObjectives.add(secretObjective);
        setObjectivesScore(commonObjectives);
        totalScore += getObjectivesScore();
    }

    /**
     * Given objectives set the objective score
     * @param objectives
     */
    public void setObjectivesScore(List<ObjectiveCard> objectives) {
        objectivesScore = 0;

        for (ObjectiveCard objectiveCard : objectives) {
            objectivesScore += objectiveIncrement(objectiveCard);
        }
    }

    // TEST METHODS ////////////////////////////////////////////////////////////////////////////////////////////////////
    public void printCornersArea() {
        System.out.println("_____________________________________________________________________________________________");
        for (int j = 47; j > 35; j--) {
            for (int i = 35; i < 47; i++) {
                Corner x = cornersArea[i][j].getCorner();
                if (x != null) {
                    if (x.isFull()) {
                        System.out.print(x.getResource().toString() + "\t");
                    } else if (x.isVisible()) {
                        System.out.print("EMPTY\t");
                    } else {
                        System.out.print("HIDDEN\t");
                    }
                } else {
                    System.out.print("null\t");
                }
            }
            System.out.print("\n");
        }
    }

    /**
     * Constructor used for testing different game situations and patterns
     *
     * @param customCardsArea
     */
    public PlayerData(CardsMatrix customCardsArea) {
        boardSize = customCardsArea.length();
        cornersArea = new CornerPointer[boardSize()][boardSize()];
        for (int i = 0; i < boardSize(); i++) {
            for (int j = 0; j < boardSize(); j++) {
                cornersArea[i][j] = new CornerPointer();
            }
        }
        cardsArea = customCardsArea.copy();
        hand = new ArrayList<>();

        numOfResources = new EnumMap<>(Resource.class);
        for (Resource resource : Resource.values()) {
            numOfResources.put(resource, 0);
        }
        secretObjective = null;
        totalScore = 0;
        objectivesScore = 0;
    }

    public void setNumOfResources(Map<Resource, Integer> numOfResources) {
        for (Resource resource : Resource.values()) {
            this.numOfResources.put(resource, numOfResources.get(resource));
        }
    }

    public CornerPointer[][] getCornersArea() {
        return cornersArea;
    }

    public Map<Resource, Integer> getNumOfResources() {
        return numOfResources;
    }
}
