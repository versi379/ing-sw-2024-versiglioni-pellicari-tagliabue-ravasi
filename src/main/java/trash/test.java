package trash;

import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.game.PlayerData;
import it.polimi.sw.GC50.model.lobby.Player;

public class test {
    public static void main(String[] args) {
        PlayerData pl= new PlayerData(40);
        Player player= new Player("luca");
        Game game2 = new Game("Partita", 1, 20);
        game2.addPlayer(player);


        PhysicalCard card;
        card = game2.pickCard(DrawingPosition.GOLDDECK);
        pl.placeCard(card.getBack(),0,0);
        card = game2.pickCard(DrawingPosition.GOLDDECK);
        pl.placeCard(card.getBack(),1,1);
        card = game2.pickCard(DrawingPosition.GOLDDECK);
        card = game2.pickCard(DrawingPosition.GOLDDECK);
        pl.placeCard(card.getBack(),40,40);
        card = game2.pickCard(DrawingPosition.GOLDDECK);
        pl.placeCard(card.getBack(),41,41);
        card = game2.pickCard(DrawingPosition.GOLDDECK);
        pl.placeCard(card.getBack(),40,42);
        card = game2.pickCard(DrawingPosition.GOLDDECK);
        pl.placeCard(card.getBack(),39,41);
        card = game2.pickCard(DrawingPosition.GOLDDECK);
        pl.placeCard(card.getBack(),39,39);

        PlayableCard[] deck= new PlayableCard[6];
        for(int i=0;i<6;i++){
            card = game2.pickCard(DrawingPosition.GOLDDECK);
            deck[i]=card.getFront();
        }
        PhysicalCard[] cards= new PhysicalCard[2];
        for(int i=0;i<2;i++){
            card = game2.pickCard(DrawingPosition.RESOURCEDECK);
            cards[i]=card;
        }

        CardsMatrix mat3= pl.getCardsArea();
        PrintGameArea ob= new PrintGameArea();
        ob.addPlayerArea("luca",10, Color.RED,mat3);
        ob.addPlayerArea("luca",10, Color.BLUE,mat3);
        ob.addPlayerArea("luca",10, Color.GREEN,mat3);
        ob.updateDeck(deck);
        ob.updatePlayerHand(cards);
        //ob.printallboard(false,0);
        ob.printAllBoard(true,0);
        //.print(0);

        //ob.updateDeck();
    }
}
