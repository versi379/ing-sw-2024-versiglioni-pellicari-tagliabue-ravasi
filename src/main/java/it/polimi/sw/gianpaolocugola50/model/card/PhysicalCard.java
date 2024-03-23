package it.polimi.sw.gianpaolocugola50.model.card;

public class PhysicalCard {
    private final CardType cardType;
    private final int quantity;
    private final PlayableCard front;
    private final PlayableCard back;

    public PhysicalCard(CardType cardType, PlayableCard front, PlayableCard back, int quantity) {
        this.cardType = cardType;
        this.front = front;
        this.back = back;
        this.quantity = quantity;
    }

    public CardType getCardType() {
        return cardType;
    }

    public int getQuantity() {
        return quantity;
    }

    public PlayableCard getFront() {
        return front;
    }

    public PlayableCard getBack() {
        return back;
    }
}