package it.polimi.sw.GC50.model.game;

import java.io.Serializable;

/**
 * Game status possibilities
 */
public enum GameStatus implements Serializable {
    WAITING, SETUP, PLAYING, ENDED
}
