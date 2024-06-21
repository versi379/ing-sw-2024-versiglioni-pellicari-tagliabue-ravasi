package it.polimi.sw.GC50.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.sw.GC50.model.cards.Color;
import it.polimi.sw.GC50.model.cards.Resource;
import it.polimi.sw.GC50.model.objectives.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectiveAdapter extends TypeAdapter<Objective> {

    @Override
    public void write(JsonWriter out, Objective objective) throws IOException {
        out.beginObject();
        out.name("type").value(objective.getClass().getSimpleName());
        switch (objective.getClass().getSimpleName()) {
            case "MonolithObjective":
                MonolithObjective objective1 = (MonolithObjective) objective;
                out.name("targetColor").value(objective1.getTargetColor().toString());
                out.name("orientation").value(objective1.getOrientation().toString());
                break;
            case "IdenticalResourcesObjective":
                IdenticalResourcesObjective objective2 = (IdenticalResourcesObjective) objective;
                out.name("resource").value(objective2.getTargetResource().toString());
                out.name("count").value(objective2.getCount());
                break;
            case "DifferentResourcesObjective":
                DifferentResourcesObjective objective3 = (DifferentResourcesObjective) objective;
                if (objective3.getTargetResources() != null) {
                    out.name("targetResources").beginArray();
                    for (Resource resource : objective3.getTargetResources()) {
                        out.value(resource.toString());
                    }
                    out.endArray();
                }
                break;
            case "CaveObjective":
                CaveObjective objective4 = (CaveObjective) objective;
                out.name("targetColor1").value(objective4.getTargetColor1().toString());
                out.name("targetColor2").value(objective4.getTargetColor2().toString());
                out.name("orientation").value(objective4.getOrientation().toString());

                break;
            default:

                break;
        }
        out.endObject();
    }

    @Override
    public Objective read(JsonReader in) throws IOException {
        in.beginObject();
        Objective objective = null;
        while (in.hasNext()) {
            in.nextName();
            switch (in.nextString()) {
                case "MonolithObjective":
                    in.nextName();
                    Color color1 = Color.valueOf(in.nextString());
                    in.nextName();
                    MonolithOrientation orientation = MonolithOrientation.valueOf(in.nextString());
                    in.endObject();
                    return new MonolithObjective(color1, orientation);
                case "IdenticalResourcesObjective":
                    in.nextName();
                    Resource resource = Resource.valueOf(in.nextString());
                    in.nextName();
                    int count = in.nextInt();
                    in.endObject();
                    return new IdenticalResourcesObjective(resource, count);
                case "DifferentResourcesObjective":
                    in.nextName();
                    in.beginArray();
                    List<Resource> listResources = new ArrayList<>();
                    while (in.hasNext()) {
                        listResources.add(Resource.valueOf(in.nextString()));
                    }
                    in.endArray();
                    Set<Resource> convert = new HashSet<>(listResources);
                    in.endObject();
                    return new DifferentResourcesObjective(convert);
                case "CaveObjective":
                    in.nextName();
                    Color color2 = Color.valueOf(in.nextString());
                    in.nextName();
                    Color color3 = Color.valueOf(in.nextString());
                    in.nextName();
                    CaveOrientation orientation2 = CaveOrientation.valueOf(in.nextString());
                    in.endObject();
                    return new CaveObjective(color2, color3, orientation2);
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return objective;
    }
}
