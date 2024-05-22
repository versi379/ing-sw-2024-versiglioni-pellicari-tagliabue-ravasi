package it.polimi.sw.GC50.net.Messages;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardUpdateMex implements Message {
    private final String nickname;
    private final List<PhysicalCard> hand;
    private final CardsMatrix cardsMatrix;
    private final int totalScore;

    public BoardUpdateMex(Game game, Player player) {
        nickname = player.getNickname();
        hand = game.getHand(player);
        cardsMatrix = game.getCardsArea(player);
        totalScore = game.getTotalScore(player);
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
    public CardsMatrix getCardsMatrix() {
        return cardsMatrix.copy();
    }

    public int getTotalScore() {
        return totalScore;
    }
}
