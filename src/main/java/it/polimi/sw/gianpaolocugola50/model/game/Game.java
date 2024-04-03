package it.polimi.sw.gianpaolocugola50.model.game;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.gianpaolocugola50.model.adapter.*;
import it.polimi.sw.gianpaolocugola50.model.card.*;
import it.polimi.sw.gianpaolocugola50.model.chat.Chat;
import it.polimi.sw.gianpaolocugola50.model.objective.*;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;

public class Game {
    //idGame it identify the game
    private final String id;

    //number of player
    private final int numPlayers;

    //status of the game
    private GameStatus status;

    //list of player of one game, max 4 player
    //the first player on the list is the black one
    private final List<Player> playerList;

    //index of the currently playing player
    private int currentIndex;

    //map of player's data
    private final Map<Player, PlayerData> playerAreas;

    //deck of resourceCard
    private final Stack<PhysicalCard> resourceDeck;

    //deck of goldCard
    private final Stack<PhysicalCard> goldDeck;

    private final PhysicalCard[] revealedCards;

    //deck of StarterCard
    private final Stack<PhysicalCard> starterDeck;

    //these are the card for the objective
    private final Stack<ObjectiveCard> objectiveDeck;

    //these are the objective that all the player of the game have in common
    private List<ObjectiveCard> commonObjectives;
    private Chat chat;


