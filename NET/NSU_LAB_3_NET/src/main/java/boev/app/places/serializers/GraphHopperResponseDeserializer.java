package boev.app.places.serializers;

import boev.app.places.info.GraphHopperResponse;
import boev.app.places.info.Hit;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GraphHopperResponseDeserializer implements JsonDeserializer<GraphHopperResponse> {
    @Override
    public GraphHopperResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonArray jsonElements = jsonObject.getAsJsonArray("hits");
        List<Hit> list = new ArrayList<>();

        for(JsonElement elem : jsonElements){
            list.add(jsonDeserializationContext.deserialize(elem, Hit.class));
        }
        return new GraphHopperResponse(list);
    }
}
