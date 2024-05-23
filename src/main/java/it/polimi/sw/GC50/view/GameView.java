package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.*;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;

import java.util.*;

public class GameView {
    // GENERAL //////////////////////////////////////////////////////
    private GameStatus gameStatus;
    private PlayingPhase playingPhase;
    private String currentPlayer;
    private List<ObjectiveCard> commonObjectives;
    private PlayableCard[] decks;
    private List<String> winnerList;
    // PLAYER SPECIFIC //////////////////////////////////////////////
    private String nickname;
    private List<PhysicalCard> hand;
    private ObjectiveCard secretObjective;
    private List<ObjectiveCard> secreteObjectivesList;
    private PhysicalCard starterCard;
    // BOARDS ///////////////////////////////////////////////////////
    private final Map<String, PlayerDataView> playerAreas;

    public GameView(String nickname) {
        gameStatus = GameStatus.WAITING;
        playingPhase = PlayingPhase.PLACING;
        currentPlayer = null;
        commonObjectives = null;
        decks = null;

        this.nickname = nickname;
        hand = null;
        secretObjective = null;

        secreteObjectivesList = null;
        starterCard = null;

        playerAreas = new HashMap<>();
    }

    public void clear() {
        gameStatus = GameStatus.WAITING;
        playingPhase = PlayingPhase.PLACING;
        currentPlayer = null;
        commonObjectives = null;
        decks = null;

        hand = null;
        secretObjective = null;

        secreteObjectivesList = null;
        starterCard = null;

        playerAreas.clear();
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setPlayingPhase(PlayingPhase playingPhase) {
        this.playingPhase = playingPhase;
    }

    public PlayingPhase getPlayingPhase() {
        return playingPhase;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCommonObjectives(List<ObjectiveCard> commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    public List<ObjectiveCard> getCommonObjectives() {
        return new ArrayList<>(commonObjectives);
    }

    public void setDecks(PlayableCard[] decks) {
        this.decks = decks;
    }

    public PlayableCard[] getDecks() {
        return decks.clone();
    }

    public void setWinnerList(List<String> winnerList) {
        this.winnerList = winnerList;
    }

    public List<String> getWinnerList() {
        return new ArrayList<>(winnerList);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setHand(List<PhysicalCard> hand) {
        this.hand = hand;
    }

    public List<PhysicalCard> getHand() {
        return new ArrayList<>(hand);
    }

    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }

    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    public void setSecreteObjectivesList(List<ObjectiveCard> secreteObjectivesList) {
        this.secreteObjectivesList = secreteObjectivesList;
    }

    public List<ObjectiveCard> getSecreteObjectivesList() {
        return new ArrayList<>(secreteObjectivesList);
    }

    public void setStarterCard(PhysicalCard starterCard) {
        this.starterCard = starterCard;
    }

    public PhysicalCard getStarterCard() {
        return starterCard;
    }

    public void setPlayerArea(String nickname, CardsMatrix cardsMatrix, int totalScore, int objectivesScore) {
        playerAreas.put(nickname, new PlayerDataView(cardsMatrix, totalScore, objectivesScore));
    }

    public void removePlayerArea(String nickname) {
        playerAreas.remove(nickname);
    }

    public PlayerDataView getPlayerArea(String nickname) {
        return playerAreas.get(nickname);
    }

    public List<String> getPlayerList() {
        return new ArrayList<>(playerAreas.keySet());
    }
}
