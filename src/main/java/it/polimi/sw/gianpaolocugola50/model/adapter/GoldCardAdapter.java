package it.polimi.sw.gianpaolocugola50.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.sw.gianpaolocugola50.model.card.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoldCardAdapter extends TypeAdapter<GoldCard> {
    @Override
    public void write(JsonWriter out, GoldCard card) throws IOException {
        out.beginObject();
        out.name("color").value(card.getColor().toString());
        out.name("points").value(card.getPoints());
        out.name("bonus");
        new BonusAdapter().write(out, card.getBonus());
        if (card.getFixedResources() != null) {
            out.name("fixedResources").beginArray();
            for (Resource resource : card.getFixedResources()) {
                out.value(resource.toString());
            }
            out.endArray();
        }
        if (card.getConstraintList() != null) {
            out.name("constraint").beginArray();
            for (Resource resource : card.getConstraintList()) {
                out.value(resource.toString());
            }
            out.endArray();
        }
        out.name("corners").beginArray();
        new CornerAdapter().write(out, card.getNwCorner());
        new CornerAdapter().write(out, card.getNeCorner());
        new CornerAdapter().write(out, card.getSwCorner());
        new CornerAdapter().write(out, card.getSeCorner());
        out.endArray();
        out.endObject();
    }

    @Override
    public GoldCard read(JsonReader in) throws IOException {
        in.beginObject();
        Color color = null;
        int points = 0;
        Bonus bonus = new BlankBonus();
        List<Resource> fixedResources = new ArrayList<>();
        List<Resource> constraint = new ArrayList<>();
        Corner[] corners = new Corner[4];
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "color":
                    color = Color.valueOf(in.nextString());
                    break;
                case "points":
                    points = in.nextInt();
                    break;
                case "bonus":
                    new BonusAdapter().read(in);
                    break;
                case "fixedResources":
                    in.beginArray();
                    while (in.hasNext()) {
                        fixedResources.add(Resource.valueOf(in.nextString()));
                    }
                    in.endArray();
                    break;
                case "constraint":
                    in.beginArray();
                    while (in.hasNext()) {
                        constraint.add(Resource.valueOf(in.nextString()));
                    }
                    in.endArray();
                    break;
                case "corners":
                    in.beginArray();
                    corners[1] = new CornerAdapter().read(in);
                    corners[2] = new CornerAdapter().read(in);
                    corners[0] = new CornerAdapter().read(in);
                    corners[3] = new CornerAdapter().read(in);

                    in.endArray();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return new GoldCard(color, points, bonus, fixedResources, corners, constraint);
    }
}

