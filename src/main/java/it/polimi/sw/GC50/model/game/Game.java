package it.polimi.sw.GC50.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.GC50.adapter.*;
import it.polimi.sw.GC50.model.GameObservable;
import it.polimi.sw.GC50.model.cards.*;
import it.polimi.sw.GC50.model.chat.ChatMessage;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objectives.Objective;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;
import it.polimi.sw.GC50.net.messages.*;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.*;

public class Game extends GameObservable {

    /**
     * Declared number of players
     */
    private final int numPlayers;

    /**
     * Declared minimum score for triggering game's ending
     */
    private final int endScore;

    /**
     * Initial size of Resource and Gold decks
     */
    private final int deckSize;

    /**
     * Game's current status
     */
    private GameStatus status;


    /**
     * Game's current phase
     */
    private PlayingPhase currentPhase;

    /**
     * Indication that game's termination condition has been met
     */
    private boolean lastRound;

    /**
     * List of game's players
     */
    private final List<Player> playerList;

    /**
     * Mappings between each player and their game's area (PlayerData instances)
     */
    private final Map<Player, PlayerData> playerAreas;

    /**
     * PlayerList's index of currently playing player
     */
    private int currentIndex;

    /**
     * List of winner players (empty until game's ending)
     */
    private final List<Player> winnerList;

    /**
     * Deck of resource (physical) cards
     */
    private final Stack<PhysicalCard> resourceDeck;

    /**
     * Deck of gold (physical) cards
     */
    private final Stack<PhysicalCard> goldDeck;

    /**
     * Revealed (physical) cards in the middle of the table
     */
    private final PhysicalCard[] revealedCards;

    /**
     * Deck of starter (physical) cards
     */
    private final Stack<PhysicalCard> starterDeck;

    /**
     * Deck of objective cards
     */
    private final Stack<ObjectiveCard> objectiveDeck;

    /**
     * List of game's common objective cards
     */
    private final List<ObjectiveCard> commonObjectives;

    /**
     * constructs a new Game instance
     *
     * @param numPlayers number of players in the game
     * @param endScore   shows final score
     */
    public Game(int numPlayers, int endScore) {
        this.numPlayers = numPlayers;
        this.endScore = endScore;
        deckSize = 40;

        status = GameStatus.WAITING;
        currentPhase = PlayingPhase.PLACING;
        lastRound = false;

        playerList = new ArrayList<>();
        playerAreas = new HashMap<>();
        currentIndex = 0;
        winnerList = new ArrayList<>();

        resourceDeck = new Stack<>();
        goldDeck = new Stack<>();
        revealedCards = new PhysicalCard[4];
        starterDeck = new Stack<>();
        objectiveDeck = new Stack<>();
        commonObjectives = new ArrayList<>();
    }

    // GENERAL INFO ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * @return game status
     */
    public GameStatus getStatus() {
        return status;
    }

    // PLAYERS MANAGEMENT //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Verify if a player is in the game
     *
     * @param player player searched
     * @return a boolean ( true if is present)
     */
    public boolean containsPlayer(Player player) {
        return playerList.stream()
                .anyMatch(player::equals);
    }

    /**
     * adds a new player to the game
     *
     * @param player added player
     */
    public void addPlayer(Player player) {
        playerList.add(player);
        playerAreas.put(player, new PlayerData(deckSize));
        setChanged();
        notifyObservers(Notify.NOTIFY_PLAYER_JOINED_GAME, new PlayerJoinedMex(this, player));

        if (playerList.size() >= getNumPlayers()) {
            setup();
        }
    }

    /**
     * Removes a player from the game
     *
     * @param player player removed
     */
    public void removePlayer(Player player) {
        if (playerList.contains(player)) {
            playerList.remove(player);
            playerAreas.remove(player);
            setChanged();
            notifyObservers(Notify.NOTIFY_PLAYER_LEFT_GAME, new PlayerMex(player));

            if (!playerList.isEmpty()) {
                currentIndex--;
                nextPlayer();
            }
        }
    }

