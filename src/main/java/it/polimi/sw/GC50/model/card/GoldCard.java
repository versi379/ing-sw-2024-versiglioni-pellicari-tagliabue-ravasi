package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.util.*;

/**
 * Specific type of playable card, characterized by specific rules
 */
public class GoldCard extends PlayableCard implements Serializable {

    /**
     * Represents the constraint regarding the amount of resources
     * needed in the player's area in order to place the card
     */
    private final Map<Resource, Integer> constraint;

    public GoldCard(Color color, int points, Bonus bonus, List<Resource> fixedResources, Corner[] corners, List<Resource> constraint) {
        super(color, points, bonus, fixedResources, corners);

        this.constraint = new EnumMap<>(Resource.class);
        for (Resource resource : Resource.values()) {
            this.constraint.put(resource, 0);
        }
        if (constraint != null) {
            for (Resource resource : constraint) {
                this.constraint.replace(resource, (this.constraint.get(resource)) + 1);
            }
        }
    }

    public List<Resource> getConstraintList() {
        List<Resource> constraintList = new ArrayList<>();
        for (Map.Entry<Resource, Integer> entry : constraint.entrySet()) {
            Resource resource = entry.getKey();
            for (int i = entry.getValue(); i > 0; i--) {
                constraintList.add(resource);
            }
        }
        return constraintList;
    }

    @Override
    public boolean isPlaceable(PlayerData board, int x, int y) {
        return board.isPositionValid(x, y) &&
                this.checkConstraint(board);
    }

    public boolean checkConstraint(PlayerData playerData) {
        return constraint.keySet().stream()
                .noneMatch(x -> constraint.get(x) > playerData.numOfResource(x));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GoldCard goldCard)) {
            return false;
        }
        return getColor().equals(goldCard.getColor()) &&
                getPoints() == goldCard.getPoints() &&
                getBonus().equals(goldCard.getBonus()) &&
                getFixedResources().equals(goldCard.getFixedResources()) &&
                getCorners().equals(goldCard.getCorners()) &&
                getConstraintList().equals(goldCard.getConstraintList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor(), getPoints(), getBonus(), getFixedResources(), getCorners(), getConstraintList());
    }

    @Override
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

        if (super.getColor().equals(Color.GREEN)) {
            colorstart = ("\u001B[32m");


        }
        if (super.getColor().equals(Color.BLUE)) {
            colorstart = ("\u001B[34m");

        }
        if (super.getColor().equals(Color.RED)) {
            colorstart = ("\u001B[31m");

        }
        if (super.getColor().equals(Color.PURPLE)) {
            colorstart = ("\u001B[35m");


        }


        //corner nw
        if (this.getNwCorner().isVisible()) {
            String x = " ";
            if (this.getNwCorner().isFull()) {
                x = getResString(this.getNwCorner().getResource());
            }
            cardtmp[0][0].append(colorstart);
            cardtmp[0][0].append("╔═════╗");
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
        cardtmp[0][1].append("════════");
        cardtmp[1][1].append("   ");
        cardtmp[2][1].append("        ");

        if (super.getPoints() > 0) {
            cardtmp[1][1].append(colorend);
            cardtmp[1][1].append("\u001B[37m");
            cardtmp[1][1].append(super.getPoints());


        } else {
            cardtmp[1][1].append(" ");
        }
        cardtmp[1][1].append(" ");

        /////////bonus

        if (super.getBonus().getClass().getSimpleName().equals("BlankBonus")) {
            cardtmp[1][1].append(" ");

        } else if (super.getBonus().getClass().getSimpleName().equals("CoveredCornersBonus")) {
            cardtmp[1][1].append("C");
        } else if (super.getBonus().getClass().getSimpleName().equals("ResourcesBonus")) {
            ResourcesBonus bonustmp = (ResourcesBonus) super.getBonus();
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
            cardtmp[0][2].append("╔═════╗");
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

        if (super.getFixedResources().isEmpty()) {
            cardtmp[3][0].append("║                    ║");
        } else {
            cardtmp[3][0].append("║        ");
            cardtmp[3][0].append(colorend);
            for (Resource res : super.getFixedResources()) {
                cardtmp[3][0].append(getResString(res));
            }
            cardtmp[3][0].append(colorstart);
            for (int i = 0; i < 12 - super.getFixedResources().size(); i++) {
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
            cardtmp[6][0].append("╚═════╝");
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
        cardtmp[4][1].append("        ");



            List<Resource> ConstraintList =getConstraintList();
            if (ConstraintList == null) {
                cardtmp[5][1].append("        ");
            } else {
                cardtmp[5][1].append(" ");
                for (Resource res : ConstraintList) {
                    cardtmp[5][1].append(getResString(res));
                }
                for (int i = 0; i < 7 - ConstraintList.size(); i++) {
                    cardtmp[5][1].append(" ");
                }


            }



        cardtmp[6][1].append("════════");
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
            cardtmp[6][2].append("╚═════╝");

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
                System.out.print(card[i][j]);
            }

            System.out.print("\n");
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
