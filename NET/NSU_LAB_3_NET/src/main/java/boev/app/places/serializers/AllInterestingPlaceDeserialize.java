package boev.app.places.serializers;

import boev.app.places.info.InterestingPlace;
import boev.app.places.place.AllInterestingPlace;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AllInterestingPlaceDeserialize implements JsonDeserializer<AllInterestingPlace> {
    @Override
    public AllInterestingPlace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray jsonArray = jsonElement.getAsJsonObject().getAsJsonArray("features");

        List<InterestingPlace> interestingPlaces = new ArrayList<>();

        for(JsonElement jsonElement1 : jsonArray){
            interestingPlaces.add(jsonDeserializationContext.deserialize(jsonElement1, InterestingPlace.class));
        }
        return new AllInterestingPlace(interestingPlaces);
    }
}
