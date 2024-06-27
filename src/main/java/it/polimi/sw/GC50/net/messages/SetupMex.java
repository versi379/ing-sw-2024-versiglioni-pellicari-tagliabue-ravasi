package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class that manages set up messages
 */
public class SetupMex implements Message {
    private final List<String> playerList;
    private final List<ObjectiveCard> commonObjectives;
    private final PlayableCard[] decks;
    private final Map<String, List<PhysicalCard>> handMap;
    private final Map<String, List<ObjectiveCard>> secretObjectivesMap;
    private final Map<String, PhysicalCard> starterCardMap;

    /**
     * Construct an instance of SetupMex
     *
     * @param game we referred
     */
    public SetupMex(Game game) {
        playerList = game.getPlayerList().stream().map(Player::getNickname).toList();
        commonObjectives = game.getCommonObjectives();
        decks = game.getDecksTop();
        handMap = new HashMap<>();
        secretObjectivesMap = new HashMap<>();
        starterCardMap = new HashMap<>();
        for (Player player : game.getPlayerList()) {
            handMap.put(player.getNickname(), game.getHand(player));
            secretObjectivesMap.put(player.getNickname(), game.getSecretObjectivesSelection(player));
            starterCardMap.put(player.getNickname(), game.getStarterCard(player));
        }
    }

    /**
     * @return an array list of players
     */
    public List<String> getPlayerList() {
        return  new ArrayList<>(playerList);
    }

    /**
     * @return an array list of common objectives
     */
    public List<ObjectiveCard> getCommonObjectives() {
        return new ArrayList<>(commonObjectives);
    }

    /**
     * @return a copy of decks
     */
    public PlayableCard[] getDecks() {
        return decks.clone();
    }

    /**
     * Given a player returns his/her hand
     *
     * @param nickname player's nickname
     * @return player's hand
     */

    public List<PhysicalCard> getHand(String nickname) {
        return new ArrayList<>(handMap.get(nickname));
    }

    /**
     * Given a player returns his/her backs hand
     *
     * @param nickname player's nickname
     * @return player's backs hand
     */
    public List<PlayableCard> getHandBacks(String nickname) {
        return new ArrayList<>(handMap.get(nickname).stream().map(PhysicalCard::getBack).toList());
    }

    /**
     * Given a player returns a map of his/her secret objectives
     *
     * @param nickname player's nickname
     * @return player's  secret objectives
     */
    public List<ObjectiveCard> getSecretObjectivesMap(String nickname) {
        return new ArrayList<>(secretObjectivesMap.get(nickname));
    }

    /**
     * Given a player returns his/her starter card
     *
     * @param nickname player's nickname
     * @return player's  starter card
     */
    public PhysicalCard getStarterCard(String nickname) {
        return starterCardMap.get(nickname);
    }
}
