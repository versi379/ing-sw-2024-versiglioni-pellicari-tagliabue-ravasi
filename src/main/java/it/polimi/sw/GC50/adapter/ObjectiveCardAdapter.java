package it.polimi.sw.GC50.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.sw.GC50.model.objectives.Objective;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;

import java.io.IOException;

/**
 * used to adapt objective card to json file
 */
public class ObjectiveCardAdapter extends TypeAdapter<ObjectiveCard> {

    @Override
    public void write(JsonWriter out, ObjectiveCard card) throws IOException {
        out.beginObject();
        out.name("code").value(card.getCode());
        out.name("pointsPerCompletion").value(card.getPointsPerCompletion());
        out.name("objective");
        new ObjectiveAdapter().write(out, card.getObjective());
        out.endObject();
    }

    @Override
    public ObjectiveCard read(JsonReader in) throws IOException {
        in.beginObject();
        String code = null;
        int pointsPerCompletion = 0;
        Objective objective = null;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "code":
                    code = in.nextString();
                    break;
                case "pointsPerCompletion":
                    pointsPerCompletion = in.nextInt();
                    break;
                case "objective":
                    objective = new ObjectiveAdapter().read(in);
                    break;

                default:
                    in.skipValue(); // Ignoring unknown fields
                    break;
            }
        }
        in.endObject();
        return new ObjectiveCard(code, pointsPerCompletion, objective);
    }
}
