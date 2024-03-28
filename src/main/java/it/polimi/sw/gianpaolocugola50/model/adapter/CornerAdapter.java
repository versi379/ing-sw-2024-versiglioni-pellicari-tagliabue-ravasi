package it.polimi.sw.gianpaolocugola50.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.sw.gianpaolocugola50.model.card.Corner;
import it.polimi.sw.gianpaolocugola50.model.card.CornerStatus;
import it.polimi.sw.gianpaolocugola50.model.card.Resource;

import java.io.IOException;

public class CornerAdapter extends TypeAdapter<Corner> {
    @Override
    public void write(JsonWriter out, Corner corner) throws IOException {
        out.beginObject();
        out.name("status").value(corner.getStatus().toString());
        if (corner.getResource()!=null ) {
            out.name("resource").value(corner.getResource().toString());
        }
        out.endObject();
    }

    @Override
    public Corner read(JsonReader in) throws IOException {
        in.beginObject();
        CornerStatus status = null;
        Resource resource = null;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "status":
                    status = CornerStatus.valueOf(in.nextString());
                    break;
                case "resource":
                    resource = Resource.valueOf(in.nextString());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return new Corner(status, resource);
    }

}
