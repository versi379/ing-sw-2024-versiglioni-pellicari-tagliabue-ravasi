package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.*;
import it.polimi.sw.GC50.model.objective.IdenticalResourcesObjective;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.sw.GC50.model.card.CornerTest.*;
import static it.polimi.sw.GC50.model.card.PlayableCardTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerDataTest {

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

    // SETUP PHASE _____________________________________________________________________________________________________
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

        assertNull(playerData.getStarterCard());


        PhysicalCard starterCard = new PhysicalCard(CardType.STARTER, null, null);
        List<ObjectiveCard> secretObjectivesList = new ArrayList<>();
        playerData.setStartingChoices(starterCard, secretObjectivesList);

        assertEquals(starterCard, playerData.getStarterCard());
    }

    @Test
    void testGetSecretObjectivesList() {
        PlayerData playerData = new PlayerData(40);

        assertNull(playerData.getSecretObjectivesList());


        PhysicalCard starterCard = new PhysicalCard(CardType.STARTER, null, null);
        List<ObjectiveCard> secretObjectivesList = new ArrayList<>();
        playerData.setStartingChoices(starterCard, secretObjectivesList);

        assertEquals(secretObjectivesList, playerData.getSecretObjectivesList());
    }

    @Test
    void testSetSecretObjective() {
        PlayerData playerData = new PlayerData(40);
        ObjectiveCard secretObjective = new ObjectiveCard(1, new IdenticalResourcesObjective(Resource.ANIMAL, 1));
        playerData.setSecretObjective(secretObjective);

        assertEquals(secretObjective, playerData.getSecretObjective());
    }

    @Test
    void testCheckPreparationFalse() {
        PlayerData playerData = new PlayerData(40);
        PhysicalCard starterCard = new PhysicalCard(CardType.STARTER, null, null);
        List<ObjectiveCard> secretObjectivesList = new ArrayList<>();
        playerData.setStartingChoices(starterCard, secretObjectivesList);
        playerData.checkPreparation();

        assertFalse(playerData.isReady());
    }

    @Test
    void testCheckPreparationTrue() {
        PlayerData playerData = new PlayerData(40);
        PhysicalCard starterCard = new PhysicalCard(CardType.STARTER, whitePlayableCard, whitePlayableCard);
        List<ObjectiveCard> secretObjectivesList = new ArrayList<>();
        playerData.setStartingChoices(starterCard, secretObjectivesList);


        ObjectiveCard secretObjective = new ObjectiveCard(1, new IdenticalResourcesObjective(Resource.ANIMAL, 1));
        playerData.setSecretObjective(secretObjective);
        playerData.checkPreparation();

        assertFalse(playerData.isReady());


        playerData.placeCard(whitePlayableCard, 40, 40);
        playerData.checkPreparation();

        assertTrue(playerData.isReady());
    }

    @Test
    void testIsReady() {
        PlayerData playerData = new PlayerData(40);
        PhysicalCard starterCard = new PhysicalCard(CardType.STARTER, whitePlayableCard, whitePlayableCard);
        List<ObjectiveCard> secretObjectivesList = new ArrayList<>();
        playerData.setStartingChoices(starterCard, secretObjectivesList);

        assertFalse(playerData.isReady());
    }

    // BOARD MANAGEMENT ________________________________________________________________________________________________
    @Test
    void testBoardSize() {
        PlayerData playerData = new PlayerData(2);

        assertEquals(6, playerData.boardSize());
    }

    @Test
    void testGetCardsArea() {
        CardsMatrix cardsArea1 = CardsMatrixTest.testCardsMatrix();
        PlayerData playerData = new PlayerData(cardsArea1);
        CardsMatrix cardsArea2 = playerData.getCardsArea();

        for (int i = 0; i < cardsArea2.length(); i++) {
            for (int j = 0; j < cardsArea2.length(); j++) {
                assertEquals(cardsArea1.get(i, j), cardsArea2.get(i, j));
            }
        }
    }

    @Test
    void testGetCard() {
        CardsMatrix cardsArea = CardsMatrixTest.testCardsMatrix();
        PlayerData playerData = new PlayerData(cardsArea);

        for (int i = 0; i < playerData.getCardsArea().length(); i++) {
            for (int j = 0; j < playerData.getCardsArea().length(); j++) {
                assertEquals(cardsArea.getAtCornersCoordinates(i, j), playerData.getCard(i, j));
            }
        }
    }

    @Test
    void testGetTargetCornersCenter() {
        PlayerData playerData = new PlayerData(2);
        playerData.placeCard(new PlayableCard(Color.RED, 3,
                new Corner[]{hiddenCorner, emptyCorner, inkCorner, fungiCorner}), 3, 3);
        CornerPointer[] targetCorners = playerData.getTargetCorners(3, 3);

        assertEquals(hiddenCorner, targetCorners[0].getCorner());
        assertEquals(emptyCorner, targetCorners[1].getCorner());
        assertEquals(inkCorner, targetCorners[2].getCorner());
        assertEquals(fungiCorner, targetCorners[3].getCorner());
    }

    @Test
    void testGetTargetCornersEdge() {
        PlayerData playerData = new PlayerData(2);
        playerData.placeCard(new PlayableCard(Color.RED, 3,
                new Corner[]{hiddenCorner, emptyCorner, inkCorner, fungiCorner}), 4, 4);
        CornerPointer[] targetCorners = playerData.getTargetCorners(5, 5);

        assertEquals(inkCorner, targetCorners[0].getCorner());
        assertFalse(targetCorners[1].isPresent());
        assertFalse(targetCorners[2].isPresent());
        assertFalse(targetCorners[3].isPresent());
    }

    @Test
    void testIsPositionValidOutOfBounds() {
        CardsMatrix cardsArea = CardsMatrixTest.testCardsMatrix();
        PlayerData playerData = new PlayerData(cardsArea);

        assertFalse(playerData.isPositionValid(100, 100));
    }

    @Test
    void testIsPositionValidOccupiedPosition() {
        CardsMatrix cardsArea = CardsMatrixTest.testCardsMatrix();
        PlayerData playerData = new PlayerData(cardsArea);
        playerData.placeCard(whitePlayableCard, 2, 2);

        assertFalse(playerData.isPositionValid(2, 2));
    }

    @Test
    void testIsPositionValidOddPosition() {
        CardsMatrix cardsArea = CardsMatrixTest.testCardsMatrix();
        PlayerData playerData = new PlayerData(cardsArea);
        playerData.placeCard(whitePlayableCard, 2, 2);

        assertFalse(playerData.isPositionValid(2, 3));
    }

    @Test
    void testIsPositionValidNoCoveredCorner() {
        CardsMatrix cardsArea = CardsMatrixTest.testCardsMatrix();
        PlayerData playerData = new PlayerData(cardsArea);

        assertFalse(playerData.isPositionValid(3, 1));
    }

    @Test
    void testIsPositionValidTrue() {
        CardsMatrix cardsArea = CardsMatrixTest.testCardsMatrix();
        PlayerData playerData = new PlayerData(cardsArea);
        playerData.placeCard(whitePlayableCard, 2, 2);

        assertTrue(playerData.isPositionValid(3, 1));
    }

    @Test
    void testPlaceCard() {
        PlayerData playerData = new PlayerData(2);
        playerData.placeCard(new PlayableCard(Color.RED, 3,
                new Corner[]{hiddenCorner, emptyCorner, inkCorner, fungiCorner}), 2, 2);

        assertEquals(3, playerData.getTotalScore());


        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.PLANT));
        playerData.placeCard(new PlayableCard(Color.RED, 2, new CoveredCornersBonus(), fixedResources,
                new Corner[]{hiddenCorner, emptyCorner, inkCorner, fungiCorner}), 3, 3);

        assertEquals(5, playerData.getTotalScore());
    }

    @Test
    void testNumOfResource() {
        PlayerData playerData = new PlayerData(2);
        playerData.placeCard(new PlayableCard(Color.RED, 3,
                new Corner[]{hiddenCorner, emptyCorner, insectCorner, fungiCorner}), 2, 2);

        assertEquals(1, playerData.numOfResource(Resource.INSECT));


        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.PLANT));
        playerData.placeCard(new PlayableCard(Color.RED, 3, fixedResources,
                new Corner[]{hiddenCorner, emptyCorner, inkCorner, fungiCorner}), 3, 3);

        assertEquals(3, playerData.numOfResource(Resource.FUNGI));
        assertEquals(1, playerData.numOfResource(Resource.INK));
        assertEquals(1, playerData.numOfResource(Resource.PLANT));
        assertEquals(0, playerData.numOfResource(Resource.INSECT));
    }

    // HAND MANAGEMENT _________________________________________________________________________________________________
    @Test
    void testAddCard() {
    }

    @Test
    void testRemoveCard() {
    }

    @Test
    void testGetHand() {
        PlayerData playerData = new PlayerData(2);

        assertEquals(new ArrayList<>(), playerData.getHand());


        PhysicalCard card = new PhysicalCard(CardType.RESOURCE, redPlayableCard, redPlayableCard);
        playerData.addCard(card);

        assertEquals(new ArrayList<>(List.of(card)), playerData.getHand());
    }

    // SCORE MANAGEMENT ________________________________________________________________________________________________
    @Test
    void getTotalScore() {
    }

    @Test
    void getObjectivesScore() {
    }

    @Test
    void getSecretObjective() {
    }

    @Test
    void objectiveIncrement() {
    }

    @Test
    void setFinalScore() {
    }

    @Test
    void setObjectivesScore() {
    }
}
