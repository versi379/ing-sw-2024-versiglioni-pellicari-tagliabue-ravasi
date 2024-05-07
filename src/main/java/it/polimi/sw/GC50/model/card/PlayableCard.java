package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Stream.concat;

/**
 * Represents either front or back of a game card
 */
public class PlayableCard implements Serializable {

    private final Color color;

    /**
     * Represents the number at the top of the card
     * (not necessarily the score obtained by the player when placing it)
     */
    private final int points;

    /**
     * Represents the condition regarding the player's score
     * increment obtained when placing the card,
     * computed as points * bonus.checkBonus(...)
     */
    private final Bonus bonus;

    /**
     * Represents the list of resources present in the center of the card
     */
    private final List<Resource> fixedResources;

    /**
     * Represents the corners of the card (always initialized as an array of length 4)
     * In order, it contains the SW, NW, NE and SE corners
     */
    private final Corner[] corners;

    public PlayableCard(Color color, int points, Bonus bonus, List<Resource> fixedResources, Corner[] corners) {
        this.color = color;
        this.points = points;
        this.bonus = bonus;
        this.fixedResources = fixedResources;
        this.corners = corners.clone();
    }

    /**
     * Constructor for cards without bonus
     */
    public PlayableCard(Color color, int points, List<Resource> fixedResources, Corner[] corners) {
        this.color = color;
        this.points = points;
        this.bonus = new BlankBonus();
        this.fixedResources = fixedResources;
        this.corners = corners.clone();
    }

    /**
     * Constructor for cards without bonus and central resource
     */
    public PlayableCard(Color color, int points, Corner[] corners) {
        this.color = color;
        this.points = points;
        this.bonus = new BlankBonus();
        this.fixedResources = new ArrayList<>();
        this.corners = corners.clone();
    }

    public Color getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public List<Resource> getFixedResources() {
        return new ArrayList<>(fixedResources);
    }

    public List<Corner> getCorners() {
        return new ArrayList<>(Arrays.asList(corners));
    }

    public Corner getSwCorner() {
        return corners[0];
    }

    public Corner getNwCorner() {
        return corners[1];
    }

    public Corner getNeCorner() {
        return corners[2];
    }

    public Corner getSeCorner() {
        return corners[3];
    }

    public int resourceCount(Resource targetResource) {
        return (int) concat(Arrays.stream(corners).map(Corner::getResource), fixedResources.stream())
                .filter(targetResource::equals)
                .count();
    }

    public boolean isPlaceable(PlayerData board, int x, int y) {
        return board.isPositionValid(x, y);
    }

    public int scoreIncrement(PlayerData board, int x, int y) {
        return points * bonus.checkBonus(this, board, x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PlayableCard playableCard)) {
            return false;
        }
        return getColor().equals(playableCard.getColor()) &&
                getPoints() == playableCard.getPoints() &&
                getBonus().equals(playableCard.getBonus()) &&
                getFixedResources().equals(playableCard.getFixedResources()) &&
                getCorners().equals(playableCard.getCorners());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor(), getPoints(), getBonus(), getFixedResources(), getCorners());
    }

    public String[][] toStringTUI() {
        String[][] card = new String[3][7];
        // 7 righe e 3 colonne;

        String colorStart = switch (getColor()) {
            case WHITE -> "\u001B[37m";
            case GREEN -> "\u001B[32m";
            case BLUE -> "\u001B[34m";
            case RED -> "\u001B[31m";
            case PURPLE -> "\u001B[35m";
        };
        String colorEnd = "\u001B[0m";


        // SW corner
        if (getSwCorner().isVisible()) {
            String x = (getSwCorner().isFull())
                    ? colorEnd + getSwCorner().getResource().toStringTUI() + colorStart : " ";
            card[0][2] = "╠═════╗";
            card[0][1] = "║  " + x + "  ║";
            card[0][0] = "╚═════╝";
        } else {
            card[0][2] = "║      ";
            card[0][1] = "║      ";
            card[0][0] = "╚══════";
        }

        // NW corner
        if (getNwCorner().isVisible()) {
            String x = (getNwCorner().isFull())
                    ? colorEnd + getNwCorner().getResource().toStringTUI() + colorStart : " ";
            card[0][6] = "╔═════╗";
            card[0][5] = "║  " + x + "  ║";
            card[0][4] = "╠═════╝";
        } else {
            card[0][6] = "╔══════";
            card[0][5] = "║      ";
            card[0][4] = "║      ";
        }

        // NE corner
        if (getNeCorner().isVisible()) {
            String x = (getNeCorner().isFull())
                    ? colorEnd + getNeCorner().getResource().toStringTUI() + colorStart : " ";
            card[2][6] = "╔═════╗";
            card[2][5] = "║  " + x + "  ║";
            card[2][4] = "╚═════╣";
        } else {
            card[2][6] = "══════╗";
            card[2][5] = "      ║";
            card[2][4] = "      ║";
        }

        // SE corner
        if (getSeCorner().isVisible()) {
            String x = (getSeCorner().isFull())
                    ? colorEnd + getSeCorner().getResource().toStringTUI() + colorStart : " ";
            card[2][2] = "╔═════╣";
            card[2][1] = "║  " + x + "  ║";
            card[2][0] = "╚═════╝";
        } else {
            card[2][2] = "      ║";
            card[2][1] = "      ║";
            card[2][0] = "══════╝";
        }

        // TOP
        String points = (getPoints() > 0) ? colorEnd + getPoints() + colorStart : " ";
        String bonus = getBonus().toStringTUI();
        card[1][6] = "═══════";
        card[1][5] = "  " + points + " " + bonus + "  ";
        card[1][4] = "       ";

        // LEFT
        card[0][3] = "║      ";

        // RIGHT
        card[2][3] = "      ║";

        // BOTTOM
        card[1][2] = "       ";
        card[1][1] = "       ";
        card[1][0] = "═══════";

        // CENTER
        String fixedResourcesString = "";
        int emptySpaces = 7;
        for (Resource resource : getFixedResources()) {
            fixedResourcesString += resource.toStringTUI();
            emptySpaces--;
        }
        String leftPadding = "";
        String rightPadding = "";
        for (int i = 0; i < emptySpaces; i++) {
            if (i % 2 == 0) {
                rightPadding += " ";
            } else {
                leftPadding += " ";
            }
        }
        card[1][3] = leftPadding + fixedResourcesString + rightPadding;


        for (int i = 0; i < card.length; i++) {
            for (int j = 0; j < card[i].length; j++) {
                card[i][j] = colorStart + card[i][j] + colorEnd;
            }
        }
        return card;
    }
}
