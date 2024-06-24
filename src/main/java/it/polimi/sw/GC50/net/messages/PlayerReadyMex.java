package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;

/**
 * Class that shows a message when a player is ready
 */
public class PlayerReadyMex implements Message {
    private final String nickname;
    private final ObjectiveCard secretObjective;
    private final CardsMatrix cardsMatrix;
    private final int totalScore;

    /**
     * Constructs an instance of PlayerReadyMex
     *
     * @param game   game where the message is sent
     * @param player player's nickname
     */
    public PlayerReadyMex(Game game, Player player) {
        nickname = player.getNickname();
        secretObjective = game.getSecretObjective(player);
        cardsMatrix = game.getCardsArea(player);
        totalScore = game.getTotalScore(player);
    }

    /**
     * @return player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return player's secret objective
     */
    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    /**
     * @return a copy of card matrix
     */
    public CardsMatrix getCardsMatrix() {
        return cardsMatrix.copy();
    }

    /**
     * @return player's total score
     */
    public int getTotalScore() {
        return totalScore;
    }
}
