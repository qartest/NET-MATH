package boev.app.places.serializers;

import boev.app.places.info.Weather;
import boev.app.places.info.WeatherMain;
import com.google.gson.*;

import java.lang.reflect.Type;

public class WeatherMainDeserializer implements JsonDeserializer<WeatherMain> {
    @Override
    public WeatherMain deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        WeatherMain weather = new WeatherMain();
        weather.setTemp(getDoubleValue("temp", jsonObject));
        weather.setFeels_like(getDoubleValue("feels_like", jsonObject));
        weather.setTemp_min(getDoubleValue("temp_min", jsonObject));
        weather.setTemp_max(getDoubleValue("temp_min", jsonObject));

        return weather;
    }

    private double getDoubleValue(String value, JsonObject jsonObject){
        double answer;
        if(jsonObject.has(value)){
            answer = jsonObject.get(value).getAsDouble();
        }
        else{
            answer = Double.NaN;
        }
        return answer;
    }


}
