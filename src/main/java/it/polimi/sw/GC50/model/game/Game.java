package it.polimi.sw.GC50.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.GC50.model.adapter.*;
import it.polimi.sw.GC50.model.card.*;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.objective.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class Game {
    private final String id;

    //number of players
    private final int numPlayers;

    private final int endScore;

    private final int deckSize;

    //status of the game
    private GameStatus status;

    private boolean lastTurn;

    //list of player of one game, max numPlayers
    //the first player on the list is the black one
    private final List<Player> playerList;

    //index of the currently playing player
    private int currentIndex;

    private PlayingPhase currentPhase;

    //map of player's data
    private final Map<Player, PlayerData> playerAreas;

    //deck of resourceCard
    private final Stack<PhysicalCard> resourceDeck;

    //deck of goldCard
    private final Stack<PhysicalCard> goldDeck;

    private final PhysicalCard[] revealedCards;

    //deck of StarterCard
    private final Stack<PhysicalCard> starterDeck;

    private final Stack<ObjectiveCard> objectiveDeck;

    //these are the objective that all players have in common
    private final List<ObjectiveCard> commonObjectives;

    private final Chat chat;


    public Game(String id, int numPlayers, int endScore, Player creator) {
        this.id = id;
        this.numPlayers = numPlayers;
        this.endScore = endScore;
        deckSize = 40;

        status = GameStatus.WAITING;
        playerList = new ArrayList<>();
        playerAreas = new HashMap<>();
        resourceDeck = new Stack<>();
        goldDeck = new Stack<>();
        revealedCards = new PhysicalCard[4];
        starterDeck = new Stack<>();
        objectiveDeck = new Stack<>();
        commonObjectives = new ArrayList<>();
        chat = new Chat();
        addPlayer(creator);
    }

    // GAME'S GENERAL INFOS ____________________________________________________________________________________________
    public String getId() {
        return id;
    }

    public GameStatus getStatus() {
        return status;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    // PLAYERS MANAGEMENT ______________________________________________________________________________________________
    public void addPlayer(Player player) {
        playerList.add(player);
        playerAreas.put(player, new PlayerData(deckSize));
        player.setCurrentGame(this);

        if (playerList.size() >= getNumPlayers()) {
            setup();
        }
    }

    public void removePlayer(Player player) {
        if (playerList.contains(player)) {
            playerList.remove(player);
            playerAreas.remove(player);
            player.setCurrentGame(null);

            if (currentIndex >= playerList.size()) {
                currentIndex = 0;
            }
            if (playerList.isEmpty()) {
                GamesManager.getInstance().deleteGame(getId());
            }
        }
    }

    public List<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }

    public Player getCurrentPlayer() {
        return playerList.get(currentIndex);
    }

    public PlayingPhase getCurrentPhase() {
        return currentPhase;
    }

    public PlayerData getPlayerData(Player player) {
        return playerAreas.get(player);
    }

    public PlayerData getPlayerData(String nickName) {
        for (Player player : playerList) {
            if (player.equals(new Player(nickName))) {
                return getPlayerData(player);
            }
        }
        return null;
    }

    // SETUP PHASE _____________________________________________________________________________________________________
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
            //set resource deck,Gold,And Starter
            FileReader reader = new FileReader
                    ("src/main/resources/it/polimi/sw/GC50/cardJson/physicalCardGenerated.json");
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

    private void setCommonObjectives(int quantity) {
        commonObjectives.addAll(pickObjectivesList(quantity));
    }

    public void setStartingChoices(Player player, PhysicalCard starterCard, List<ObjectiveCard> secretObjectivesList) {
        getPlayerData(player).setStartingChoices(starterCard, secretObjectivesList);
    }

    private PhysicalCard pickStarterCard() {
        return starterDeck.pop();
    }

    private List<ObjectiveCard> pickObjectivesList(int quantity) {
        List<ObjectiveCard> objectives = new ArrayList<>();
        for (int i = 0; i < quantity && !objectiveDeck.isEmpty(); i++) {
            objectives.add(objectiveDeck.pop());
        }
        return objectives;
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
            checkSetupStatus();
        }
    }

    public void setSecretObjective(Player player, ObjectiveCard secretObjective) {
        getPlayerData(player).setSecretObjective(secretObjective);
        checkPreparation(player);
        if (isReady(player)) {
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
        }
    }

    // PLAYING PHASE ___________________________________________________________________________________________________
    private void start() {
        status = GameStatus.PLAYING;
        currentIndex = 0;
        currentPhase = PlayingPhase.PLACING;
        lastTurn = false;
        System.err.println("Game \"" + id + "\" has started");
    }

    private void drawingPhase() {
        currentPhase = PlayingPhase.DRAWING;
    }

    private void nextPlayer() {
        if (lastTurn && currentIndex == playerList.size()) {
            end();
        } else {
            currentIndex = (currentIndex + 1) % playerList.size();
            currentPhase = PlayingPhase.PLACING;
        }
    }

    private void setLastTurn() {
        lastTurn = true;
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

    // END PHASE _______________________________________________________________________________________________________
    private void end() {
        status = GameStatus.ENDED;
        playerList.stream()
                .map(this::getPlayerData)
                .forEach(x -> x.setFinalScore(commonObjectives));

        List<Player> winnerList = new ArrayList<>(playerList);
        int maxScore = winnerList.stream()
                .map(this::getTotalScore)
                .max(Integer::compareTo)
                .orElse(0);
        winnerList.removeIf(player -> getTotalScore(player) < maxScore);
        if (winnerList.size() == 1) {
            System.err.println("Vincitore: " + winnerList.getFirst());
            System.err.println("Punteggio: " + maxScore);
        } else {
            int maxObjectivesScore = winnerList.stream()
                    .map(this::getObjectivesScore)
                    .max(Integer::compareTo)
                    .orElse(0);
            winnerList.removeIf(player -> getObjectivesScore(player) < maxObjectivesScore);
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

    public int getTotalScore(Player player) {
        return getPlayerData(player).getTotalScore();
    }

    public int getObjectivesScore(Player player) {
        return getPlayerData(player).getObjectivesScore();
    }

    // OTHER METHODS ___________________________________________________________________________________________________
    private void saveDeckOnFile(String name) {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/it/polimi/sw/GC50/cardJson/" + name + ".json")) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter());
            gsonBuilder.registerTypeAdapter(Objective.class, new ObjectiveAdapter());
            gsonBuilder.registerTypeAdapter(Bonus.class, new BonusAdapter());
            gsonBuilder.registerTypeAdapter(Corner.class, new CornerAdapter());
            gsonBuilder.registerTypeAdapter(PhysicalCard.class, new PhysicalCardAdapter());
            gsonBuilder.registerTypeAdapter(PlayableCard.class, new PlayableCardAdapter());
            gsonBuilder.registerTypeAdapter(GoldCard.class, new GoldCardAdapter());
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            // Convertire la lista di oggetti in formato JSON utilizzando l'oggetto Gson
            gson.toJson(starterDeck, fileWriter);
            gson.toJson(resourceDeck, fileWriter);
            gson.toJson(goldDeck, fileWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveObjectiveDeckOnFile(String name) {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/it/polimi/sw/GC50/cardJson/" + name + ".json")) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter());
            gsonBuilder.registerTypeAdapter(Objective.class, new ObjectiveAdapter());
            gsonBuilder.registerTypeAdapter(Bonus.class, new BonusAdapter());
            gsonBuilder.registerTypeAdapter(Corner.class, new CornerAdapter());
            gsonBuilder.registerTypeAdapter(PhysicalCard.class, new PhysicalCardAdapter());
            gsonBuilder.registerTypeAdapter(PlayableCard.class, new PlayableCardAdapter());
            gsonBuilder.registerTypeAdapter(GoldCard.class, new GoldCardAdapter());
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            gson.toJson(objectiveDeck, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method used to read the file json with all the cards
     * then while reading it will add automatically all the cards to the decks
     */
    private void setDeck() {

        CardType type;
        int quantity;
        Color color;
        int point;
        Corner[] cornerTmp;
        Corner[] cornerTmp2;
        List<Resource> fixedResources = new ArrayList<>();
        PlayableCard cardFront;
        PlayableCard cardBack;
        Gson gson = new Gson();

        try (FileReader reader =
                     new FileReader("src/main/resources/it/polimi/sw/GC50/cardJson/card.json")) {

            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);


            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                JsonObject front = jsonObject.getAsJsonObject("front");

                type = CardType.valueOf(jsonObject.get("type").getAsString());
                quantity = jsonObject.get("quantity").getAsInt();
                point = front.get("point").getAsInt();
                color = Color.valueOf(jsonObject.get("color").getAsString());

                if (!CardType.OBJECTIVE.equals(type)) {
                    JsonObject back = jsonObject.getAsJsonObject("back");
                    JsonObject corner = front.getAsJsonObject("corner");
                    JsonObject backCorner = back.getAsJsonObject("corner");
                    cornerTmp = cornerFromJsonObj(corner).clone();
                    cornerTmp2 = cornerFromJsonObj(backCorner).clone();

                    if (CardType.RESOURCE.equals(type) || CardType.GOLD.equals(type)) {
                        JsonArray centerBack = back.getAsJsonArray("center");
                        fixedResources = fixedValueFromJsonArray(centerBack);

                    } else if (CardType.STARTER.equals(type)) {
                        JsonArray center = front.getAsJsonArray("center");
                        fixedResources = fixedValueFromJsonArray(center);
                    }

                    if (CardType.RESOURCE.equals(type)) {
                        //this is for the resource card
                        cardFront = new PlayableCard(color, point, cornerTmp);
                        cardBack = new PlayableCard(color, 0, cornerTmp2, fixedResources);
                        resourceDeck.add(
                                new PhysicalCard(
                                        type,
                                        cardFront,
                                        cardBack,
                                        quantity
                                )
                        );

                    }
                    if (CardType.STARTER.equals(type)) {
                        //this is the creation of a starter card
                        cardFront = new PlayableCard(color, point, cornerTmp, fixedResources);
                        cardBack = new PlayableCard(color, 0, cornerTmp2);
                        starterDeck.add(
                                new PhysicalCard(
                                        type,
                                        cardFront,
                                        cardBack,
                                        quantity
                                )
                        );
                    }
                    if (CardType.GOLD.equals(type)) {
                        //the restriction are the bonus interface

                        String code = front.get("code").getAsString();
                        Bonus bonus = null;
                        if (code.equals("G00")) {
                            bonus = new BlankBonus();
                        } else if (code.equals("G01")) {
                            Resource resourceRequired = Resource.valueOf(front.get("restriction").getAsString());
                            bonus = new ResourcesBonus(resourceRequired);
                        } else if (code.equals("G02")) {

                            bonus = new CoveredCornersBonus();
                        }

                        //the requirement are the constraint
                        JsonArray requirementArray = front.getAsJsonArray("requirement");
                        List<Resource> requirement = new ArrayList<>();
                        requirement = fixedValueFromJsonArray(requirementArray);

                        cardFront = new GoldCard(color, point, bonus, null, cornerTmp, requirement);
                        cardBack = new PlayableCard(color, 0, cornerTmp2, fixedResources);

                        goldDeck.add(
                                new PhysicalCard(
                                        type,
                                        cardFront,
                                        cardBack,
                                        quantity
                                )
                        );
                    }
                } else {
                    Objective objective = null;
                    if ("OB00".equals(front.get("code").getAsString())) {
                        JsonObject monolithObjective = front.getAsJsonObject("monolithObjective");
                        objective = new MonolithObjective(
                                Color.valueOf(monolithObjective.get("color").getAsString()),
                                MonolithOrientation.valueOf(monolithObjective.get("orientation").getAsString())
                        );
                    } else if ("OB01".equals(front.get("code").getAsString())) {
                        JsonObject caveObjective = front.getAsJsonObject("caveObjective");
                        objective = new CaveObjective(
                                Color.valueOf(caveObjective.get("color").getAsString()),
                                Color.valueOf(caveObjective.get("color2").getAsString()),
                                CaveOrientation.valueOf(caveObjective.get("orientation").getAsString())
                        );
                    } else if ("OB02".equals(front.get("code").getAsString())) {
                        JsonObject identicalResourcesObjective =
                                front.getAsJsonObject("identicalResourcesObjective");
                        objective = new IdenticalResourcesObjective(
                                Resource.valueOf(identicalResourcesObjective.get("typeOfResource").getAsString()),
                                identicalResourcesObjective.get("numOfResource").getAsInt()
                        );
                    } else if ("OB03".equals(front.get("code").getAsString())) {
                        JsonObject differentResourcesObjective =
                                front.getAsJsonObject("differentResourcesObjective");
                        JsonArray typeOfDifferentResource =
                                differentResourcesObjective.getAsJsonArray("typeOfDifferentResource");
                        objective = new DifferentResourcesObjective
                                (new HashSet<>(fixedValueFromJsonArray(typeOfDifferentResource)));
                    }

                    objectiveDeck.add(
                            new ObjectiveCard(point, objective)
                    );

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //after the parsing, used the method to mix the cards of the decks
        // mixAllDecks(resourceDeck);
        //mixAllDecks(startDeck);
        //mixAllDecks(goldDeck);
        //mixObjective(objectiveDeck);
    }

    private Corner[] cornerFromJsonObj(JsonObject corner) {
        Corner[] cornerTmp = new Corner[4];
       /* cornerTmp[0] = checkCornerStatus(corner, "sw");
        cornerTmp[1] = checkCornerStatus(corner, "nw");
        cornerTmp[2] = checkCornerStatus(corner, "ne");
        cornerTmp[3] = checkCornerStatus(corner, "se");*/
        cornerTmp[0] = checkCornerStatus(corner, "nw");
        cornerTmp[1] = checkCornerStatus(corner, "ne");
        cornerTmp[2] = checkCornerStatus(corner, "sw");
        cornerTmp[3] = checkCornerStatus(corner, "se");
        return cornerTmp;
    }

    private Corner checkCornerStatus(JsonObject corner, String field) {
        if (corner.getAsJsonArray(field).get(1).isJsonNull()) {
            return new Corner(CornerStatus.valueOf(corner.getAsJsonArray(field).get(0).getAsString()), null);
        }
        return new Corner(CornerStatus.valueOf(corner.getAsJsonArray(field).get(0).getAsString()), Resource.valueOf(corner.getAsJsonArray(field).get(1).getAsString()));
    }

    private List<Resource> fixedValueFromJsonArray(JsonArray centerBack) {
        List<Resource> fixedResources = new ArrayList<>();
        for (int i = 0; i < centerBack.size(); i++)
            fixedResources.add(Resource.valueOf(centerBack.get(i).getAsString()));

        return fixedResources;
    }

    // TEST METHODS ____________________________________________________________________________________________________
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
