package it.polimi.sw.GC50.model.chat;

import it.polimi.sw.GC50.model.lobby.Player;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

   @Test
   void MessageFirstConstructor() {
       Player player = new Player("XXX");
       LocalTime time = LocalTime.of(8,20,45);
       Message message = new Message(player,"Message 1",time);
       assertEquals(player.getNickname(),"XXX");
       assertEquals(message.getContent(),"Message 1");
       assertEquals(message.getTime(), time.toString());

   }
   @Test
   void MessageSecondConstructor(){
      Player player1 = new Player("XXX");
      Player player2 = new Player("YYY");
      LocalTime time = LocalTime.of(8,20,45);
      Message message = new Message(player1,"Message 1",time,player2);
      assertEquals(player1.getNickname(),"XXX");
      assertEquals(player2.getNickname(),"YYY");
      assertEquals(message.getContent(),"Message 1");
      assertEquals(message.getTime(), time.toString());

   }



}