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

    public String[] toStringTui() {

        ArrayList<StringBuilder> sb = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            sb.add(new StringBuilder());
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
        sb.get(0).append(colorstart);
        sb.get(1).append(colorstart);
        sb.get(2).append(colorstart);
        sb.get(3).append(colorstart);
        sb.get(4).append(colorstart);
        sb.get(5).append(colorstart);
        sb.get(6).append(colorstart);

        if (this.getNwCorner().isVisible()) {
            String x = " ";
            if (this.getNwCorner().isFull()) {
                x = getResString(this.getNwCorner().getResource());
            }
            sb.get(0).append("╔═════╗════════");
            sb.get(1).append("║  ");
            sb.get(1).append(colorend);
            sb.get(1).append("");
            sb.get(1).append(x);
            sb.get(1).append("");
            sb.get(1).append(colorstart);
            sb.get(1).append("  ║");
            sb.get(2).append("╠═════╝        ");
        } else {
            sb.get(0).append("╔══════════════");
            sb.get(1).append("║      ");
            sb.get(2).append("║              ");
        }


        //////////point

        sb.get(1).append("   ");
        if (this.points > 0) {
            sb.get(1).append(colorend);
            sb.get(1).append("\u001B[37m");
            sb.get(1).append(this.points);
            sb.get(1).append(colorend);
            sb.get(1).append(colorstart);
        } else {
            sb.get(1).append(" ");
        }
        sb.get(1).append(" ");

        /////////bonus
        System.out.println(this.bonus.getClass().getSimpleName());
        if (this.bonus.getClass().getSimpleName().equals("BlankBonus")) {
            sb.get(1).append(" ");
        } else if (this.bonus.getClass().getSimpleName().equals("CoveredCornersBonus")) {
            sb.get(1).append("C");
        } else if (this.bonus.getClass().getSimpleName().equals("ResourcesBonus")) {
            ResourcesBonus bonustmp = (ResourcesBonus) this.bonus;
            sb.get(1).append(colorend);
            sb.get(1).append(getResString((bonustmp.getTargetResource())));
            sb.get(1).append(colorstart);
        }
        sb.get(1).append("  ");
        if (this.getNeCorner().isVisible()) {
            String x = " ";
            if (this.getNeCorner().isFull()) {
                x = getResString(this.getNeCorner().getResource());
            }
            sb.get(0).append("╔═════╗");
            sb.get(1).append("║  ");
            sb.get(1).append(colorend);
            sb.get(1).append(x);
            sb.get(1).append(colorstart);
            sb.get(1).append("  ║");
            sb.get(2).append("╚═════╣");

        } else {

            sb.get(0).append("══════╗");
            sb.get(1).append("      ║");
            sb.get(2).append("      ║");
        }
        sb.get(0).append(colorend);
        sb.get(1).append(colorend);
        sb.get(2).append(colorend);

        ///CENTER

        if (this.fixedResources.isEmpty()) {
            sb.get(3).append("║                    ║");
        } else {
            sb.get(3).append("║        ");
            sb.get(3).append(colorend);
            for (Resource res : this.fixedResources) {
                sb.get(3).append(getResString(res));
            }
            sb.get(3).append(colorstart);
            for (int i = 0; i < 12 - this.fixedResources.size(); i++) {
                sb.get(3).append(" ");
            }
            sb.get(3).append("║");

        }
///sud
        if (this.getSwCorner().isVisible()) {
            String x = " ";
            if (this.getNwCorner().isFull()) {
                x = getResString(this.getNwCorner().getResource());
            }
            sb.get(4).append("╠═════╗        ");
            sb.get(5).append("║  ");
            sb.get(5).append(colorend);
            sb.get(5).append(x);
            sb.get(5).append(colorstart);
            sb.get(5).append("  ║        ");
            sb.get(6).append("╚═════╝════════");
        } else {

            sb.get(4).append("║              ");
            sb.get(5).append("║              ");
            sb.get(6).append("╚══════════════");
        }


        if (this.getSeCorner().isVisible()) {
            String x = " ";
            if (this.getNeCorner().isFull()) {

                x = getResString(this.getNeCorner().getResource());

            }
            sb.get(4).append("╔═════╣");
            sb.get(5).append("║  ");
            sb.get(5).append(colorend);
            sb.get(5).append(x);
            sb.get(5).append(colorstart);
            sb.get(5).append("  ║");
            sb.get(6).append("╚═════╝");

        } else {
            sb.get(4).append("      ║");
            sb.get(5).append("      ║");
            sb.get(6).append("══════╝");


        }
        sb.get(4).append(colorend);
        sb.get(5).append(colorend);
        sb.get(6).append(colorend);
        for (int i = 0; i < 7; i++) {
            System.out.println(sb.get(i).toString());
        }

        return null;
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
