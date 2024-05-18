package it.polimi.sw.GC50.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.GC50.adapter.*;
import it.polimi.sw.GC50.model.card.*;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.chat.Message;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.*;
import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.util.Request;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.*;

public class Game extends Observable implements Serializable, GameInterface {

    /**
     * Game's unique identifier
     */
    private final String id;

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
     * Game's chat
     */
    private final Chat chat;

    public Game(String id, int numPlayers, int endScore, Player creator) {
        this.id = id;
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

        chat = new Chat();

        addPlayer(creator);
    }

    // GENERAL INFO ////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getId() {
        return id;
    }

    public PlayableCard[] getDecksTop() {
        PlayableCard[] drawableCards = new PlayableCard[6];

        drawableCards[0] = peekCard(DrawingPosition.RESOURCEDECK);
        drawableCards[1] = peekCard(DrawingPosition.RESOURCE1);
        drawableCards[2] = peekCard(DrawingPosition.RESOURCE2);
        drawableCards[3] = peekCard(DrawingPosition.GOLDDECK);
        drawableCards[4] = peekCard(DrawingPosition.GOLD1);
        drawableCards[5] = peekCard(DrawingPosition.GOLD2);
        return drawableCards;
    }

    public Chat getChat() {
        return chat;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public GameStatus getStatus() {
        return status;
    }

    // PLAYERS MANAGEMENT //////////////////////////////////////////////////////////////////////////////////////////////
    public boolean containsPlayer(Player player) {
        return playerList.stream()
                .anyMatch(player::equals);
    }

    public boolean containsPlayer(String nickname) {
        return playerList.stream()
                .map(Player::getNickname)
                .anyMatch(nickname::equals);
    }

    public void addPlayer(Player player) {
        playerList.add(player);
        playerAreas.put(player, new PlayerData(deckSize));
        player.setCurrentGame(this);
        setChanged();
        notifyObservers(Request.NOTIFY_PLAYER_JOINED_GAME, player.getNickname());

        if (playerList.size() >= getNumPlayers()) {
            setChanged();
            notifyObservers(Request.NOTIFY_ALL_PLAYER_JOINED_THE_GAME, null);
            setup();
            setChanged();
            notifyObservers(Request.NOTIFY_GAME_SETUP, null);
        }
    }

    public void removePlayer(Player player) {
        if (playerList.contains(player)) {
            playerList.remove(player);
            playerAreas.remove(player);
            if (currentIndex >= playerList.size()) {
                currentIndex = 0;
            }
            setChanged();
            notifyObservers(Request.NOTIFY_PLAYER_LEFT_GAME, player.getNickname());
        }
    }

    public List<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }

    public Player getCurrentPlayer() {
        return playerList.get(currentIndex);
    }

    public PlayerData getPlayerData(Player player) {
        return playerAreas.get(player);
    }

    public PlayerData getPlayerData(String nickname) {
        for (Player player : playerList) {
            if (player.equals(new Player(nickname))) {
                return getPlayerData(player);
            }
        }
        return null;
    }

    // SETUP PHASE /////////////////////////////////////////////////////////////////////////////////////////////////////
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
                    ("src/main/resources/it/polimi/sw/GC50/cardJson/physicalCardGenerated2.json");
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
                    ("src/main/resources/it/polimi/sw/GC50/cardJson/objectiveCardGenerated.json");
            Type objectiveCardType = new TypeToken<List<ObjectiveCard>>() {
            }.getType();
            List<ObjectiveCard> objectiveCards = gson.fromJson(reader, objectiveCardType);
            objectiveDeck.addAll(objectiveCards);

        } catch (IOException e) {
            e.printStackTrace();
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

    private List<ObjectiveCard> pickObjectivesList(int quantity) {
        List<ObjectiveCard> objectives = new ArrayList<>();
        for (int i = 0; i < quantity && !objectiveDeck.isEmpty(); i++) {
            objectives.add(objectiveDeck.pop());
        }
        return objectives;
    }

    private void setCommonObjectives(int quantity) {
        commonObjectives.addAll(pickObjectivesList(quantity));
    }

    public List<ObjectiveCard> getCommonObjectives() {
        return new ArrayList<>(commonObjectives);
    }

    public PhysicalCard pickStarterCard() {
        return starterDeck.pop();
    }

    private void setStartingChoices(Player player, PhysicalCard starterCard, List<ObjectiveCard> secretObjectivesList) {
        getPlayerData(player).setStartingChoices(starterCard, secretObjectivesList);
    }

    public PhysicalCard getStarterCard(Player player) {
        return getPlayerData(player).getStarterCard();
    }

    public List<ObjectiveCard> getSecretObjectivesList(Player player) {
        return getPlayerData(player).getSecretObjectivesList();
    }

    public void setStarterCard(Player player, PlayableCard starterCard) {
        placeCard(player, starterCard, deckSize, deckSize);
        checkPreparation(player);
        if (isReady(player)) {
            setChanged();
            notifyObservers(Request.NOTIFY_PLAYER_READY, player.getNickname());
            checkSetupStatus();
        }
    }

    public void setSecretObjective(Player player, ObjectiveCard secretObjective) {
        getPlayerData(player).setSecretObjective(secretObjective);
        checkPreparation(player);
        setChanged();
        notifyObservers(Request.NOTIFY_CHOOSE_OBJECTIVE, player.getNickname());
        if (isReady(player)) {
            setChanged();
            notifyObservers(Request.NOTIFY_PLAYER_READY, player.getNickname());
            checkSetupStatus();
        }
    }

    public boolean isReady(Player player) {
        return getPlayerData(player).isReady();
    }

    private void checkPreparation(Player player) {
        getPlayerData(player).checkPreparation();
    }

    private void checkSetupStatus() {
        if (playerList.stream()
                .allMatch(this::isReady)) {
            start();
            setChanged();
            notifyObservers(Request.NOTIFY_GAME_STARTED, null);

        }
    }

    // PLAYING PHASE ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void start() {
        status = GameStatus.PLAYING;
        System.err.println("Game \"" + id + "\" has started");
    }

    public PlayingPhase getCurrentPhase() {
        return currentPhase;
    }

    private void drawingPhase() {
        currentPhase = PlayingPhase.DRAWING;
    }

    private void nextPlayer() {
        if (lastRound && currentIndex == playerList.size() - 1) {
            end();
        } else {
            currentIndex = (currentIndex + 1) % playerList.size();
            currentPhase = PlayingPhase.PLACING;
            setChanged();
            notifyObservers(Request.NOTIFY_NEXT_TURN, null);
        }
    }

    public boolean isLastRound() {
        return lastRound;
    }

    private void setLastTurn() {
        lastRound = true;
    }

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


    public void placeCard(Player player, PlayableCard card, int x, int y) {
        getPlayerData(player).placeCard(card, x, y);
        if (getTotalScore(player) >= endScore) {
            setLastTurn();
        }
        if (status.equals(GameStatus.PLAYING)) {
            drawingPhase();
        }
    }

    public void addCard(Player player, PhysicalCard card) {
        getPlayerData(player).addCard(card);
        setChanged();
        notifyObservers(Request.NOTIFY_CARD_DRAW, player.getNickname());
        if (status.equals(GameStatus.PLAYING)) {
            nextPlayer();
        }
    }

    public void removeCard(Player player, int index) {
        getPlayerData(player).removeCard(index);
    }

    public List<PhysicalCard> getHand(Player player) {
        return getPlayerData(player).getHand();
    }

    public void sendMessageInChat(Player player, String message) {
        chat.addMessage(new Message(player, message, LocalTime.now()));
        setChanged();
        notifyObservers(Request.NOTIFY_CHAT_MESSAGE, null);
    }

    // END PHASE ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private void end() {
        status = GameStatus.ENDED;
        playerList.stream()
                .map(this::getPlayerData)
                .forEach(x -> x.setFinalScore(getCommonObjectives()));

        int maxScore = playerList.stream()
                .map(this::getTotalScore)
                .max(Integer::compareTo)
                .orElse(0);
        playerList.stream()
                .filter(x -> getTotalScore(x) == maxScore)
                .forEach(winnerList::add);
        if (winnerList.size() == 1) {
            System.err.println("Vincitore: " + winnerList.getFirst());
            System.err.println("Punteggio: " + maxScore);

        } else {
            int maxObjectivesScore = playerList.stream()
                    .map(this::getObjectivesScore)
                    .max(Integer::compareTo)
                    .orElse(0);
            winnerList.removeIf(x -> getObjectivesScore(x) < maxObjectivesScore);
            if (winnerList.size() == 1) {
                System.err.println("Vincitore: " + winnerList.getFirst());
                System.err.println("Punteggio: " + maxScore);
                System.err.println("Punteggio obiettivi: " + maxObjectivesScore);

            } else {
                System.err.println("Pareggio");
                System.err.print("Vincitori:");
                for (Player player : winnerList) {
                    System.err.print(" " + player);
                }
                System.err.println();
                System.err.println("Punteggio: " + maxScore);
                System.err.println("Punteggio obiettivi: " + maxObjectivesScore);
                System.err.println();
            }
        }
    }

    public List<Player> getWinnerList() {
        return new ArrayList<>(winnerList);
    }

    public int getTotalScore(Player player) {
        return getPlayerData(player).getTotalScore();
    }

    public int getObjectivesScore(Player player) {
        return getPlayerData(player).getObjectivesScore();
    }

    // OTHER METHODS ///////////////////////////////////////////////////////////////////////////////////////////////////
    public void error(Request request, Object arg) {
        setChanged();
        notifyObservers(request, arg);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Game game)) {
            return false;
        }
        return getId().equals(game.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return getId();
    }

    // TEST METHODS ////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<ObjectiveCard> getObjectives(int quantity) {
        return pickObjectivesList(quantity);
    }

    public int resourceDeckSize() {
        return resourceDeck.size();
    }

    public int goldDeckSize() {
        return goldDeck.size();
    }

    public void forceEnd() {
        end();
    }
}
