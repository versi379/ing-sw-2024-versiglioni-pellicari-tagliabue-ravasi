package it.polimi.sw.gianpaolocugola50.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.sw.gianpaolocugola50.model.card.CardType;
import it.polimi.sw.gianpaolocugola50.model.card.PhysicalCard;
import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;

import java.io.IOException;

public class PhysicalCardAdapter extends TypeAdapter<PhysicalCard> {

    @Override
    public void write(JsonWriter out, PhysicalCard card) throws IOException {
        out.beginObject();
        out.name("cardType").value(card.getCardType().toString());
        out.name("quantity").value(card.getQuantity());
        out.name("front");
        new PlayableCardAdapter().write(out, card.getFront());
        out.name("back");
        new PlayableCardAdapter().write(out, card.getBack());
        out.endObject();
    }

    @Override
    public PhysicalCard read(JsonReader in) throws IOException {
        in.beginObject();
        CardType cardType = null;
        int quantity = 0;
        PlayableCard front = null;
        PlayableCard back = null;
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "cardType":
                    cardType = CardType.valueOf(in.nextString());
                    break;
                case "quantity":
                    quantity = in.nextInt();
                    break;
                case "front":
                    front = new PlayableCardAdapter().read(in);
                    break;
                case "back":
                    back = new PlayableCardAdapter().read(in);
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return new PhysicalCard(cardType, front, back, quantity);
    }
}
