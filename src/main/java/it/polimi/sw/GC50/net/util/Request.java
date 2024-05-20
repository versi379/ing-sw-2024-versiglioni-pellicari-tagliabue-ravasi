package it.polimi.sw.GC50.net.util;

public enum Request {
    //////////////////////////////////////////
    //Client to Server
    ///////////////////////////////////////////

    //LOBBY
    CREATE_GAME,
    ENTER_GAME,
    GET_FREE_MATCH,
    SET_NAME,

    //ACTIVE GAME
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

    //////////////////////////////////////////
    //Server to Client
    ///////////////////////////////////////////

    // PLAYER //////////////////////////////////////////////////////////////////////////////////////////////////////////
    NOTIFY_PLAYER_JOINED_GAME,
    NOTIFY_PLAYER_LEFT_GAME,

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    NOTIFY_GAME_SETUP,
    NOTIFY_PLAYER_READY,

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    NOTIFY_GAME_STARTED,
    NOTIFY_CARD_PLACED,
    NOTIFY_CARD_DRAWN,
    NOTIFY_NEXT_TURN,
    NOTIFY_CARD_NOT_FOUND,
    NOTIFY_CARD_NOT_PLACEABLE,
    NOTIFY_DRAWING_POSITION_NOT_AVAILABLE,
    NOTIFY_NOT_YOUR_PLACING_PHASE,
    NOTIFY_OPERATION_NOT_AVAILABLE,
    NOTIFY_INVALID_INDEX,
    NOTIFY_CHAT_MESSAGE,

    // END /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    NOTIFY_GAME_ENDED,
    GET_MODEL_RESPONSE,
    REQUEST_NOT_AVAILABLE,
    GET_CHAT_MODEL_RESPONSE
}
