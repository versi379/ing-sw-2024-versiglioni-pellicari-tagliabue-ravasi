package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.nio.file.attribute.UserPrincipalLookupService;
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

    public String[][] toStringTui() {
        String[][] card = new String[7][3];
        //7 righe e 3 colonne;
        StringBuilder[][] cardtmp = new StringBuilder[7][3];

        ArrayList<StringBuilder> sb = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                cardtmp[i][j] = new StringBuilder();
            }
        }
        String colorstart = "";
        String colorend = "";


        colorend = ("\u001B[0m");

        if (this.color.equals(Color.GREEN)) {
            colorstart = ("\u001B[32m");


        }
        if (this.color.equals(Color.BLUE)) {
            colorstart = ("\u001B[34m");

        }
        if (this.color.equals(Color.RED)) {
            colorstart = ("\u001B[31m");

        }
        if (this.color.equals(Color.PURPLE)) {
            colorstart = ("\u001B[35m");


        }


        //corner nw
        if (this.getNwCorner().isVisible()) {
            String x = " ";
            if (this.getNwCorner().isFull()) {
                x = getResString(this.getNwCorner().getResource());
            }
            cardtmp[0][0].append(colorstart);
            cardtmp[0][0].append("╔═════╦");
            cardtmp[0][0].append(colorend);

            cardtmp[1][0].append(colorstart);
            cardtmp[1][0].append("║  ");
            cardtmp[1][0].append(colorend);
            cardtmp[1][0].append(x);
            cardtmp[1][0].append(colorstart);
            cardtmp[1][0].append("  ║");
            cardtmp[1][0].append(colorend);

            cardtmp[2][0].append(colorstart);
            cardtmp[2][0].append("╠═════╝");
            cardtmp[2][0].append(colorend);

        } else {
            cardtmp[0][0].append(colorstart);
            cardtmp[1][0].append(colorstart);
            cardtmp[2][0].append(colorstart);
            cardtmp[0][0].append("╔══════");
            cardtmp[1][0].append("║      ");
            cardtmp[2][0].append("║      ");
            cardtmp[0][0].append(colorend);
            cardtmp[1][0].append(colorend);
            cardtmp[2][0].append(colorend);

        }
        ////up center
        cardtmp[0][1].append(colorstart);
        cardtmp[1][1].append(colorstart);
        cardtmp[2][1].append(colorstart);
        cardtmp[0][1].append("═══════");
        cardtmp[1][1].append("  ");
        cardtmp[2][1].append("       ");

        if (this.points > 0) {
            cardtmp[1][1].append(colorend);
            cardtmp[1][1].append("\u001B[37m");
            cardtmp[1][1].append(this.points);


        } else {
            cardtmp[1][1].append(" ");
        }
        cardtmp[1][1].append(" ");

        /////////bonus

        if (this.bonus.getClass().getSimpleName().equals("BlankBonus")) {
            cardtmp[1][1].append(" ");

        } else if (this.bonus.getClass().getSimpleName().equals("CoveredCornersBonus")) {
            cardtmp[1][1].append("C");
        } else if (this.bonus.getClass().getSimpleName().equals("ResourcesBonus")) {
            ResourcesBonus bonustmp = (ResourcesBonus) this.bonus;
            cardtmp[1][1].append(colorend);
            cardtmp[1][1].append(getResString((bonustmp.getTargetResource())));
            cardtmp[1][1].append(colorstart);
        }
        cardtmp[1][1].append("  ");
        cardtmp[1][1].append(colorend);

        cardtmp[0][2].append(colorstart);
        cardtmp[1][2].append(colorstart);
        cardtmp[2][2].append(colorstart);

        if (this.getNeCorner().isVisible()) {
            String x = " ";
            if (this.getNeCorner().isFull()) {
                x = getResString(this.getNeCorner().getResource());
            }
            cardtmp[0][2].append("╦═════╗");
            cardtmp[1][2].append("║  ");
            cardtmp[1][2].append(colorend);
            cardtmp[1][2].append(x);
            cardtmp[1][2].append(colorstart);
            cardtmp[1][2].append("  ║");
            cardtmp[2][2].append("╚═════╣");

        } else {
            cardtmp[0][2].append("══════╗");
            cardtmp[1][2].append("      ║");
            cardtmp[2][2].append("      ║");

        }
        cardtmp[0][2].append(colorend);
        cardtmp[1][2].append(colorend);
        cardtmp[2][2].append(colorend);


        ///CENTER
        cardtmp[3][0].append(colorstart);

        if (this.fixedResources.isEmpty()) {
            cardtmp[3][0].append("║                   ║");
        } else {
            cardtmp[3][0].append("║       ");
            cardtmp[3][0].append(colorend);
            for (Resource res : this.fixedResources) {
                cardtmp[3][0].append(getResString(res));
            }
            cardtmp[3][0].append(colorstart);
            for (int i = 0; i < 12 - this.fixedResources.size(); i++) {
                cardtmp[3][0].append(" ");
            }
            cardtmp[3][0].append("║");


        }
        cardtmp[3][0].append(colorend);
        cardtmp[3][1].append("");
        cardtmp[3][2].append("");

