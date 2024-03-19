package it.polimi.sw.gianpaolocugola50.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.Reader;

public class a{
    public static void main(String[] args) {
        try {
            // Crea un oggetto Gson
            Gson gson = new Gson();

            // Leggi il file JSON come oggetto JsonElement
            Reader reader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola50/cardJson/resourceCard.json");
            JsonElement jsonElement = gson.fromJson(reader, JsonElement.class);

            // Verifica se il JSON è un array o un singolo oggetto
            if (jsonElement.isJsonArray()) {
                // Se il JSON è un array, leggi ciascun oggetto
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    // Stampare le informazioni dell'oggetto
                    printCardInfo(jsonObject);
                }
            } else if (jsonElement.isJsonObject()) {
                // Se il JSON è un singolo oggetto, leggilo direttamente
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                // Stampare le informazioni dell'oggetto
                printCardInfo(jsonObject);
            } else {
                System.out.println("Il JSON non è valido.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per stampare le informazioni di una carta
    private static void printCardInfo(JsonObject jsonObject) {
        System.out.println("Type: " + jsonObject.get("type").getAsString());
        System.out.println("Color: " + jsonObject.get("color").getAsString());
        System.out.println("Quantity: " + jsonObject.get("quantity").getAsInt());
        System.out.println("Point: " + jsonObject.get("point").getAsInt());
        JsonObject corner = jsonObject.getAsJsonObject("corner");
        System.out.println("NW Corner: " + corner.get("nw").getAsString());
        System.out.println("NE Corner: " + corner.get("ne").getAsString());
        System.out.println("SW Corner: " + corner.get("sw").getAsString());
        System.out.println("SE Corner: " + corner.get("se").getAsString());
        System.out.println();
    }
}
