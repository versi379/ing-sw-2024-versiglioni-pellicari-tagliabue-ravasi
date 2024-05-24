package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.GameView;

public interface Client {
    public GameView getGameView();
    public void addCommand(Command command, String[] args);
}