    public Game(String id, int numPlayers, Player creator) {
        this.id = id;
        this.numPlayers = numPlayers;
        status = GameStatus.WAITING;
        playerList = new ArrayList<>();
        currentIndex = 0;
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

    public String getId() {
        return id;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void addPlayer(Player player) {
        playerList.add(player);
        playerAreas.put(player, new PlayerData());
        player.setCurrentGame(this);
        if (playerList.size() >= numPlayers) {
            setup();
        }
    }

    public void removePlayer(Player player) {
        if (playerList.contains(player)) {
            playerList.remove(player);
            playerAreas.remove(player);
            if (currentIndex >= playerList.size()) {
                currentIndex = 0;
            }
            if (playerList.isEmpty()) {
                GamesManager.getInstance().deleteGame(id);
            }
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

    //non so a cosa possa servire ma c'era prima un metodo equivalente
    public PlayerData getPlayerData(String nickName) {
        for (Player player : playerList) {
            if (player.equals(new Player(nickName))) {
                return getPlayerData(player);
            }
        }
        return null;
    }

    private void setup() {
        status = GameStatus.SETUP;
        setDeckV2();
        setCommonObjectives(2);
        revealedCards[0] = drawCard(DrawingPosition.RESOURCEDECK);
        revealedCards[1] = drawCard(DrawingPosition.RESOURCEDECK);
        revealedCards[2] = drawCard(DrawingPosition.GOLDDECK);
        revealedCards[3] = drawCard(DrawingPosition.GOLDDECK);

        for (Player player : playerList) {
            setStartingChoices(player, getStarterCard(), getSecreteObjectivesList(2));
            addCard(player, drawCard(DrawingPosition.RESOURCEDECK));
            addCard(player, drawCard(DrawingPosition.RESOURCEDECK));
            addCard(player, drawCard(DrawingPosition.GOLDDECK));
        }
    }

    private void start() {
        status = GameStatus.PLAYING;
        System.err.println("Game \"" + id + "\" has started");
    }

    public void end() {
        status = GameStatus.FINISHED;
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
                System.err.println("Punteggio: " + maxScore);
                System.err.println("Punteggio obiettivi: " + maxObjectivesScore);
                System.err.println("Vincitori:");
                for (Player player : winnerList) {
                    System.err.print(player + " ");
                }
                System.err.println();
            }
        }
    }

    public void nextPlayer() {
        currentIndex = (currentIndex + 1) % playerList.size();
    }

    /**
     * Method used to draw just one card from the decks
     *
     * @return the top card on the deck
     * if the deck type is not right it will return null
     * also if the deck is empty it will return null
     */
    public PhysicalCard drawCard(DrawingPosition position) {
        switch (position) {
            case DrawingPosition.RESOURCEDECK -> {
                if (!resourceDeck.isEmpty()) {
                    return resourceDeck.pop();
                }
            }
            case DrawingPosition.RESOURCE1 -> {
                PhysicalCard tmp = revealedCards[0];
                if (tmp != null) {
                    if (!resourceDeck.isEmpty()) {
                        revealedCards[0] = drawCard(DrawingPosition.RESOURCEDECK);
                    } else if (!goldDeck.isEmpty()) {
                        revealedCards[0] = drawCard(DrawingPosition.GOLDDECK);
                    } else {
                        revealedCards[0] = null;
                    }
                    return tmp;
                }
            }
            case DrawingPosition.RESOURCE2 -> {
                PhysicalCard tmp = revealedCards[1];
                if (tmp != null) {
                    if (!resourceDeck.isEmpty()) {
                        revealedCards[1] = drawCard(DrawingPosition.RESOURCEDECK);
                    } else if (!goldDeck.isEmpty()) {
                        revealedCards[1] = drawCard(DrawingPosition.GOLDDECK);
                    } else {
                        revealedCards[1] = null;
                    }
                    return tmp;
                }
            }
            case DrawingPosition.GOLDDECK -> {
                if (!goldDeck.isEmpty()) {
                    return goldDeck.pop();
                }
            }
            case DrawingPosition.GOLD1 -> {
                PhysicalCard tmp = revealedCards[2];
                if (tmp != null) {
                    if (!goldDeck.isEmpty()) {
                        revealedCards[2] = drawCard(DrawingPosition.GOLDDECK);
                    } else if (!resourceDeck.isEmpty()) {
                        revealedCards[2] = drawCard(DrawingPosition.RESOURCEDECK);
                    } else {
                        revealedCards[2] = null;
                    }
                    return tmp;
                }
            }
            case DrawingPosition.GOLD2 -> {
                PhysicalCard tmp = revealedCards[3];
                if (tmp != null) {
                    if (!goldDeck.isEmpty()) {
                        revealedCards[3] = drawCard(DrawingPosition.GOLDDECK);
                    } else if (!resourceDeck.isEmpty()) {
                        revealedCards[3] = drawCard(DrawingPosition.RESOURCEDECK);
                    } else {
                        revealedCards[3] = null;
                    }
                    return tmp;
                }
            }
        }
        // invalid draw
        return null;
    }

    public int resourceDeckSize() {
        return resourceDeck.size();
    }

    public int goldDeckSize() {
        return goldDeck.size();
    }

    public PhysicalCard getStarterCard() {
        return starterDeck.pop();
    }

    public List<ObjectiveCard> getSecreteObjectivesList(int quantity) {
        List<ObjectiveCard> secretObjectives = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            if (!objectiveDeck.isEmpty()) {
                secretObjectives.add(objectiveDeck.pop());
            }
        }
        return secretObjectives;
    }

    private void setCommonObjectives(int quantity) {
        for (int i = 0; i < quantity; i++) {
            if (!objectiveDeck.isEmpty()) {
                commonObjectives.add(objectiveDeck.pop());
            }
        }
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
     * Method used to read the file json with all the cards
     */
    private void setDeckV2() {
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
            FileReader reader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola50/cardJson/physicalCardGenerated.json");
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
            reader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola50/cardJson/objectiveCardGenerated.json");
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

    private void saveDeckOnFile(String name) {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/it/polimi/sw/gianpaolocugola50/cardJson/" + name + ".json")) {
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
        try (FileWriter fileWriter = new FileWriter("src/main/resources/it/polimi/sw/gianpaolocugola50/cardJson/" + name + ".json")) {
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

        try (FileReader reader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola50/cardJson/card.json")) {

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
                                Color.valueOf(caveObjective.get("color").getAsString()), Color.valueOf(caveObjective.get("color2").getAsString()),
                                CaveOrientation.valueOf(caveObjective.get("orientation").getAsString())
                        );
                    } else if ("OB02".equals(front.get("code").getAsString())) {
                        JsonObject identicalResourcesObjective = front.getAsJsonObject("identicalResourcesObjective");
                        objective = new IdenticalResourcesObjective(
                                Resource.valueOf(identicalResourcesObjective.get("typeOfResource").getAsString()),
                                identicalResourcesObjective.get("numOfResource").getAsInt()
                        );
                    } else if ("OB03".equals(front.get("code").getAsString())) {
                        JsonObject differentResourcesObjective = front.getAsJsonObject("differentResourcesObjective");
                        JsonArray typeOfDifferentResource = differentResourcesObjective.getAsJsonArray("typeOfDifferentResource");
                        objective = new DifferentResourcesObjective(FromListToSet(fixedValueFromJsonArray(typeOfDifferentResource)));
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

    private Set<Resource> FromListToSet(List<Resource> list) {
        Set<Resource> convert = new HashSet<>(list);
        return convert;
    }

    //metodi playerData
    //__________________________________________________________________________
    public void setStartingChoices(Player player, PhysicalCard starterCard, List<ObjectiveCard> secretObjectivesList) {
        getPlayerData(player).setStartingChoices(starterCard, secretObjectivesList);
    }

    public PhysicalCard getStarterCard(Player player) {
        return getPlayerData(player).getStarterCard();
    }

    public List<ObjectiveCard> getSecretObjectivesList(Player player) {
        return getPlayerData(player).getSecretObjectivesList();
    }

    public void setStarterCard(Player player, PlayableCard starterCard) {
        getPlayerData(player).setStarterCard(starterCard);
        checkSetupStatus();
    }

    public void setSecretObjective(Player player, ObjectiveCard secretObjective) {
        getPlayerData(player).setSecretObjective(secretObjective);
        checkSetupStatus();
    }

    private void checkSetupStatus() {
        if (playerList.stream()
                .map(this::getPlayerData)
                .allMatch(PlayerData::isReady)) {
            start();
        }
    }

    public void placeCard(Player player, PlayableCard card, int x, int y) {
        getPlayerData(player).placeCard(card, x, y);
    }

    public void addCard(Player player, PhysicalCard card) {
        getPlayerData(player).addCard(card);
    }

    public void removeCard(Player player, int index) {
        getPlayerData(player).removeCard(index);
    }

    public List<PhysicalCard> getHand(Player player) {
        return getPlayerData(player).getHand();
    }

    public int getTotalScore(Player player) {
        return getPlayerData(player).getTotalScore();
    }

    public int getObjectivesScore(Player player) {
        return getPlayerData(player).getObjectivesScore();
    }

    //test
    public ObjectiveCard getSecreteObjective2() {
        return objectiveDeck.pop();
    }
}
