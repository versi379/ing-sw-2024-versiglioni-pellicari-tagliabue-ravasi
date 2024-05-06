package it.polimi.sw.GC50.net.gameMexNet;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.Resource;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.CornerPointer;
import it.polimi.sw.GC50.model.game.PlayerData;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelMex implements Serializable {
    private final PlayerData Playerdata;
    private final Chat chat;
    private final String currentPlayer;
    private final ArrayList<CardsMatrix> cardsMatrixAvengers;

    public ModelMex(PlayerData playerdata, Chat chat, String currentPlayer, ArrayList<CardsMatrix> cardsMatrixAvengers) {
        Playerdata = playerdata;
        this.chat = chat;
        this.currentPlayer = currentPlayer;
        this.cardsMatrixAvengers = cardsMatrixAvengers;
    }

    public PlayerData getPlayerdata() {
        return Playerdata;
    }

    public Chat getChat() {
        return chat;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<CardsMatrix> getCardsMatrixAvengers() {
        return cardsMatrixAvengers;
    }
}
