package it.polimi.sw.gianpaolocugola50.model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;

public class ReadJSONFileWithGson {
    public static void main(String[] args) {
        try {
            // Crea un oggetto Gson
            Gson gson = new Gson();

            // Crea un BufferedReader per leggere il file JSON
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola50/cardJson/resourceCard.json"));

            // Leggi il contenuto del file JSON in una stringa
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();

            // Converte la stringa JSON in un array di oggetti Carta
            Card[] cards = gson.fromJson(jsonContent.toString(), Card[].class);

            // Stampare le informazioni delle carte
            for (Card card : cards) {
                System.out.println("Type: " + card.type);
                System.out.println("Color: " + card.color);
                System.out.println("Quantity: " + card.quantity);
                System.out.println("Point: " + card.point);
                System.out.println("Corner NW: " + card.corner.nw);
                System.out.println("Corner NE: " + card.corner.ne);
                System.out.println("Corner SW: " + card.corner.sw);
                System.out.println("Corner SE: " + card.corner.se);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Classe per rappresentare la struttura delle carte JSON
    static class Card {
        String type;
        String color;
        int quantity;
        int point;
        Corner corner;
    }

    // Classe per rappresentare la struttura degli angoli delle carte JSON
    static class Corner {
        String nw;
        String ne;
        String sw;
        String se;
    }
}
