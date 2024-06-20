package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * class that sends a message when decks is update
 */
public class DecksUpdateMex implements Message {
    private final PlayableCard[] decks;
    private final String nickname;
    private final List<PhysicalCard> hand;

    /**
     * Constructs an instance of DecksUpdateMex
     * @param game      game where message is sent
     * @param player    who receive the message
     */
    public DecksUpdateMex(Game game, Player player) {
        decks = game.getDecksTop();
        nickname = player.getNickname();
        hand = game.getHand(player);
    }

    /**

     * @return  decks
     */
    public PlayableCard[] getDecks() {
        return decks;
    }

    /**
     * @return player's nickname
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

     * @return  an arraylist of player's hand
     */
    public List<PlayableCard> getHandBacks() {
        return new ArrayList<>(hand.stream().map(PhysicalCard::getBack).toList());
    }
}
