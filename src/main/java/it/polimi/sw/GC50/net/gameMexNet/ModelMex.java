package it.polimi.sw.GC50.net.gameMexNet;

import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelMex implements Serializable {
    private final PlayerData Playerdata;
    private final Chat chat;
    private final PlayableCard[] drawingCard;
    private final ArrayList<SinglePlayerArea> otherPlayersInfo;
    private final String currentPlayer;

        public class SinglePlayerArea implements Serializable{
            private String nickname;
            private int point;
            private Color color;
            private CardsMatrix cardsMatrix;

            public SinglePlayerArea(String nickname, int point, Color color, CardsMatrix cardsMatrix) {
                this.nickname = nickname;
                this.point = point;
                this.color = color;
                this.cardsMatrix = cardsMatrix;
            }

            public void setPoint(int point) {
                this.point = point;
            }

            public String getNickname() {
                return nickname;
            }

            public int getPoint() {
                return point;
            }

            public Color getColor() {
                return color;
            }

            public CardsMatrix getCardsMatrix() {
                return cardsMatrix;
            }

            public void setCardsMatrix(CardsMatrix cardsMatrix) {
                this.cardsMatrix = cardsMatrix;
            }
    }
    public ModelMex(PlayerData playerdata, Chat chat, String currentPlayer, ArrayList<String> name, ArrayList<CardsMatrix> board, ArrayList<Color> color, ArrayList<Integer> point, PlayableCard[] drawingCard) {
        Playerdata = playerdata;
        this.chat = chat;
        this.drawingCard = drawingCard;
        this.otherPlayersInfo = new ArrayList<>();
        for(int i=0;i<name.size();i++){
            this.otherPlayersInfo.add(new SinglePlayerArea(name.get(i),point.get(i),color.get(i),board.get(i)));
        }
        this.currentPlayer = currentPlayer;

    }

    public PlayableCard[] getDrawingCard() {
        return drawingCard;
    }

    public PlayerData getPlayerdata() {
        return Playerdata;
    }

    public Chat getChat() {
        return chat;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<SinglePlayerArea> getOtherPlayersInfo() {
        return otherPlayersInfo;
    }
}
