package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.*;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;

import java.util.*;

public class GameView {
    private boolean inGame;
    // GENERAL //////////////////////////////////////////////////////
    private int playersLeft;
    private GameStatus gameStatus;
    private PlayingPhase playingPhase;
    private boolean turnEnded;
    private String currentPlayer;
    private List<ObjectiveCard> commonObjectives;
    private PlayableCard[] decks;
    private List<String> winnerList;
    // PLAYER SPECIFIC //////////////////////////////////////////////
    private String nickname;
    private List<PhysicalCard> hand;
    private ObjectiveCard secretObjective;
    private List<ObjectiveCard> secreteObjectivesSelection;
    private PhysicalCard starterCard;
    // BOARDS ///////////////////////////////////////////////////////
    private final Map<String, PlayerDataView> playerAreas;

    /**
     * Construct an instance of game view
     *
     * @param nickname player's nickname
     */
    public GameView(String nickname) {
        inGame = false;

        playersLeft = -1;
        gameStatus = GameStatus.WAITING;
        playingPhase = PlayingPhase.PLACING;
        turnEnded = false;
        currentPlayer = null;
        commonObjectives = null;
        decks = null;

        this.nickname = nickname;
        hand = null;
        secretObjective = null;

        secreteObjectivesSelection = null;
        starterCard = null;

        playerAreas = new HashMap<>();
    }

    /**
     * clear game view
     */
    public void clear() {
        inGame = false;

        gameStatus = GameStatus.WAITING;
        playingPhase = PlayingPhase.PLACING;
        currentPlayer = null;
        commonObjectives = null;
        decks = null;

        hand = null;
        secretObjective = null;

        secreteObjectivesSelection = null;
        starterCard = null;

        playerAreas.clear();
    }

    /**
     * @param inGame sets in game
     */
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    /**
     * @return a boolean
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * Sets
     *
     * @param playersLeft number of player that have left the game
     */
    public void setPlayersLeft(int playersLeft) {
        this.playersLeft = playersLeft;
    }

    /**
     * @return number of player that have left the game
     */
    public int getPlayersLeft() {
        return playersLeft;
    }

    /**
     * @return true if all players have joint the game
     */
    public boolean allJoined() {
        return getPlayersLeft() == 0;
    }

    /**
     * @param gameStatus sets game status
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * @return game status
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * @param playingPhase sets playing phase
     */
    public void setPlayingPhase(PlayingPhase playingPhase) {
        this.playingPhase = playingPhase;
    }

    /**
     * @return playing phase
     */
    public PlayingPhase getPlayingPhase() {
        return playingPhase;
    }

    /**
     * @param turnEnded verify if turn is ended
     */
    public void setTurnEnded(boolean turnEnded) {
        this.turnEnded = turnEnded;
    }

    /**
     * @return turn ended
     */
    public boolean isTurnEnded() {
        return turnEnded;
    }

    /**
     * sets a player as current player
     *
     * @param currentPlayer set
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * @return currentPlayer
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Given a objective set as commonObjectives
     *
     * @param commonObjectives selected
     */
    public void setCommonObjectives(List<ObjectiveCard> commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    /**
     * @return a list of commonObjectives
     */
    public List<ObjectiveCard> getCommonObjectives() {
        return new ArrayList<>(commonObjectives);
    }

    /**
     * @param decks set
     */
    public void setDecks(PlayableCard[] decks) {
        this.decks = decks;
    }

    /**
     * @return a copy of the decks
     */
    public PlayableCard[] getDecks() {
        return decks.clone();
    }

    /**
     * sets a winner list
     *
     * @param winnerList set
     */
    public void setWinnerList(List<String> winnerList) {
        this.winnerList = winnerList;
    }

    /**
     * @return a winner list
     */
    public List<String> getWinnerList() {
        return new ArrayList<>(winnerList);
    }

    /**
     * given a player set his/her nickname
     *
     * @param nickname of the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * given a hand selected as player hand
     *
     * @param hand given
     */
    public void setHand(List<PhysicalCard> hand) {
        this.hand = hand;
    }

    /**
     * @return a list of physical card that represents the hand
     */
    public List<PhysicalCard> getHand() {
        return new ArrayList<>(hand);
    }

    /**
     * @param secretObjective Given a objective set a secret objective
     */
    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }

    /**
     * @return secret objective
     */
    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    /**
     * @param secreteObjectivesSelection given a selection of secret objective selected them as secreteObjectivesSelection
     */
    public void setSecreteObjectivesSelection(List<ObjectiveCard> secreteObjectivesSelection) {
        this.secreteObjectivesSelection = secreteObjectivesSelection;
    }

    /**
     * @return a list of secreteObjectivesSelection
     */

    public List<ObjectiveCard> getSecreteObjectivesSelection() {
        return new ArrayList<>(secreteObjectivesSelection);
    }

    /**
     * Given a starter card select them as starter card
     *
     * @param starterCard selected
     */
    public void setStarterCard(PhysicalCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * @return sterter card selected
     */
    public PhysicalCard getStarterCard() {
        return starterCard;
    }

    /**
     * Set player area
     *
     * @param nickname        player's nickname
     * @param cardsMatrix     a copy of card matrix
     * @param totalScore      player's total score
     * @param objectivesScore player's objective score
     * @param ready           player's status
     */
    public void setPlayerArea(String nickname, CardsMatrix cardsMatrix, int totalScore, int objectivesScore, boolean ready) {
        playerAreas.put(nickname, new PlayerDataView(cardsMatrix, totalScore, objectivesScore, ready));
    }

    /**
     * Given a player's nickname remove he/she from player area
     *
     * @param nickname player's nickname
     */
    public void removePlayerArea(String nickname) {
        playerAreas.remove(nickname);
    }

    /**
     * @param nickname player's nickname
     * @return player's area
     */
    public PlayerDataView getPlayerArea(String nickname) {
        return playerAreas.get(nickname);
    }

    /**
     * @return a list of players
     */
    public List<String> getPlayerList() {
        return new ArrayList<>(playerAreas.keySet());
    }

    /**
     * @return true if player's areas are ready
     */
    public boolean allReady() {
        return playerAreas.values().stream()
                .allMatch(PlayerDataView::isReady);
    }
}
