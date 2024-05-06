package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.view.View;

public class GuiView implements View {
    ModelMex modelmex;

    //grafica che chiede il nome
    @Override
    public String askName() {
        return null;
    }

    @Override
    public String askGameName() {
        return null;
    }

    @Override
    public int askNumberOfPlayer() {
        return 0;
    }

    @Override
    public void waitPlayer() {

    }

    @Override
    public int joinorcreate() {
        return 0;
    }

    @Override
    public void allPlayerReady() {

    }

    @Override
    public void addModel(ModelMex modelmex) {
        this.modelmex = modelmex;
    }

    @Override
    public int SelectObjectiveCard() {
        return 0;
    }

    @Override
    public Boolean selectStarterFace() {
        return null;
    }
}