    /**
     * @return an array list with all players in the game
     */
    public List<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }

    /**
     * @return who is the current player
     */
    public Player getCurrentPlayer() {
        return playerList.get(currentIndex);
    }

    /**
     * Given a player returns his/her data
     *
     * @param player player searched
     * @return player's infos
     */
    public PlayerData getPlayerData(Player player) {
        return playerAreas.get(player);
    }

    /**
     * Given a nickname returns his/her data
     *
     * @param nickname player searched
     * @return player's infos
     */
    public PlayerData getPlayerData(String nickname) {
        for (Player player : playerList) {
            if (player.equals(new Player(nickname))) {
                return getPlayerData(player);
            }
        }
        return null;
    }

    // SETUP PHASE /////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Prepares game
     */
    private void setup() {
        status = GameStatus.SETUP;
        initDecks();
        setCommonObjectives(2);
        revealedCards[0] = pickCard(DrawingPosition.RESOURCEDECK);
        revealedCards[1] = pickCard(DrawingPosition.RESOURCEDECK);
        revealedCards[2] = pickCard(DrawingPosition.GOLDDECK);
        revealedCards[3] = pickCard(DrawingPosition.GOLDDECK);

        for (Player player : playerList) {
            addCard(player, pickCard(DrawingPosition.RESOURCEDECK));
            addCard(player, pickCard(DrawingPosition.RESOURCEDECK));
            addCard(player, pickCard(DrawingPosition.GOLDDECK));
            setStartingChoices(player, pickStarterCard(), pickObjectivesList(2));
        }
        setChanged();
        notifyObservers(Notify.NOTIFY_GAME_SETUP, new SetupMex(this));
    }

    /**
     * Method used to read the file json with all the cards
     */
    private void initDecks() {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter());
            gsonBuilder.registerTypeAdapter(Objective.class, new ObjectiveAdapter());
            gsonBuilder.registerTypeAdapter(Bonus.class, new BonusAdapter());
            gsonBuilder.registerTypeAdapter(Corner.class, new CornerAdapter());
            gsonBuilder.registerTypeAdapter(PhysicalCard.class, new PhysicalCardAdapter());
            gsonBuilder.registerTypeAdapter(PlayableCard.class, new PlayableCardAdapter());
            gsonBuilder.registerTypeAdapter(GoldCard.class, new GoldCardAdapter());
            Gson gson = gsonBuilder.create();

            FileReader reader = new FileReader
                    ("src/main/resources/cardsJson/physicalCards.json");
            Type physicalCardListType = new TypeToken<List<PhysicalCard>>() {
            }.getType();

            List<PhysicalCard> physicalCards = gson.fromJson(reader, physicalCardListType);
            // now put the cards in the deck
            for (PhysicalCard card : physicalCards) {
                if (CardType.GOLD.equals(card.getCardType())) {
                    goldDeck.add(card);
                } else if (CardType.RESOURCE.equals(card.getCardType())) {
                    resourceDeck.add(card);
                } else if (CardType.STARTER.equals(card.getCardType())) {
                    starterDeck.add(card);
                }
            }
            //objective deck
            reader = new FileReader
                    ("src/main/resources/cardsJson/objectiveCards.json");
            Type objectiveCardType = new TypeToken<List<ObjectiveCard>>() {
            }.getType();
            List<ObjectiveCard> objectiveCards = gson.fromJson(reader, objectiveCardType);
            objectiveDeck.addAll(objectiveCards);

        } catch (IOException ignored) {
        }

        mixAllDecks(resourceDeck);
        mixAllDecks(starterDeck);
        mixAllDecks(goldDeck);
        mixObjective(objectiveDeck);
    }

    /**
     * Method used to mix the cards of the decks
     */
    private void mixAllDecks(Stack<PhysicalCard> deck) {
        for (int i = 0; i < 4; i++) {
            List<PhysicalCard> listDeck = new ArrayList<>(deck);
            Collections.shuffle(listDeck);
            deck.clear();
            for (PhysicalCard card : listDeck) {
                deck.push(card);
            }
        }
    }

    /**
     * Method used to mix the objective cards
     */
    private void mixObjective(Stack<ObjectiveCard> deck) {
        for (int i = 0; i < 4; i++) {
            List<ObjectiveCard> listDeck = new ArrayList<>(deck);
            Collections.shuffle(listDeck);
            deck.clear();
            for (ObjectiveCard card : listDeck) {
                deck.push(card);
            }
        }
    }

    /**
     * choose a list of objective
     *
     * @param quantity of cards in objective card decks
     * @return objective
     */
    private List<ObjectiveCard> pickObjectivesList(int quantity) {
        List<ObjectiveCard> objectives = new ArrayList<>();
        for (int i = 0; i < quantity && !objectiveDeck.isEmpty(); i++) {
            objectives.add(objectiveDeck.pop());
        }
        return objectives;
    }

    /**
     * Given a quantity sets a common objective
     *
     * @param quantity of common objective
     */
    private void setCommonObjectives(int quantity) {
        commonObjectives.addAll(pickObjectivesList(quantity));
    }

    /**
     * returns common objective
     *
     * @return an array list of common objective
     */
    public List<ObjectiveCard> getCommonObjectives() {
        return new ArrayList<>(commonObjectives);
    }

    /**
     * picks the first card from the deck
     *
     * @return the first card from the deck
     */
    public PhysicalCard pickStarterCard() {
        return starterDeck.pop();
    }

    /**
     * Given a player sets the starting choices
     *
     * @param player               who plays
     * @param starterCard          starter card selected
     * @param secretObjectivesList list of secret objectives
     */
    private void setStartingChoices(Player player, PhysicalCard starterCard, List<ObjectiveCard> secretObjectivesList) {
        getPlayerData(player).setStartingChoices(starterCard, secretObjectivesList);
    }

    /**
     * Picks a starter card given a player
     *
     * @param player who choose the starter card
     * @return starter card
     */
    public PhysicalCard getStarterCard(Player player) {
        return getPlayerData(player).getStarterCard();
    }

    /**
     * Given a player returns the list of secret objectives
     *
     * @param player nickname of the player
     * @return secret objectives of the player
     */
    public List<ObjectiveCard> getSecretObjectivesSelection(Player player) {
        return getPlayerData(player).getSecretObjectivesSelection();
    }

    /**
     * Given a player sets the starter card
     *
     * @param player      player who sets starter card
     * @param starterCard starter card chosen
     */
    public void setStarterCard(Player player, PlayableCard starterCard) {
        placeCard(player, starterCard, deckSize, deckSize);
        checkPreparation(player);
        if (isReady(player)) {
            setChanged();
            notifyObservers(Notify.NOTIFY_PLAYER_READY, new PlayerReadyMex(this, player));
            checkSetupStatus();
        }
    }

    /**
     * Given a player sets the secret objective
     *
     * @param player          who set secret objective
     * @param secretObjective secret objective chosen
     */
    public void setSecretObjective(Player player, ObjectiveCard secretObjective) {
        getPlayerData(player).setSecretObjective(secretObjective);
        checkPreparation(player);
        if (isReady(player)) {
            setChanged();
            notifyObservers(Notify.NOTIFY_PLAYER_READY, new PlayerReadyMex(this, player));
            checkSetupStatus();
        }
    }

    /**
     * Returns the secret objective given a player
     *
     * @param player nickname of the player
     * @return secret objective
     */
    public ObjectiveCard getSecretObjective(Player player) {
        return getPlayerData(player).getSecretObjective();
    }

    /**
     * Verify if player status is ready
     *
     * @param player player that we want to know the status
     * @return player status
     */
    public boolean isReady(Player player) {
        return getPlayerData(player).isReady();
    }

    /**
     * * Verify if a player has chosen both objective secret and starter card
     *
     * @param player player selected
     */
    private void checkPreparation(Player player) {
        getPlayerData(player).checkPreparation();
    }

    /**
     * verify if check up status is ready
     */
    private void checkSetupStatus() {
        if (playerList.stream()
                .allMatch(this::isReady)) {
            start();
        }
    }

    // PLAYING PHASE ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * starts game
     */
    private void start() {
        status = GameStatus.PLAYING;
        setChanged();
        notifyObservers(Notify.NOTIFY_GAME_STARTED, new PlayerMex(getCurrentPlayer()));
    }

    /**
     * @return current phase of the game
     */
    public PlayingPhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * phase in which player pick a card from deck
     */
    private void drawingPhase() {
        currentPhase = PlayingPhase.DRAWING;
    }

    /**
     * Returns next player
     */
    private void nextPlayer() {
        if (lastRound && currentIndex == playerList.size() - 1) {
            end();
        } else {
            currentIndex = (currentIndex + 1) % playerList.size();
            currentPhase = PlayingPhase.PLACING;
            setChanged();
            notifyObservers(Notify.NOTIFY_NEXT_TURN, new PlayerMex(getCurrentPlayer()));
        }
    }

    /**
     * Verify if the current round is the last
     *
     * @return true if the round is the last
     */
    public boolean isLastRound() {
        return lastRound;
    }

    /**
     * Sets last round = true
     */
    private void setLastTurn() {
        lastRound = true;
    }

    /**
     * Returns visible PlayableCard in the center of the board during game,
     * then backs of the decks and fronts of the four face up cards
     */
    public PlayableCard[] getDecksTop() {
        if (status.equals(GameStatus.WAITING)) {
            return null;
        }

        PlayableCard[] drawableCards = new PlayableCard[6];
        drawableCards[0] = peekCard(DrawingPosition.RESOURCEDECK);
        drawableCards[1] = peekCard(DrawingPosition.RESOURCE1);
        drawableCards[2] = peekCard(DrawingPosition.RESOURCE2);
        drawableCards[3] = peekCard(DrawingPosition.GOLDDECK);
        drawableCards[4] = peekCard(DrawingPosition.GOLD1);
        drawableCards[5] = peekCard(DrawingPosition.GOLD2);
        return drawableCards;
    }

    /**
     * Select pick card from the decks
     *
     * @param position where we place the card
     * @return peek of the decks
     */
    private PlayableCard peekCard(DrawingPosition position) {
        return switch (position) {
            case DrawingPosition.RESOURCEDECK -> (!resourceDeck.isEmpty()) ? resourceDeck.peek().getBack() : null;
            case DrawingPosition.RESOURCE1 -> revealedCards[0].getFront();
            case DrawingPosition.RESOURCE2 -> revealedCards[1].getFront();
            case DrawingPosition.GOLDDECK -> (!goldDeck.isEmpty()) ? goldDeck.peek().getBack() : null;
            case DrawingPosition.GOLD1 -> revealedCards[2].getFront();
            case DrawingPosition.GOLD2 -> revealedCards[3].getFront();
        };
    }

    /**
     * Pick a card from the deck
     *
     * @param position of the card
     * @return a card
     */
    public PhysicalCard pickCard(DrawingPosition position) {
        PhysicalCard card = null;
        switch (position) {
            case DrawingPosition.RESOURCEDECK -> {
                if (!resourceDeck.isEmpty()) {
                    card = resourceDeck.pop();
                    if (resourceDeck.isEmpty() && goldDeck.isEmpty()) {
                        setLastTurn();
                    }
                }
            }
            case DrawingPosition.RESOURCE1 -> {
                card = revealedCards[0];
                if (card != null) {
                    if (!resourceDeck.isEmpty()) {
                        revealedCards[0] = pickCard(DrawingPosition.RESOURCEDECK);
                    } else if (!goldDeck.isEmpty()) {
                        revealedCards[0] = pickCard(DrawingPosition.GOLDDECK);
                    } else {
                        revealedCards[0] = null;
                    }
                }
            }
            case DrawingPosition.RESOURCE2 -> {
                card = revealedCards[1];
                if (card != null) {
                    if (!resourceDeck.isEmpty()) {
                        revealedCards[1] = pickCard(DrawingPosition.RESOURCEDECK);
                    } else if (!goldDeck.isEmpty()) {
                        revealedCards[1] = pickCard(DrawingPosition.GOLDDECK);
                    } else {
                        revealedCards[1] = null;
                    }
                }
            }
            case DrawingPosition.GOLDDECK -> {
                if (!goldDeck.isEmpty()) {
                    card = goldDeck.pop();
                    if (resourceDeck.isEmpty() && goldDeck.isEmpty()) {
                        setLastTurn();
                    }
                }
            }
            case DrawingPosition.GOLD1 -> {
                card = revealedCards[2];
                if (card != null) {
                    if (!goldDeck.isEmpty()) {
                        revealedCards[2] = pickCard(DrawingPosition.GOLDDECK);
                    } else if (!resourceDeck.isEmpty()) {
                        revealedCards[2] = pickCard(DrawingPosition.RESOURCEDECK);
                    } else {
                        revealedCards[2] = null;
                    }
                }
            }
            case DrawingPosition.GOLD2 -> {
                card = revealedCards[3];
                if (card != null) {
                    if (!goldDeck.isEmpty()) {
                        revealedCards[3] = pickCard(DrawingPosition.GOLDDECK);
                    } else if (!resourceDeck.isEmpty()) {
                        revealedCards[3] = pickCard(DrawingPosition.RESOURCEDECK);
                    } else {
                        revealedCards[3] = null;
                    }
                }
            }
        }
        return card;
    }

    /**
     * Places a card on the board
     *
     * @param player player who puts the card
     * @param card   card played
     * @param x      X coordinates
     * @param y      Y coordinates
     */
    public void placeCard(Player player, PlayableCard card, int x, int y) {
        getPlayerData(player).placeCard(card, x, y);
        if (getTotalScore(player) >= endScore) {
            setLastTurn();
        }
        if (status.equals(GameStatus.PLAYING)) {
            drawingPhase();
            setChanged();
            notifyObservers(Notify.NOTIFY_CARD_PLACED, new BoardUpdateMex(this, player));
        }
    }

    /**
     * Returns cards area of a player
     *
     * @param player player selected
     * @return player's cards area
     */
    public CardsMatrix getCardsArea(Player player) {
        return getPlayerData(player).getCardsArea();
    }

    /**
     * Adds a card in a player area
     *
     * @param player player selected
     * @param card   card that we add
     */
    public void addCard(Player player, PhysicalCard card) {
        getPlayerData(player).addCard(card);
        if (status.equals(GameStatus.PLAYING)) {
            setChanged();
            notifyObservers(Notify.NOTIFY_CARD_DRAWN, new DecksUpdateMex(this, player));
            nextPlayer();
        }
    }

    /**
     * removes a card from a player area
     *
     * @param player player selected
     * @param index  index of the card
     */
    public void removeCard(Player player, int index) {
        getPlayerData(player).removeCard(index);
    }

    /**
     * returns a player hand
     *
     * @param player player selected
     * @return player's hand
     */
    public List<PhysicalCard> getHand(Player player) {
        return getPlayerData(player).getHand();
    }

    /**
     * add a message in the chat
     *
     * @param player  who send the message
     * @param message content of the message
     */
    public void addChatMessage(Player player, String message) {
        ChatMessage chatMessage = new ChatMessage(player, message, LocalTime.now());
        setChanged();
        notifyObservers(Notify.NOTIFY_CHAT_MESSAGE, new ChatMex(chatMessage));
    }

    public void addChatMessage(Player player, Player receiver, String message) {
        ChatMessage chatMessage = new ChatMessage(player, receiver, message, LocalTime.now());
        setChanged();
        notifyObservers(Notify.NOTIFY_CHAT_MESSAGE, new ChatMex(chatMessage));
    }

    // END PHASE ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Finishes the game
     */
    private void end() {
        status = GameStatus.ENDED;
        playerList.forEach(this::setFinalScore);

        int maxScore = playerList.stream()
                .map(this::getTotalScore)
                .max(Integer::compareTo)
                .orElse(0);
        playerList.stream()
                .filter(x -> getTotalScore(x) == maxScore)
                .forEach(winnerList::add);
        if (winnerList.size() > 1) {
            int maxObjectivesScore = playerList.stream()
                    .map(this::getObjectivesScore)
                    .max(Integer::compareTo)
                    .orElse(0);
            winnerList.removeIf(x -> getObjectivesScore(x) < maxObjectivesScore);
        }
        setChanged();
        notifyObservers(Notify.NOTIFY_GAME_ENDED, new EndMex(this));
    }

    /**
     * Returns player who win the game
     *
     * @return an array list
     */
    public List<Player> getWinnerList() {
        return new ArrayList<>(winnerList);
    }

    private void setFinalScore(Player player) {
        getPlayerData(player).setFinalScore(getCommonObjectives());
    }

    /**
     * Returns the total score of a player
     *
     * @param player player selected
     * @return player's total score
     */
    public int getTotalScore(Player player) {
        return getPlayerData(player).getTotalScore();
    }

    /**
     * Returns score from objective given a player
     *
     * @param player player selected
     * @return player's objective score
     */
    public int getObjectivesScore(Player player) {
        return getPlayerData(player).getObjectivesScore();
    }

    // OTHER METHODS ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Notifies an error
     *
     * @param player  player selected
     * @param content content of message error
     */
    public void error(Player player, String content) {
        setChanged();
        notifyObservers(Notify.NOTIFY_ERROR, new ErrorMex(player, content));
    }

    // TEST METHODS ////////////////////////////////////////////////////////////////////////////////////////////////////
    public int resourceDeckSize() {
        return resourceDeck.size();
    }

    public int goldDeckSize() {
        return goldDeck.size();
    }
}
