package it.polimi.sw.gianpaolocugola50.model.game;

import it.polimi.sw.gianpaolocugola50.model.card.Color;
import it.polimi.sw.gianpaolocugola50.model.card.Corner;
import it.polimi.sw.gianpaolocugola50.model.card.PhysicalCard;
import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;
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
    //list of player of one game, max 4 player


    //the player that is on turn
    private Player nowPlayer;

    //the winner of the game
    private Player winnerPlayer;

    //deck of resourceCard
    private Stack<PlayableCard> resourceDeck;

    //deck of goldCard
    private Stack<PhysicalCard> goldDeck;

    //deck of StarterCard
    private Stack<PhysicalCard> StartDeck;

    //resource and gold card on the desk
    private PhysicalCard[] revealedCards;

    //gold card on the desk
    private ObjectiveCard[] commonObjectives;


    public Game(int idGame, int numPlayers, Player creator) {
        this.idGame = idGame;
        this.numPlayers = 0;
        this.players = new ArrayList<>();
        players.add(creator);
    }

    public void join(Player player) {
        players.add(player);
        if (players.size() == numPlayers) {
            start();
        }
    }

    public void start() {

    }

    public void setAllPlayerData() {
        for (Player player : players) {
            player.setPlayerData(getStarterCard());
        }
    }

    private PlayableCard getStarterCard() {
        return null;
    }

    public void setResourceDeck() {
        this.resourceDeck = new Stack<>();
        String type;
        Color color;
        int quantity;
        int point;
        String ne;
        String nw;
        String se;
        String sw;
        PlayableCard card1;
        Corner[] corners;

        JsonElement jsonElement = getResourceFromJson();
        try {
            // Verifica se il JSON è un array o un singolo oggetto
            if (jsonElement.isJsonArray()) {
                // Se il JSON è un array, leggi ciascun oggetto
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    // create card
                    type = jsonObject.get("type").getAsString();
                    color = Color.valueOf(jsonObject.get("color").getAsString());
                    point = jsonObject.get("point").getAsInt();
                    card1 = new PlayableCard(color, point);
                    resourceDeck.add(card1);
                    System.out.println(card1.getColor());
                    /*JsonObject corner = jsonObject.getAsJsonObject("corner");

                    nw=corner.get("nw").getAsString();
                    ne=corner.get("ne").getAsString();
                    sw=corner.get("sw").getAsString();
                    se=corner.get("se").getAsString();
                    */

                }
            } else if (jsonElement.isJsonObject()) {
                // Se il JSON è un singolo oggetto, leggilo direttamente
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                // Stampare le informazioni dell'oggetto
                // create card
                type = jsonObject.get("type").getAsString();
                color = Color.valueOf(jsonObject.get("color").getAsString());
                point = jsonObject.get("point").getAsInt();
                card1 = new PlayableCard(color, point);
                resourceDeck.add(card1);
                System.out.println(card1.getColor());
            } else {

            }
        } catch (Exception e) {

        }

    }

    private JsonElement getResourceFromJson() {
        try {
            // Crea un oggetto Gson
            Gson gson = new Gson();

            // Leggi il file JSON come oggetto JsonElement
            Reader reader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola50/cardJson/resourceCard.json");
            JsonElement jsonElement = gson.fromJson(reader, JsonElement.class);
            return jsonElement;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
