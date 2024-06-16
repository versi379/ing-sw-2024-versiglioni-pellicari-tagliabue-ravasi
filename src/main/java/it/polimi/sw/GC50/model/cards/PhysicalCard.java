package it.polimi.sw.GC50.model.cards;

import java.io.Serializable;

/**
 * Represents a game card as it would be in reality,
 * with the possibility of it being placed on either front or back face
 */
public class PhysicalCard implements Serializable {
    private final CardType cardType;
    private final PlayableCard front;
    private final PlayableCard back;

    /**
     * constructs a new Physical Card instance without identifier code
     *
     * @param cardType   specify type of card
     * @param front      specify front of card
     * @param back       specify back of card
     */
    public PhysicalCard(CardType cardType, PlayableCard front, PlayableCard back) {
        this.cardType = cardType;
        this.front = front;
        this.back = back;
    }

    /**
     * Returns the type of the card
     * @return cardType
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Returns front face of the card
     * @return front face
     */
    public PlayableCard getFront() {
        return front;
    }

    /**
     * Returns back face of the card
     * @return back face
     */
    public PlayableCard getBack() {
        return back;
    }
}
