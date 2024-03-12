package it.polimi.sw.gianpaolocugola50.model;

public class GoldCard extends RGCard {
    private Map<Resource, int> constraint;
    private BonusType bonus;

    public void setConstraint(Map<Resource, int>) {

    }

    public Map<Resource, int> getConstraint {

    }

    public void setBonus(BonusType bonus) {
        this.bonus = bonus;
    }

    public BonusType getBonus() {
        return bonus;
    }
}
