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

}
