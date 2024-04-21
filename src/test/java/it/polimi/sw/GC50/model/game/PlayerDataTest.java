package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.*;
import it.polimi.sw.GC50.model.objective.IdenticalResourcesObjective;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDataTest {

    @Test
    void testPlayerDataConstructor() {
        PlayerData playerData = new PlayerData(40);
        assertEquals(82, playerData.boardSize());

        for (int i = 0; i < playerData.boardSize() - 1; i = i + 2) {
            for (int j = 0; j < playerData.boardSize() - 1; j = j + 2) {
                assertNull(playerData.getCard(i, j));
                for (CornerPointer cornerPointer : playerData.getTargetCorners(i, j)) {
                    assertFalse(cornerPointer.isPresent());
                }
            }
        }

        for (Resource resource : Resource.values()) {
            assertEquals(0, playerData.numOfResource(resource));
        }
        assertEquals(0, playerData.getTotalScore());
        assertEquals(0, playerData.getObjectivesScore());
    }

    @Test
    void testSetStartingChoices() {
        PlayerData playerData = new PlayerData(40);
        PhysicalCard starterCard = new PhysicalCard(CardType.STARTER, null, null);
        List<ObjectiveCard> secretObjectivesList = new ArrayList<>();
        playerData.setStartingChoices(starterCard, secretObjectivesList);
        assertEquals(starterCard, playerData.getStarterCard());
        assertEquals(secretObjectivesList, playerData.getSecretObjectivesList());
    }

    @Test
    void testGetStarterCard() {
        PlayerData playerData = new PlayerData(40);
        PhysicalCard starterCard = new PhysicalCard(CardType.STARTER, null, null);
        List<ObjectiveCard> secretObjectivesList = new ArrayList<>();
        playerData.setStartingChoices(starterCard, secretObjectivesList);
        assertEquals(starterCard, playerData.getStarterCard());
    }

    @Test
    void testGetSecretObjectivesList() {
        PlayerData playerData = new PlayerData(40);
        PhysicalCard starterCard = new PhysicalCard(CardType.STARTER, null, null);
        List<ObjectiveCard> secretObjectivesList = new ArrayList<>();
        playerData.setStartingChoices(starterCard, secretObjectivesList);
        assertEquals(secretObjectivesList, playerData.getSecretObjectivesList());
    }

    @Test
    void setSecretObjective() {
        PlayerData playerData = new PlayerData(40);
        ObjectiveCard secretObjective = new ObjectiveCard(1, new IdenticalResourcesObjective(Resource.ANIMAL, 1));
        playerData.setSecretObjective(secretObjective);
        assertEquals(secretObjective, playerData.getSecretObjective());
    }

    @Test
    void checkPreparation() {
    }

    @Test
    void isReady() {
    }

    @Test
    void getCardsArea() {
    }

    @Test
    void numOfResource() {
    }

    @Test
    void getTargetCorners() {
    }

    @Test
    void isPositionValid() {
    }

    @Test
    void placeCard() {
    }

    @Test
    void addCard() {
    }

    @Test
    void removeCard() {
    }

    @Test
    void getHand() {
    }

    @Test
    void getTotalScore() {
    }

    @Test
    void getObjectivesScore() {
    }

    @Test
    void setFinalScore() {
    }

    @Test
    void setObjectivesScore() {
    }

    @Test
    void objectiveIncrement() {
    }

    @Test
    void printCornersArea() {
    }
}