package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.cards.Color;
import it.polimi.sw.GC50.model.cards.Resource;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objectives.*;
import it.polimi.sw.GC50.view.TUI.TuiModelPrinter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

public class PrintTUITest {

    @Test
    void testPrintBoard() {
        Player player = new Player("Player");
        Game game = new Game(1, 20);
        game.addPlayer(player);

        System.out.println();
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

        TuiModelPrinter.printPlayerArea(game.getCardsArea(player));


        System.out.println();
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 40, 40);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 41, 41);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 40, 42);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 39, 41);
        game.placeCard(player, game.pickCard(DrawingPosition.GOLDDECK).getFront(), 39, 39);

        TuiModelPrinter.printPlayerArea(game.getCardsArea(player));


        System.out.println();
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        game.placeCard(player, game.pickCard(DrawingPosition.RESOURCEDECK).getFront(), 0, 0);

        TuiModelPrinter.printPlayerArea(game.getCardsArea(player));
    }

    @Test
    void testCardToStringTUI() {
        Game game = new Game(1, 20);
        game.addPlayer(new Player("Player"));
        while (game.goldDeckSize() > 0) {
            String[][] card = game.pickCard(DrawingPosition.GOLDDECK).getFront().toStringTUI();
            for (int i = 6; i >= 0; i--) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(card[j][i]);
                }
                System.out.println();
            }
        }
        for (int k = 0; k < 5; k++) {
            String[][] card = game.pickStarterCard().getFront().toStringTUI();
            for (int i = card[0].length - 1; i >= 0; i--) {
                for (int j = 0; j < card.length; j++) {
                    System.out.print(card[j][i]);
                }
                System.out.println();
            }
        }
    }

    @Test
    void testObjectiveToStringTUI() {
        Objective objective;

        objective = new IdenticalResourcesObjective(Resource.PLANT, 5);
        System.out.println(objective.toStringTUI());
        System.out.println();

        objective = new DifferentResourcesObjective(new HashSet<>(Arrays.asList(Resource.PLANT, Resource.FUNGI, Resource.SCROLL)));
        System.out.println(objective.toStringTUI());
        System.out.println();

        objective = new MonolithObjective(Color.RED, MonolithOrientation.RIGHTDIAGONAL);
        System.out.println(objective.toStringTUI());
        System.out.println();

        objective = new MonolithObjective(Color.RED, MonolithOrientation.LEFTDIAGONAL);
        System.out.println(objective.toStringTUI());
        System.out.println();

        objective = new CaveObjective(Color.RED, Color.BLUE, CaveOrientation.UPRIGHTL);
        System.out.println(objective.toStringTUI());
        System.out.println();

        objective = new CaveObjective(Color.RED, Color.BLUE, CaveOrientation.UPRIGHTJ);
        System.out.println(objective.toStringTUI());
        System.out.println();

        objective = new CaveObjective(Color.RED, Color.BLUE, CaveOrientation.INVERTEDL);
        System.out.println(objective.toStringTUI());
        System.out.println();

        objective = new CaveObjective(Color.RED, Color.BLUE, CaveOrientation.INVERTEDJ);
        System.out.println(objective.toStringTUI());
        System.out.println();

        ObjectiveCard objectiveCard = new ObjectiveCard(2, objective);
        System.out.println(objectiveCard.toStringTUI());
    }
}
