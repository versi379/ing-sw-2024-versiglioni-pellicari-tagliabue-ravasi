package it.polimi.sw.GC50.model.card;

/**
 * Represents a game card as it would be in reality,
 * with the possibility of it being placed on either front or back face
 */
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