///sud
        cardtmp[4][0].append(colorstart);
        cardtmp[5][0].append(colorstart);
        cardtmp[6][0].append(colorstart);

        if (this.getSwCorner().isVisible()) {
            String x = " ";
            if (this.getNwCorner().isFull()) {
                x = getResString(this.getNwCorner().getResource());
            }

            cardtmp[4][0].append("╠═════╗");
            cardtmp[5][0].append("║  ");
            cardtmp[5][0].append(colorend);
            cardtmp[5][0].append(x);
            cardtmp[5][0].append(colorstart);
            cardtmp[5][0].append("  ║");
            cardtmp[6][0].append("╚═════╩");
        } else {

            cardtmp[4][0].append("║      ");
            cardtmp[5][0].append("║      ");
            cardtmp[6][0].append("╚══════");
        }
        cardtmp[4][0].append(colorend);
        cardtmp[5][0].append(colorend);
        cardtmp[6][0].append(colorend);

        ////////down center
        cardtmp[4][1].append(colorstart);
        cardtmp[5][1].append(colorstart);
        cardtmp[6][1].append(colorstart);
        cardtmp[4][1].append("       ");


        cardtmp[5][1].append("       ");


        cardtmp[6][1].append("═══════");
        cardtmp[4][1].append(colorend);
        cardtmp[5][1].append(colorend);
        cardtmp[6][1].append(colorend);


        //////
        cardtmp[4][2].append(colorstart);
        cardtmp[5][2].append(colorstart);
        cardtmp[6][2].append(colorstart);

        if (this.getSeCorner().isVisible()) {
            String x = " ";
            if (this.getNeCorner().isFull()) {

                x = getResString(this.getNeCorner().getResource());

            }
            cardtmp[4][2].append("╔═════╣");
            cardtmp[5][2].append("║  ");
            cardtmp[5][2].append(colorend);
            cardtmp[5][2].append(x);
            cardtmp[5][2].append(colorstart);
            cardtmp[5][2].append("  ║");
            cardtmp[6][2].append("╩═════╝");

        } else {
            cardtmp[4][2].append("      ║");
            cardtmp[5][2].append("      ║");
            cardtmp[6][2].append("══════╝");


        }
        cardtmp[4][2].append(colorend);
        cardtmp[5][2].append(colorend);
        cardtmp[6][2].append(colorend);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                card[i][j] = cardtmp[i][j].toString();

            }


        }

        return card;
    }

    private String getResString(Resource res) {
        switch (res) {


            case ANIMAL:
                return new String("\u001B[34mA\u001B[0m");
            case INK:
                return new String("\u001B[33mI\u001B[0m");
            case FUNGI:
                return new String("\u001B[31mF\u001B[0m");
            case INSECT:
                return new String("\u001B[35mI\u001B[0m");
            case PLANT:
                return new String("\u001B[32mP\u001B[0m");
            case QUILL:
                return new String("\u001B[33mQ\u001B[0m");
            case SCROLL:
                return new String("\u001B[33mQ\u001B[0m");


        }
        return null;
    }
}
