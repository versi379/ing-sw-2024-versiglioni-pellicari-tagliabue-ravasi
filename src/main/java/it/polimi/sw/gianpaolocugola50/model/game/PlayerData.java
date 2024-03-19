package it.polimi.sw.gianpaolocugola50.model.game;
import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;
import it.polimi.sw.gianpaolocugola50.model.card.Resource;
import it.polimi.sw.gianpaolocugola50.model.card.Color;
import it.polimi.sw.gianpaolocugola50.model.game.CornerPointer;

import java.util.*;

public class PlayerData {
    private static final int MATRIX_LENGTH = 84;
    private final CornerPointer[][] cornersArea;
    private final PlayableCard[][] cardsArea;
    private int score;
    private final Map<Resource, Integer> numOfResources;
    //da riprendere
    //private final ObjectiveCard secretObjective;
    //private final PhysicalCard[] hand;

    public PlayerData(PlayableCard starterCard) {
        cornersArea = new CornerPointer[MATRIX_LENGTH][MATRIX_LENGTH];
        for(int i = 0; i < MATRIX_LENGTH; i++) {
            for(int j = 0; j < MATRIX_LENGTH; j++) {
                cornersArea[i][j] = new CornerPointer();
            }
        }
        cardsArea = new PlayableCard[MATRIX_LENGTH][MATRIX_LENGTH];
        score = 0;
        numOfResources = new EnumMap<>(Resource.class);
        for(Resource resource : Resource.values()) {
            numOfResources.put(resource, 0);
        }
        this.placeCard(starterCard, (MATRIX_LENGTH/2)-1, (MATRIX_LENGTH/2)-1);
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
        if(x < 0 || x >= MATRIX_LENGTH - 2 || y < 0 || y >= MATRIX_LENGTH - 2) {
            return false;
        }
        CornerPointer[] targetCorners = this.getTargetCorners(x, y);
        for(CornerPointer cornerPointer : targetCorners) {
            if(cornerPointer.isPresent() && cornerPointer.getCorner().isVisible()) {
                return (x + y) % 2 == 0 &&
                        cardsArea[x][y] == null;
            }
        }
        return false;
    }

    public void placeCard(PlayableCard card, int x, int y) {
        CornerPointer[] targetCorners = this.getTargetCorners(x, y);

        for(CornerPointer cornerPointer : targetCorners) {
            if (cornerPointer.isPresent() && cornerPointer.getCorner().isFull()) {
                numOfResources.replace(cornerPointer.getCorner().getResource(),
                        numOfResources.get(cornerPointer.getCorner().getResource()) - 1);
            }
        }
        targetCorners[0].setCorner(card.getSwCorner());
        targetCorners[1].setCorner(card.getNwCorner());
        targetCorners[2].setCorner(card.getNeCorner());
        targetCorners[3].setCorner(card.getSeCorner());
        cardsArea[cornersToCardsX(x, y)][cornersToCardsY(x, y)] = card;
        for(Resource resource : Resource.values()) {
            numOfResources.replace(resource, numOfResources.get(resource) + card.resourceCount(resource));
        }
        score += card.scoreIncrement(this, x, y);
    }

    private static int cornersToCardsX(int x, int y) {
        return (x + y) / 2;
    }
    private static int cornersToCardsY(int x, int y) {
        return (MATRIX_LENGTH - 2 - x + y) / 2;
    }
    public PlayableCard[] getNearCards(int x, int y) {
        PlayableCard[] result = new PlayableCard[4];

        result[0] = cardsArea[cornersToCardsX(x, y) - 1][cornersToCardsY(x, y)];
        result[1] = cardsArea[cornersToCardsX(x, y)][cornersToCardsY(x, y) + 1];
        result[2] = cardsArea[cornersToCardsX(x, y) + 1][cornersToCardsY(x, y)];
        result[3] = cardsArea[cornersToCardsX(x, y)][cornersToCardsY(x, y) - 1];
        return result;
    }
}
