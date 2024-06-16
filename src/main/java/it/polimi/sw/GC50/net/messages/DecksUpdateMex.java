package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;

import java.util.ArrayList;
import java.util.List;

public class DecksUpdateMex implements Message {
    private final PlayableCard[] decks;
    private final String nickname;
    private final List<PhysicalCard> hand;

    public DecksUpdateMex(Game game, Player player) {
        decks = game.getDecksTop();
        nickname = player.getNickname();
        hand = game.getHand(player);
    }

    public PlayableCard[] getDecks() {
        return decks;
    }

    public String getNickname() {
        return nickname;
    }

    public List<PhysicalCard> getHand() {
        return new ArrayList<>(hand);
    }

    public List<PlayableCard> getHandBacks() {
        return new ArrayList<>(hand.stream().map(PhysicalCard::getBack).toList());
    }
}
