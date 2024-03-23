package it.polimi.sw.gianpaolocugola50.model.game;

import it.polimi.sw.gianpaolocugola50.model.card.*;
import it.polimi.sw.gianpaolocugola50.model.objective.ObjectiveCard;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Stack;


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

    //resource and gold card on the desk
    private PhysicalCard[] revealedCards;
    //these are the objective that all the player of the game have in common
    private ObjectiveCard[] commonObjectives;
    //these are the card for the objective
    private Stack<ObjectiveCard> deckObjective;


    public Game(int idGame, int numPlayers, Player creator) {
        this.idGame = idGame;
        this.numPlayers = numPlayers;
        this.players = new ArrayList<>();
        players.add(creator);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void setAllPlayerData() {
        for (Player player : players) {
            player.setPlayerData(getStarterCard(), deckSize);
        }
    }

    //this is used to get the starter card for one player, the card will be given randomly;
    private PhysicalCard getStarterCard() {
        return startDeck.pop();
    }

    /**
     * Method used to draw more than one card from the decks
     */
    public PhysicalCard[] drawCards(DeckType deckType, int quantity) {
        PhysicalCard[] drawCard = new PhysicalCard[quantity];
        if (DeckType.GOLD.equals(deckType)) {
                for (int i = 0; i < quantity; i++) {
               drawCard[i]=goldDeck.pop();
            }
        }
        if (DeckType.RESOURCE.equals(deckType)) {
            for (int i = 0; i < quantity; i++) {
                drawCard[i]=resourceDeck.pop();
            }
        }
        return drawCard;
    }

    /**
     * Method used to draw just one card from the decks
     */
    public PhysicalCard drawCard(DeckType deckType){
        if (DeckType.GOLD.equals(deckType)) {
           return goldDeck.pop();
        }
        if (DeckType.RESOURCE.equals(deckType)) {
           return resourceDeck.pop();
        }
        return null;
    }
    /**
     * Method used to draw a card from the cards on the desk
     *
     */
    public PhysicalCard drawCard(DeckType deckType,int position){
        if (DeckType.REVEALED.equals(deckType)) {
            return revealedCards[position];
        }
        return null;
    }
    public ObjectiveCard[] getSecreteObjective(){
        return new ObjectiveCard[]{deckObjective.pop(), deckObjective.pop()};
    }
    private void setCommonObjectives(int quantity){
        for(int i=0;i<quantity;i++){
            if(!deckObjective.isEmpty()){
                commonObjectives[i]=deckObjective.pop();
            }
        }
    }

    private void meshAllDecks() {

    }

    //read the file json and set the decks with the cards
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
                JsonObject back = jsonObject.getAsJsonObject("back");
                JsonObject corner = front.getAsJsonObject("corner");
                JsonObject backCorner = back.getAsJsonObject("corner");


                type = CardType.valueOf(jsonObject.get("type").getAsString());
                quantity = jsonObject.get("quantity").getAsInt();
                point = front.get("point").getAsInt();
                color = Color.valueOf(jsonObject.get("color").getAsString());
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
                    //this is for the resource card
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
                    JsonArray restrictionArray = front.getAsJsonArray("restriction");
                    String code = front.get("code").getAsString();
                    JsonArray requirementArray = front.getAsJsonArray("requirement");
                    cardFront = new GoldCard(color, point, null, fixedResources, cornerTmp, null);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Corner[] cornerFromJsonObj(JsonObject corner) {
        Corner[] cornerTmp = new Corner[4];
        cornerTmp[0] = new Corner(CornerStatus.valueOf(corner.getAsJsonArray("sw").get(0).getAsString()), Resource.valueOf(corner.getAsJsonArray("sw").get(1).getAsString()));
        cornerTmp[1] = new Corner(CornerStatus.valueOf(corner.getAsJsonArray("nw").get(0).getAsString()), Resource.valueOf(corner.getAsJsonArray("nw").get(1).getAsString()));
        cornerTmp[2] = new Corner(CornerStatus.valueOf(corner.getAsJsonArray("ne").get(0).getAsString()), Resource.valueOf(corner.getAsJsonArray("ne").get(1).getAsString()));
        cornerTmp[3] = new Corner(CornerStatus.valueOf(corner.getAsJsonArray("se").get(0).getAsString()), Resource.valueOf(corner.getAsJsonArray("se").get(1).getAsString()));
        return cornerTmp;
    }

    private static List<Resource> fixedValueFromJsonArray(JsonArray centerBack) {
        List<Resource> fixedResources = null;
        for (int i = 0; i < centerBack.size(); i++)
            if (fixedResources != null) {
                fixedResources.add(Resource.valueOf(centerBack.get(i).getAsString()));
            }
        return fixedResources;
    }
}




