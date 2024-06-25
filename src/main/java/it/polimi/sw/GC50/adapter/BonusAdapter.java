package it.polimi.sw.GC50.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.sw.GC50.model.cards.*;

import java.io.IOException;

/**
 * used to adapt bonus card to json file
 */
public class BonusAdapter extends TypeAdapter<Bonus> {

    @Override
    public void write(JsonWriter out, Bonus bonus) throws IOException {

        String className = bonus.getClass().getSimpleName();
        out.beginObject();
        out.name("type").value(className);
        if (ResourcesBonus.class.equals(bonus.getClass())) {
            ResourcesBonus resourcesBonus = (ResourcesBonus) bonus;
            out.name("targetResource").value(resourcesBonus.getTargetResource().name());
        }
        out.endObject();
    }

    @Override
    public Bonus read(JsonReader in) throws IOException {
        in.beginObject();
        in.nextName();
        String type = in.nextString();

        switch (type) {
            case "ResourcesBonus" -> {
                in.nextName();
                String targetResource = in.nextString();
                in.endObject();
                return new ResourcesBonus(Resource.valueOf(targetResource));
            }
            case "BlankBonus" -> {
                in.endObject();
                return new BlankBonus();
            }
            case "CoveredCornersBonus" -> {
                in.endObject();
                return new CoveredCornersBonus();
            }
            case null, default -> in.endObject();
        }
        return null;
    }
}
