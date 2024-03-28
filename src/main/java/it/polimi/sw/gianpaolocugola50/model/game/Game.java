package it.polimi.sw.gianpaolocugola50.model.game;


import it.polimi.sw.gianpaolocugola50.card.*;
import it.polimi.sw.gianpaolocugola50.model.card.*;
import it.polimi.sw.gianpaolocugola50.model.chat.Chat;
import it.polimi.sw.gianpaolocugola50.model.objective.*;


import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import com.google.gson.JsonObject;
import it.polimi.sw.gianpaolocugola50.objective.*;

import java.io.FileReader;



public class Game {
    //idGame it identify the game
    private final int idGame;

    //number of player
    private final int numPlayers;

    //list of player of one game, max 4 player
    private List<Player> players;

    //the player that is on turn
    private Player nowPlayer;

    //the winner of the game
    private Player winnerPlayer;

    //initial size of resourceDeck and goldDeck
    private int deckSize;

    //deck of resourceCard
    private Stack<PhysicalCard> resourceDeck;

    //deck of goldCard1
    private Stack<PhysicalCard> goldDeck;

    //deck of StarterCard
    private Stack<PhysicalCard> startDeck;
    //these are the card for the objective
    private Stack<ObjectiveCard> deckObjective;

    //resource and gold card on the desk
    private PhysicalCard[] revealedCards;
    //these are the objective that all the player of the game have in common
    private List<ObjectiveCard> commonObjectives;
    private Chat chat;


    public Game(int idGame, int numPlayers, Player creator) {
        this.idGame = idGame;
        this.numPlayers = numPlayers;
        this.players = new ArrayList<>();
        players.add(creator);
        this.commonObjectives = new ArrayList<>();
        this.revealedCards = new PhysicalCard[4];
        this.resourceDeck = new Stack<>();
        this.goldDeck = new Stack<>();
        this.startDeck = new Stack<>();
        this.deckObjective = new Stack<>();
        setDeck();
        setCommonObjectives(2);
        setTableAtTheStart();
    }

    public int getNumbersOfCardInGoldDeck() {
        return goldDeck.size();
    }

    public int getNumbersOfCardInResourceDeck() {
        return resourceDeck.size();
    }

    /**
     * this method is used to add the players to this game
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * this method is used to add the players to this game
     */
    public void setTableAtTheStart() {
        revealedCards[0] = goldDeck.pop();
        revealedCards[1] = goldDeck.pop();
        revealedCards[2] = resourceDeck.pop();
        revealedCards[3] = resourceDeck.pop();
    }

    /**
     * this method is used to set the starting point for the players
     * they will be given the starter card, the 2 card for the objective, the common objective
     */

    private void setAllPlayerData() {
        for (Player player : players) {
            player.setPlayerData(
                    getStarterCard(),
                    this.deckSize,
                    this.commonObjectives,
                    getSecreteObjective()
            );
        }
    }

    /**
     * this is used to get the starter card for one player, the card will be given randomly
     * because the deck are mixed from the start;
     */
    private PhysicalCard getStarterCard() {
        return startDeck.pop();
    }

    /**
     * Method used to get the board of another player
     */
    public PlayerData getOtherPlayerBoard(String id) {
        for (int i = 0; i < players.size(); i++)
            if (players.get(i).getNickName().equals(id)) {
                return players.get(i).getPlayerData();
            }
        return null;
    }

    /**
     * Method used to draw more than one card from the decks
     */
    public PhysicalCard[] drawCards(DeckType deckType, int quantity) {
        PhysicalCard[] drawCard = new PhysicalCard[quantity];
        if (DeckType.GOLD.equals(deckType)) {
            for (int i = 0; i < quantity; i++) {
                drawCard[i] = goldDeck.pop();
            }
        }
        if (DeckType.RESOURCE.equals(deckType)) {
            for (int i = 0; i < quantity; i++) {
                drawCard[i] = resourceDeck.pop();
            }
        }
        return drawCard;
    }

    /**
     * Method used to draw just one card from the decks
     */
    public PhysicalCard drawCard(DeckType deckType) {
        if (DeckType.GOLD.equals(deckType)) {
            if (goldDeck.empty())
                return null;
            return goldDeck.pop();
        }
        if (DeckType.RESOURCE.equals(deckType)) {
            if (resourceDeck.empty())
                return null;
            return resourceDeck.pop();
        }
        return null;
    }

    /**
     * Method used to draw a card from the cards on the desk
     * it will automatically replace the card on the table
     */
    public PhysicalCard drawCard(DeckType deckType, int position) {
        if (DeckType.REVEALED.equals(deckType)) {

            PhysicalCard tmp = revealedCards[position];
            //to replace the card with the same type
            if (revealedCards[position].getCardType().equals(CardType.GOLD)) {
                if (goldDeck.empty()) {
                    if (resourceDeck.empty()) {
                        revealedCards[position] = null;
                    } else {
                        revealedCards[position] = resourceDeck.pop();
                    }
                } else {
                    revealedCards[position] = goldDeck.pop();
                }
            } else if (revealedCards[position].getCardType().equals(CardType.RESOURCE)) {
                if (resourceDeck.empty()) {
                    if (goldDeck.empty()) {
                        revealedCards[position] = null;
                    } else {
                        revealedCards[position] = goldDeck.pop();
                    }
                } else {
                    revealedCards[position] = resourceDeck.pop();
                }
            }
            return tmp;
        }
        return null;
    }

    private ObjectiveCard[] getSecreteObjective() {
        return new ObjectiveCard[]{deckObjective.pop(), deckObjective.pop()};
    }

    //it is just for test// to delete!!
    public ObjectiveCard getSecreteObjective2() {
        return deckObjective.pop();
    }

    private void setCommonObjectives(int quantity) {
        for (int i = 0; i < quantity; i++) {
            if (!deckObjective.isEmpty()) {
                commonObjectives.add(deckObjective.pop());
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

                    deckObjective.add(
                            new ObjectiveCard(point, objective)
                    );

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //after the parsing, used the method to mix the cards of the decks
        mixAllDecks(resourceDeck);
        mixAllDecks(startDeck);
        mixAllDecks(goldDeck);
        mixObjective(deckObjective);
    }


    private Corner[] cornerFromJsonObj(JsonObject corner) {
        Corner[] cornerTmp = new Corner[4];
        cornerTmp[0] = checkCornerStatus(corner, "sw");
        cornerTmp[1] = checkCornerStatus(corner, "nw");
        cornerTmp[2] = checkCornerStatus(corner, "ne");
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
        ;
        return convert;
    }
}




