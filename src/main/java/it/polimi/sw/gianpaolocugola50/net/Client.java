package it.polimi.sw.gianpaolocugola50.net;

import it.polimi.sw.gianpaolocugola50.view.GUI.Gui;
import it.polimi.sw.gianpaolocugola50.view.TUI.Tui;

import java.io.IOException;
import java.rmi.NotBoundException;

public class Client {
    public static void main(String[] args) {
        String view="tui";

        if (view.equals("tui"))
            tuiStart();
        else if (view.equals("gui"))
            guiStart();
    }

    private static void guiStart() {
        Gui gui = new Gui();
    }

    private static void tuiStart() {
        Tui tui = new Tui();
        tui.start();
    }

}
