package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * class that sends a message when game board is update
 */
public class BoardUpdateMex implements Message {
    private final String nickname;
    private final List<PhysicalCard> hand;
    private final CardsMatrix cardsMatrix;
    private final int totalScore;

    /**
     * constructs an instance of BoardUpdateMex
     * @param game  game in which appears the message
     * @param player    nickname of the player
     */
    public BoardUpdateMex(Game game, Player player) {
        nickname = player.getNickname();
        hand = game.getHand(player);
        cardsMatrix = game.getCardsArea(player);
        totalScore = game.getTotalScore(player);
    }

    /**

     * @return  player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return  player's hand
     */
    public List<PhysicalCard> getHand() {
        return new ArrayList<>(hand);
    }

    /**
     * @return back of player's hand
     */
    public List<PlayableCard> getHandBacks() {
        return new ArrayList<>(hand.stream().map(PhysicalCard::getBack).toList());
    }

    /**
     * @return a copy of card Matrix
     */
    public CardsMatrix getCardsMatrix() {
        return cardsMatrix.copy();
    }

    /**
     * @return total score
     */
    public int getTotalScore() {
        return totalScore;
    }
}
