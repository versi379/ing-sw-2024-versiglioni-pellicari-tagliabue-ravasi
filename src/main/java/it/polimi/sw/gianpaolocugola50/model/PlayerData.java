package it.polimi.sw.gianpaolocugola50.model;
import java.util.*;

public class PlayerData {
    private final CornerPointer[][] cornersArea;
    private final PlayableCard[][] cardsArea;
    private int score;
    private final Map<Resource, Integer> numOfResources;
    private final ObjectiveCard secretObjective;
    private final PhysicalCard[] hand;

    public PlayerData(PlayableCard starterCard) {
        cornersArea = new CornerPointer[82][82];
        cardsArea = new PlayableCard[82][82];
        score = 0;
        numOfResources = new EnumMap<>(Resource.class);
        for(Resource resource : Resource.values()) {
            numOfResources.put(resource, 0);
        }
        this.placeCard(starterCard, 40, 40);
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
        if(x < 0 || x >= 80 || y < 0 || y >= 80) {
            return false;
        }
        boolean cornerRequirement = false;
        CornerPointer[] targetCorners = this.getTargetCorners(x, y);
        for(CornerPointer cornerPointer : targetCorners) {
            if(cornerPointer.getCorner().isVisible()) {
                cornerRequirement = true;
            }
        }
        return (x + y) % 2 == 0 &&
                cardsArea[x][y] == null &&
                cornerRequirement;
    }

    public void placeCard(PlayableCard card, int x, int y) {
        int coveredCorners = 0;
        CornerPointer[] targetCorners = this.getTargetCorners(x, y);

        for(CornerPointer cornerPointer : targetCorners) {
            if (cornerPointer != null) {
                coveredCorners++;
                if (cornerPointer.getCorner().isFull()) {
                    numOfResources.replace(cornerPointer.getCorner().getResource(),
                            numOfResources.get(cornerPointer.getCorner().getResource()) - 1);
                }
            }
        }
        targetCorners[0].setCorner(card.getSwCorner());
        targetCorners[1].setCorner(card.getNwCorner());
        targetCorners[2].setCorner(card.getNeCorner());
        targetCorners[3].setCorner(card.getSeCorner());
        cardsArea[x][y] = card;
        for(Resource resource : Resource.values()) {
            numOfResources.replace(resource, numOfResources.get(resource) + card.resourceCount(resource));
        }
        score += card.scoreIncrement(this, coveredCorners);
    }
}
