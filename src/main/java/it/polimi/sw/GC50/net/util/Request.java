package it.polimi.sw.GC50.net.util;

public enum Request {
    //////////////////////////////////////////
    //Client to Server
    ///////////////////////////////////////////
    JOINGAME,
    CREATEGAME,
    QUITGAME,
    GETGAME,
    GETNUMBEROFPLAYER,
    GETMODEL,
    GETSTARTERCARD,
    GETCOMMONOBJECTIVE,
    GETSECRETOBJECTIVE,
    SELECTOBJECTIVE,
    PLACECARD,
    DRAWCARDGOLD0,
    DRAWCARDGOLD1,
    DRAWCARDGOLD2,
    DRAWRESOURCE0,
    DRAWRESOURCE1,
    DRAWRESOURCE2,
    MEXCHAT,
    STARTERFACE,
    GETTURN,
    GAMENOTFOUND,
    CREATE_GAME,
    ENTER_GAME,
    GET_FREE_MATCH,
    SET_NAME,
    //////////////////////////////////////////
    //Server to Client
    ///////////////////////////////////////////
    SET_NAME_RESPONSE,
    ENTER_GAME_RESPONSE,
    CREATE_GAME_RESPONSE,
    GET_FREE_MATCH_RESPONSE,
    GETMODEL_RESPONSE,
    /////////////////////////////////////////
    ///ACTIVE GAME NOTIFY FROM MODEL
    /////////////////////////////////////////
    NOTIFY_ALL_PLAYERS_JOINED_GAME,
    NOTIFY_SETUP,
    NOTIFY_NOT_YOUR_TURN,
    NOTIFY_CARD_NOT_FOUND,
    NOTIFY_CARD_NOT_PLACEABLE,
    NOTIFY_NOT_YOUR_PLACING_PHASE, NOTIFY_CARD_PLACED, NOTIFY_OPERATION_NOT_AVAILABLE, NOTIFY_INVALID_INDEX, NOTIFY_POSITION_DRAWING_NOT_AVAILABLE, PLACE_CARD, SELECT_STARTER_FACE, SELECT_OBJECTIVE_CARD, DRAW_CARD, NOTIFY_CHAT_MESSAGE;
}
