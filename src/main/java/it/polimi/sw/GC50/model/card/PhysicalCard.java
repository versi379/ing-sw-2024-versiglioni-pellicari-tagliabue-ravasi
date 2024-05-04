package it.polimi.sw.GC50.model.card;

import java.io.Serializable;

/**
 * Represents a game card as it would be in reality,
 * with the possibility of it being placed on either front or back face
 */
public class PhysicalCard implements Serializable {

    //code is a unique identifier for the card
    private String code;
    private final CardType cardType;
    private final PlayableCard front;
    private final PlayableCard back;

    public PhysicalCard(CardType cardType, PlayableCard front, PlayableCard back) {
        this.cardType = cardType;
        this.front = front;
        this.back = back;
    }

    public PhysicalCard(String code, CardType cardType, PlayableCard front, PlayableCard back) {
        this.code = code;
        this.cardType = cardType;
        this.front = front;
        this.back = back;
    }
    public String getCode() {
        return code;
    }

    public CardType getCardType() {
        return cardType;
    }

    public PlayableCard getFront() {
        return front;
    }

    public PlayableCard getBack() {
        return back;
    }
}
