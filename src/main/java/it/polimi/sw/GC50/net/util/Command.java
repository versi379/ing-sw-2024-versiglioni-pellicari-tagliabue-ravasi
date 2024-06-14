package it.polimi.sw.GC50.net.util;

public enum Command {
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
    CHAT_PRIVATE,
    LEAVE,
    HELP,
    NOT_A_COMMAND
}
