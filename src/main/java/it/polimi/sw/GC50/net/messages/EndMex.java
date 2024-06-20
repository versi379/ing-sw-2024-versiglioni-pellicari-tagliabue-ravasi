package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class that sends a message when game is finished
 */
public class EndMex implements Message {
    private final List<String> winnerList;
    private final Map<String, Integer> totalScoreMap;
    private final Map<String, Integer> objectivesScoreMap;

    /**
     * Constructs an instance of EndMex
     * @param game  where message is sent
     */
    public EndMex(Game game) {
        winnerList = game.getWinnerList().stream().map(Player::getNickname).toList();
        totalScoreMap = new HashMap<>();
        objectivesScoreMap = new HashMap<>();
        for (Player player : game.getPlayerList()) {
            totalScoreMap.put(player.getNickname(), game.getTotalScore(player));
            objectivesScoreMap.put(player.getNickname(), game.getObjectivesScore(player));
        }
    }

    /**
     * @return a list of winners
     */
    public List<String> getWinnerList() {
        return new ArrayList<>(winnerList);
    }

    /**
     * Given a nickname of a player returns his total score
     * @param nickname player's nickname
     * @return player's total score
     */
    public int getTotalScore(String nickname) {
        return totalScoreMap.get(nickname);
    }

    /**
     * Given a nickname of a player returns his objective score
     * @param nickname player's nickname
     * @return player's objective score
     */
    public int getObjectivesScore(String nickname) {
        return objectivesScoreMap.get(nickname);
    }
}
