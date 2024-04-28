package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.*;
import it.polimi.sw.GC50.model.objective.*;
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
        PhysicalCard starterCard = new PhysicalCard(CardType.STARTER, whitePlayableCard, whitePlayableCard);
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
        PlayerData playerData = new PlayerData(CardsMatrixTest.testCardsMatrix());

        assertFalse(playerData.isPositionValid(100, 100));
    }

    @Test
    void testIsPositionValidOccupiedPosition() {
        PlayerData playerData = new PlayerData(CardsMatrixTest.testCardsMatrix());
        playerData.placeCard(whitePlayableCard, 2, 2);

        assertFalse(playerData.isPositionValid(2, 2));
    }

    @Test
    void testIsPositionValidOddPosition() {
        PlayerData playerData = new PlayerData(CardsMatrixTest.testCardsMatrix());
        playerData.placeCard(whitePlayableCard, 2, 2);

        assertFalse(playerData.isPositionValid(2, 3));
    }

    @Test
    void testIsPositionValidNoCoveredCorner() {
        PlayerData playerData = new PlayerData(CardsMatrixTest.testCardsMatrix());

        assertFalse(playerData.isPositionValid(3, 1));
    }

    @Test
    void testIsPositionValidTrue() {
        PlayerData playerData = new PlayerData(CardsMatrixTest.testCardsMatrix());
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
        PlayerData playerData = new PlayerData(2);

        assertEquals(new ArrayList<>(), playerData.getHand());


        PhysicalCard redCard = new PhysicalCard(CardType.RESOURCE, redPlayableCard, redPlayableCard);
        PhysicalCard blueCard = new PhysicalCard(CardType.RESOURCE, bluePlayableCard, bluePlayableCard);
        PhysicalCard greenCard = new PhysicalCard(CardType.RESOURCE, greenPlayableCard, greenPlayableCard);
        playerData.addCard(redCard);
        playerData.addCard(blueCard);
        playerData.addCard(greenCard);

        assertEquals(new ArrayList<>(Arrays.asList(redCard, blueCard, greenCard)), playerData.getHand());
    }

    @Test
    void testRemoveCard() {
        PlayerData playerData = new PlayerData(2);
        PhysicalCard redCard = new PhysicalCard(CardType.RESOURCE, redPlayableCard, redPlayableCard);
        PhysicalCard blueCard = new PhysicalCard(CardType.RESOURCE, bluePlayableCard, bluePlayableCard);
        PhysicalCard greenCard = new PhysicalCard(CardType.RESOURCE, greenPlayableCard, greenPlayableCard);
        playerData.addCard(redCard);
        playerData.addCard(blueCard);
        playerData.addCard(greenCard);
        playerData.removeCard(1);

        assertEquals(new ArrayList<>(Arrays.asList(redCard, greenCard)), playerData.getHand());
    }

    // SCORE MANAGEMENT ________________________________________________________________________________________________
    @Test
    void testObjectiveIncrement() {
        PlayerData playerData = new PlayerData(CaveObjectiveTest.uprightLMatrix());
        ObjectiveCard objective = new ObjectiveCard(3,
                new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.UPRIGHTL));

        assertEquals(3, playerData.objectiveIncrement(objective));
    }

    @Test
    void testSetFinalScore() {
        PlayerData playerData = new PlayerData(CaveObjectiveTest.uprightLMatrix());
        playerData.placeCard(greenPlayableCard, 0, 0);
        ObjectiveCard objective = new ObjectiveCard(3,
                new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.UPRIGHTL));
        playerData.setSecretObjective(objective);
        List<ObjectiveCard> commonObjectives = new ArrayList<>(Arrays.asList(objective, objective));
        playerData.setFinalScore(commonObjectives);

        assertEquals(9 + greenPlayableCard.getPoints(), playerData.getTotalScore());
    }

    @Test
    void testSetObjectivesScore() {
        PlayerData playerData = new PlayerData(CaveObjectiveTest.uprightLMatrix());
        playerData.placeCard(greenPlayableCard, 0, 0);
        ObjectiveCard objective = new ObjectiveCard(3,
                new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.UPRIGHTL));
        playerData.setSecretObjective(objective);
        List<ObjectiveCard> commonObjectives = new ArrayList<>(Arrays.asList(objective, objective));
        playerData.setFinalScore(commonObjectives);

        assertEquals(9, playerData.getObjectivesScore());
    }
}
