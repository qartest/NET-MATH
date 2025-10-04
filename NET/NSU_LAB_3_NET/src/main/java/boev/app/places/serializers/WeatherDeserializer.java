package boev.app.places.serializers;

import boev.app.places.info.Weather;
import com.google.gson.*;

import java.lang.reflect.Type;

public class WeatherDeserializer implements JsonDeserializer<Weather> {
    @Override
    public Weather deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        Weather weather = new Weather();

        weather.setMain(setStringValue("main", jsonObject));
        weather.setDescription(setStringValue("description", jsonObject));

        return weather;
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
