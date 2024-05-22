package it.polimi.sw.GC50.net.Messages;

import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndMex implements Message {
    private final List<String> winnerList;
    private final Map<String, Integer> totalScoreMap;
    private final Map<String, Integer> objectivesScoreMap;

    public EndMex(Game game) {
        winnerList = game.getWinnerList().stream().map(Player::getNickname).toList();
        totalScoreMap = new HashMap<>();
        objectivesScoreMap = new HashMap<>();
        for (Player player : game.getPlayerList()) {
            totalScoreMap.put(player.getNickname(), game.getTotalScore(player));
            objectivesScoreMap.put(player.getNickname(), game.getObjectivesScore(player));
        }
    }

    public List<String> getWinnerList() {
        return new ArrayList<>(winnerList);
    }

    public int getTotalScore(String nickname) {
        return totalScoreMap.get(nickname);
    }

    public int getObjectivesScore(String nickname) {
        return objectivesScoreMap.get(nickname);
    }
}
