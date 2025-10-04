package boev.app.places.serializers;

import boev.app.places.info.Hit;
import boev.app.places.info.Point;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HitDeserializer implements JsonDeserializer<Hit> {

    @Override
    public Hit deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Hit hit = new Hit();

        hit.setPoint((Point) jsonDeserializationContext.deserialize(jsonObject.get("point"), Point.class));

        List<Double> add = new ArrayList<>();
        if(jsonObject.has("extent")){
            JsonArray extent = jsonObject.getAsJsonArray("extent");
            for(JsonElement elem : extent){
                add.add(elem.getAsDouble());
            }
        }

        hit.setExtent(add);

        hit.setName(setStringValue("name", jsonObject));
        hit.setCountry(setStringValue("country", jsonObject));
        hit.setCountrycode(setStringValue("countrycode", jsonObject));
        hit.setState(setStringValue("countrycode", jsonObject));
        hit.setOsm_id(jsonObject.get("osm_id").getAsLong());
        hit.setOsm_type(setStringValue("osm_type", jsonObject));
        hit.setOsm_key(setStringValue("osm_key", jsonObject));
        hit.setOsm_value(setStringValue("osm_value", jsonObject));
        return hit;
    }

    private String setStringValue(String value, JsonObject jsonObject){
        String answer;
        if(jsonObject.has(value)){
            answer = jsonObject.get(value).getAsString();
        }else{
            answer = null;
        }
        return answer;
    }


}
