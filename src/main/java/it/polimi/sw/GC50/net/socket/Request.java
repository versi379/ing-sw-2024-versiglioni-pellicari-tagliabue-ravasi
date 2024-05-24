package it.polimi.sw.GC50.net.socket;

public enum Request {
    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    CREATE_GAME,
    ENTER_GAME,
    GET_FREE_MATCH,
    SET_NAME,

    // GAME ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    MEX_CHAT,
    GET_MODEL,
    DRAW_CARD,
    SET_NAME_RESPONSE,
    ENTER_GAME_RESPONSE,
    CREATE_GAME_RESPONSE,
    GET_FREE_MATCH_RESPONSE,
    PLACE_CARD,
    SELECT_STARTER_FACE,
    SELECT_OBJECTIVE_CARD,
}
