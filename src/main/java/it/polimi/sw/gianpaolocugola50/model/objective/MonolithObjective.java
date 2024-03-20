package it.polimi.sw.gianpaolocugola50.model.objective;

import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;
import it.polimi.sw.gianpaolocugola50.model.game.CardsMatrix;
import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;
import it.polimi.sw.gianpaolocugola50.model.card.Color;

import java.util.Optional;

public class MonolithObjective implements Objective {
    private final Color targetColor;
    private final MonolithOrientation orientation;

    public MonolithObjective(Color targetColor, MonolithOrientation orientation) {
        this.targetColor = targetColor;
        this.orientation = orientation;
    }

    @Override
    public int checkCondition(PlayerData playerData) {
        CardsMatrix cardsArea = playerData.getCardsArea();
        int result = 0;
        int currentCount;

        for (int i = 0; i < PlayerData.MATRIX_LENGTH; i++) {
            currentCount = 0;
            for (int j = 0; j < PlayerData.MATRIX_LENGTH; j++) {
                if (targetColor.equals(getFromCardsArea(cardsArea, i, j)
                        .map(PlayableCard::getColor)
                        .orElse(null))) {
                    currentCount++;
                }
                if (currentCount == 3) {
                    result++;
                    currentCount = 0;
                }
            }
        }
        return result;
    }

    private Optional<PlayableCard> getFromCardsArea(CardsMatrix cardsArea, int x, int y) {
        switch (orientation) {
            case MonolithOrientation.LEFTDIAGONAL -> {
                return Optional.of(cardsArea.getAtTransposeCoordinates(x, y));
            }
            case MonolithOrientation.RIGHTDIAGONAL -> {
                return Optional.of(cardsArea.getAtNaturalCoordinates(x, y));
            }
            default -> {
                return Optional.empty();
            }
        }
    }
}
