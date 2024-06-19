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

public class SetupMex implements Message {
    private final List<ObjectiveCard> commonObjectives;
    private final PlayableCard[] decks;
    private final Map<String, List<PhysicalCard>> handMap;
    private final Map<String, List<ObjectiveCard>> secretObjectivesMap;
    private final Map<String, PhysicalCard> starterCardMap;

    public SetupMex(Game game) {
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

    public List<ObjectiveCard> getCommonObjectives() {
        return new ArrayList<>(commonObjectives);
    }

    public PlayableCard[] getDecks() {
        return decks.clone();
    }

    public List<PhysicalCard> getHand(String nickname) {
        return new ArrayList<>(handMap.get(nickname));
    }

    public List<PlayableCard> getHandBacks(String nickname) {
        return new ArrayList<>(handMap.get(nickname).stream().map(PhysicalCard::getBack).toList());
    }

    public List<ObjectiveCard> getSecretObjectivesMap(String nickname) {
        return new ArrayList<>(secretObjectivesMap.get(nickname));
    }

    public PhysicalCard getStarterCard(String nickname) {
        return starterCardMap.get(nickname);
    }
}
