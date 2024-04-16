package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.view.GUI.Gui;
import it.polimi.sw.GC50.view.TUI.Tui;

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
