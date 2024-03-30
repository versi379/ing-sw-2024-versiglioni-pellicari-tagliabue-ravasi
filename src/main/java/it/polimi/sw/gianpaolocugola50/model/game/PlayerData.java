package it.polimi.sw.gianpaolocugola50.model.game;

import it.polimi.sw.gianpaolocugola50.model.card.Corner;
import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;
import it.polimi.sw.gianpaolocugola50.model.card.Resource;
import it.polimi.sw.gianpaolocugola50.model.card.PhysicalCard;
import it.polimi.sw.gianpaolocugola50.model.objective.ObjectiveCard;

import java.util.*;

public class PlayerData {
    private final int MATRIX_LENGTH;
    private final CornerPointer[][] cornersArea;
    private final CardsMatrix cardsArea;
    private int totalScore;
    private int objectivesScore;
    private final Map<Resource, Integer> numOfResources;
    private ObjectiveCard secretObjective;
    private final List<PhysicalCard> hand;

    public PlayerData(int deckSize) {
        MATRIX_LENGTH = 2 * deckSize + 2;
        cornersArea = new CornerPointer[MATRIX_LENGTH][MATRIX_LENGTH];
        for (int i = 0; i < MATRIX_LENGTH; i++) {
            for (int j = 0; j < MATRIX_LENGTH; j++) {
                cornersArea[i][j] = new CornerPointer();
            }
        }
        cardsArea = new CardsMatrix(MATRIX_LENGTH);
        totalScore = 0;
        objectivesScore = 0;
        numOfResources = new EnumMap<>(Resource.class);
        for (Resource resource : Resource.values()) {
            numOfResources.put(resource, 0);
        }
        secretObjective = null; // da rivedere
        hand = new ArrayList<>();
    }

    public void initialize(PlayableCard starterCard, ObjectiveCard secretObjective) {
        placeCard(starterCard, (MATRIX_LENGTH / 2) - 1, (MATRIX_LENGTH / 2) - 1);
        this.secretObjective = secretObjective;
    }

    public CardsMatrix getCardsArea() {
        return cardsArea.copy();
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getObjectivesScore() {
        return objectivesScore;
    }

    public int numOfResource(Resource resource) {
        return numOfResources.get(resource);
    }

    public CornerPointer[] getTargetCorners(int x, int y) {
        CornerPointer[] result = new CornerPointer[4];

        result[0] = (cornersArea[x][y]);
        result[1] = (y < MATRIX_LENGTH - 1) ? cornersArea[x][y + 1] : new CornerPointer();
        result[2] = (x < MATRIX_LENGTH - 1 && y < MATRIX_LENGTH - 1) ? cornersArea[x + 1][y + 1] : new CornerPointer();
        result[3] = (x < MATRIX_LENGTH - 1) ? cornersArea[x + 1][y] : new CornerPointer();
        return result;
    }

    public boolean isPositionValid(int x, int y) {
        if (x < 0 || x >= MATRIX_LENGTH - 2 || y < 0 || y >= MATRIX_LENGTH - 2) {
            return false;
        }
        CornerPointer[] targetCorners = this.getTargetCorners(x, y);
        for (CornerPointer cornerPointer : targetCorners) {
            if (cornerPointer.isPresent() && cornerPointer.getCorner().isVisible()) {
                return (x + y) % 2 == 0 &&
                        cardsArea.getAtCornersCoordinates(x, y) == null;
            }
        }
        return false;
    }

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
        cardsArea.insertAtCornersCoordinates(card, x, y);
        for (Resource resource : Resource.values()) {
            numOfResources.replace(resource, numOfResources.get(resource) + card.resourceCount(resource));
        }
    }

    private void unitaryDecrement(Resource resource) {
        numOfResources.replace(resource, numOfResources.get(resource) - 1);
    }

    public void addCard(PhysicalCard card) {
        hand.add(card);
    }

    public void removeCard(int index) {
        hand.remove(index);
    }

    public List<PhysicalCard> getHand() {
        return hand;
    }

    public int objectiveIncrement(ObjectiveCard objectiveCard) {
        return objectiveCard.checkObjective(this);
    }

    //test
    public void printCornersArea() {
        System.out.println("_____________________________________________________________________________________________");
        for (int j = 47; j > 35; j--) {
            for (int i = 35; i < 47; i++) {
                Corner x = cornersArea[i][j].getCorner();
                if (x != null) {
                    if(x.isFull()) {
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
}
