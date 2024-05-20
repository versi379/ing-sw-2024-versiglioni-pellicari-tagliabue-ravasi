package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayerData;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;

import java.io.Serializable;
import java.util.*;

public class GameView implements Serializable {
    private final GameStatus gameStatus;
    private final PlayingPhase playingPhase;
    private final String currentPlayer;
    private final List<PlayableCard> decks;
    private final String playerNickname;
    private final List<PhysicalCard> hand;
    private final ObjectiveCard secretObjective;
    private final List<ObjectiveCard> secreteObjectivesList;
    private final PhysicalCard starterCard;
    private final Map<String, PlayerDataView> playerAreas;

    public GameView(Game game, Player player) {
        gameStatus = game.getStatus();
        playingPhase = game.getCurrentPhase();
        currentPlayer = game.getCurrentPlayer().getNickname();
        decks = new ArrayList<>(Arrays.asList(game.getDecksTop()));

        playerNickname = player.getNickname();
        PlayerData playerData = game.getPlayerData(player);
        hand = new ArrayList<>(playerData.getHand());
        secretObjective = playerData.getSecretObjective();

        secreteObjectivesList = playerData.getSecretObjectivesList();
        starterCard = playerData.getStarterCard();

        playerAreas = new HashMap<>();
        for(Player p : game.getPlayerList()) {
            playerData = game.getPlayerData(p);
            playerAreas.put(p.getNickname(), new PlayerDataView(playerData.getCardsArea(), playerData.getTotalScore()));
        }
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public PlayingPhase getPlayingPhase() {
        return playingPhase;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public List<PlayableCard> getDecks() {
        return new ArrayList<>(decks);
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public List<PhysicalCard> getHand() {
        return new ArrayList<>(hand);
    }

    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    public List<ObjectiveCard> getSecreteObjectivesList() {
        return new ArrayList<>(secreteObjectivesList);
    }

    public PhysicalCard getStarterCard() {
        return starterCard;
    }

    public PlayerDataView getPlayerArea(String nickname) {
        return playerAreas.get(nickname);
    }
}
