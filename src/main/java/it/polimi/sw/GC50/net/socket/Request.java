package it.polimi.sw.GC50.net.socket;

import java.io.Serializable;

public enum Request implements Serializable {
    // LOBBY ////////////////////////////////////////////////////////
    SET_PLAYER,
    RESET_PLAYER,
    CREATE_GAME,
    JOIN_GAME,
    LIST_FREE_GAMES,

    // GAME /////////////////////////////////////////////////////////
    CHOOSE_OBJECTIVE,
    CHOOSE_STARTER_FACE,
    PLACE_CARD,
    DRAW_CARD,
    CHAT,
    LEAVE,
}
