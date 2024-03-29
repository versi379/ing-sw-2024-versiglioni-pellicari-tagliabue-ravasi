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
    private final int idGame;

    //number of player
    private final int numPlayers;

    //list of player of one game, max 4 player
    //the first player on the list is the black one
    private final List<Player> playerList;
    private final Map<Player, PlayerData> playerDatas;

    //deck of resourceCard
    private final Stack<PhysicalCard> resourceDeck;

    //deck of goldCard
    private final Stack<PhysicalCard> goldDeck;

    private final PhysicalCard[] drawableCards;

    //deck of StarterCard
    private final Stack<PhysicalCard> startDeck;

    //these are the card for the objective
    private final Stack<ObjectiveCard> objectiveDeck;

    //these are the objective that all the player of the game have in common
    private List<ObjectiveCard> commonObjectives;
    private Chat chat;


    public Game(int idGame, int numPlayers, Player creator) {
        this.idGame = idGame;
        this.numPlayers = numPlayers;
        this.playerList = new ArrayList<>();
        this.playerDatas = new HashMap<>();
        this.resourceDeck = new Stack<>();
        this.goldDeck = new Stack<>();
        this.drawableCards = new PhysicalCard[4];
        this.startDeck = new Stack<>();
        this.objectiveDeck = new Stack<>();
        this.commonObjectives = new ArrayList<>();
        this.chat = new Chat();
        setDeckV2();
        setCommonObjectives(2);
        setTableAtTheStart();
        addPlayer(creator);
    }

    public void addPlayer(Player player) {
        playerList.add(player);
        playerDatas.put(player, new PlayerData(resourceDeckSize()));
    }

    public PlayerData getPlayerData(Player player) {
        return playerDatas.get(player);
    }

    public PlayerData getPlayerData(String nickName) {
        for (Player player : playerList) {
            if (player.equals(new Player(nickName))) {
                return getPlayerData(player);
            }
        }
        return null;
    }

    public int resourceDeckSize() {
        return resourceDeck.size();
    }

    public int goldDeckSize() {
        return goldDeck.size();
    }

    public void setTableAtTheStart() {
        drawableCards[0] = drawCard(DrawingPosition.RESOURCEDECK);
        drawableCards[1] = drawCard(DrawingPosition.RESOURCEDECK);
        drawableCards[2] = drawCard(DrawingPosition.GOLDDECK);
        drawableCards[3] = drawCard(DrawingPosition.GOLDDECK);
    }

    /**
     * this is used to get the starter card for one player, the card will be given randomly
     * because the deck are mixed from the start;
     */
    private PhysicalCard getStarterCard() {
        return startDeck.pop();
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
                PhysicalCard tmp = drawableCards[0];
                if (tmp != null) {
                    if (!resourceDeck.isEmpty()) {
                        drawableCards[0] = drawCard(DrawingPosition.RESOURCEDECK);
                    } else if (!goldDeck.isEmpty()) {
                        drawableCards[0] = drawCard(DrawingPosition.GOLDDECK);
                    } else {
                        drawableCards[0] = null;
                    }
                    return tmp;
                }
            }
            case DrawingPosition.RESOURCE2 -> {
                PhysicalCard tmp = drawableCards[1];
                if (tmp != null) {
                    if (!resourceDeck.isEmpty()) {
                        drawableCards[1] = drawCard(DrawingPosition.RESOURCEDECK);
                    } else if (!goldDeck.isEmpty()) {
                        drawableCards[1] = drawCard(DrawingPosition.GOLDDECK);
                    } else {
                        drawableCards[1] = null;
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
                PhysicalCard tmp = drawableCards[2];
                if (tmp != null) {
                    if (!goldDeck.isEmpty()) {
                        drawableCards[2] = drawCard(DrawingPosition.GOLDDECK);
                    } else if (!resourceDeck.isEmpty()) {
                        drawableCards[2] = drawCard(DrawingPosition.RESOURCEDECK);
                    } else {
                        drawableCards[2] = null;
                    }
                    return tmp;
                }
            }
            case DrawingPosition.GOLD2 -> {
                PhysicalCard tmp = drawableCards[3];
                if (tmp != null) {
                    if (!goldDeck.isEmpty()) {
                        drawableCards[3] = drawCard(DrawingPosition.GOLDDECK);
                    } else if (!resourceDeck.isEmpty()) {
                        drawableCards[3] = drawCard(DrawingPosition.RESOURCEDECK);
                    } else {
                        drawableCards[3] = null;
                    }
                    return tmp;
                }
            }
        }
        // invalid draw
        return null;
    }

    private ObjectiveCard[] getSecreteObjective() {
        return new ObjectiveCard[]{objectiveDeck.pop(), objectiveDeck.pop()};
    }

    //it is just for test// to delete!!
    public ObjectiveCard getSecreteObjective2() {
        return objectiveDeck.pop();
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
                    startDeck.add(card);
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
        mixAllDecks(startDeck);
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
            gson.toJson(startDeck, fileWriter);
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
                        startDeck.add(
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
}




