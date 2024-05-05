package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import it.polimi.sw.GC50.net.util.Request;

import java.util.List;

public interface GameInterface {

    // GENERAL INFO ////////////////////////////////////////////////////////////////////////////////////////////////////
    String getId();

    int getNumPlayers();

    GameStatus getStatus();

    // PLAYERS MANAGEMENT //////////////////////////////////////////////////////////////////////////////////////////////
    boolean containsPlayer(Player player);

    boolean containsPlayer(String nickname);

    void addPlayer(Player player);

    void removePlayer(Player player);

    List<Player> getPlayerList();

    Player getCurrentPlayer();

    PlayerData getPlayerData(Player player);

    PlayerData getPlayerData(String nickname);

    // SETUP PHASE /////////////////////////////////////////////////////////////////////////////////////////////////////
    List<ObjectiveCard> getCommonObjectives();

    PhysicalCard getStarterCard(Player player);

    List<ObjectiveCard> getSecretObjectivesList(Player player);
    
    void setStarterCard(Player player, PlayableCard starterCard);

    void setSecretObjective(Player player, ObjectiveCard secretObjective);

    boolean isReady(Player player);

    // PLAYING PHASE ///////////////////////////////////////////////////////////////////////////////////////////////////
    PlayingPhase getCurrentPhase();

    boolean isLastRound();

    void placeCard(Player player, PlayableCard card, int x, int y);

    /* Getter utili per la view (senza passare per getPlayerData(...))
    Corner getCorner(Player player, int x, int y);

    PlayableCard getCard(Player player, int x, int y);

    ObjectiveCard getSecretObjective(Player player);
     */

    PhysicalCard pickCard(DrawingPosition position);

    List<PhysicalCard> getHand(Player player);

    void addCard(Player player, PhysicalCard card);

    void removeCard(Player player, int index);

    void sendMessageInChat(Player player, String message);

    // END PHASE ///////////////////////////////////////////////////////////////////////////////////////////////////////
    List<Player> getWinnerList();

    int getTotalScore(Player player);

    int getObjectivesScore(Player player);

    // OTHER METHODS ///////////////////////////////////////////////////////////////////////////////////////////////////
    void error(Request request, Object arg);

    // TEST METHODS ////////////////////////////////////////////////////////////////////////////////////////////////////
    List<ObjectiveCard> getObjectives(int quantity);

    int resourceDeckSize();

    int goldDeckSize();

    void forceEnd();
}
