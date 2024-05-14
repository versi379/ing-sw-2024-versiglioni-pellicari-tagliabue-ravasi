package it.polimi.sw.GC50.view.GUI;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.view.View;
import javafx.application.Application;

public class GuiView implements View {

    ModelMex modelmex;

    private GUIApplication guiApplication;

    public GuiView() {
        new Thread(() -> {
            Application.launch(GUIApplication.class);
        }).start();
    }

    @Override
    public String askName() {
        return "giovanni";
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
    public void updateChat(Chat chat) {

    }

    @Override
    public int SelectObjectiveCard() {
        return 0;
    }

    @Override
    public Boolean selectStarterFace() {
        return null;
    }

    @Override
    public int game() {
        return 0;
    }

    @Override
    public void updateBoard() {

    }

    @Override
    public PlaceCardMex askPlaceCard() {
        return null;
    }

    @Override
    public DrawingPosition choseWhereToDraw() {
        return null;
    }
}
