package boev.app.places.serializers;

import boev.app.places.info.InterestingPlace;
import boev.app.places.info.Point;
import boev.app.places.info.Properties;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InterestingPlaceDeserialize implements JsonDeserializer<InterestingPlace> {
    @Override
    public InterestingPlace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        InterestingPlace interestingPlace = new InterestingPlace();
        interestingPlace.setProperties(jsonDeserializationContext.deserialize(jsonObject.get("properties").getAsJsonObject(), Properties.class));

        List<Double> add = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonObject("geometry").getAsJsonArray("coordinates");
        for(JsonElement jsonObject1 : jsonArray){
            add.add(jsonObject1.getAsDouble());
        }
        interestingPlace.setCoordinates(add);
        interestingPlace.setId(jsonObject.get("id").getAsInt());
        return interestingPlace;
    }
}
