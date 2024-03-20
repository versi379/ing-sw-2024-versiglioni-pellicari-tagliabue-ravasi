package it.polimi.sw.gianpaolocugola50.model.game;

import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;
import it.polimi.sw.gianpaolocugola50.model.card.Resource;

import java.util.*;

public class PlayerData {
    // da rivedere la staticità dell'attributo (possibili espansioni che cambiano la dimensione dei mazzi)
    public static final int MATRIX_LENGTH = 84;
    private final CornerPointer[][] cornersArea;
    private final CardsMatrix cardsArea;
    private int score;
    private final Map<Resource, Integer> numOfResources;
    //da riprendere
    //private final ObjectiveCard secretObjective;
    //private final PhysicalCard[] hand;

    public PlayerData(PlayableCard starterCard) {
        cornersArea = new CornerPointer[MATRIX_LENGTH][MATRIX_LENGTH];
        for (int i = 0; i < MATRIX_LENGTH; i++) {
            for (int j = 0; j < MATRIX_LENGTH; j++) {
                cornersArea[i][j] = new CornerPointer();
            }
        }
        cardsArea = new CardsMatrix(MATRIX_LENGTH);
        score = 0;
        numOfResources = new EnumMap<>(Resource.class);
        for (Resource resource : Resource.values()) {
            numOfResources.put(resource, 0);
        }
        this.placeCard(starterCard, (MATRIX_LENGTH / 2) - 1, (MATRIX_LENGTH / 2) - 1);
    }

    public CardsMatrix getCardsArea() {
        return cardsArea;
    }

    public int numOfResource(Resource resource) {
        return numOfResources.get(resource);
    }

    public CornerPointer[] getTargetCorners(int x, int y) {
        CornerPointer[] result = new CornerPointer[4];

        result[0] = (cornersArea[x][y]);
        result[1] = (cornersArea[x][y + 1]);
        result[2] = (cornersArea[x + 1][y + 1]);
        result[3] = (cornersArea[x + 1][y]);
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
        CornerPointer[] targetCorners = this.getTargetCorners(x, y);

        for (CornerPointer cornerPointer : targetCorners) {
            if (cornerPointer.isPresent() && cornerPointer.getCorner().isFull()) {
                numOfResources.replace(cornerPointer.getCorner().getResource(),
                        numOfResources.get(cornerPointer.getCorner().getResource()) - 1);
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
        score += card.scoreIncrement(this, x, y);
    }
}
