package it.polimi.sw.GC50.model.chat;

import it.polimi.sw.GC50.model.lobby.Player;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatMessage1Test {

   @Test
   void MessageFirstConstructor() {
       Player player = new Player("XXX");
       LocalTime time = LocalTime.of(8,20,45);
       ChatMessage chatMessage = new ChatMessage(player,"Message 1",time);
       assertEquals(player.getNickname(),"XXX");
       assertEquals(chatMessage.getContent(),"Message 1");
       assertEquals(chatMessage.getTime(), time.toString());

   }
   @Test
   void MessageSecondConstructor(){
      Player player1 = new Player("XXX");
      Player player2 = new Player("YYY");
      LocalTime time = LocalTime.of(8,20,45);
      ChatMessage chatMessage = new ChatMessage(player1,"Message 1",time,player2);
      assertEquals(player1.getNickname(),"XXX");
      assertEquals(player2.getNickname(),"YYY");
      assertEquals(chatMessage.getContent(),"Message 1");
      assertEquals(chatMessage.getTime(), time.toString());

   }



}