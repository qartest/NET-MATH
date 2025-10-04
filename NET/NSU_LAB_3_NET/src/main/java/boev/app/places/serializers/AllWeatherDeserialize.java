package boev.app.places.serializers;

import boev.app.places.info.AllWeather;
import boev.app.places.info.Weather;
import boev.app.places.info.WeatherMain;
import com.google.gson.*;

import java.lang.reflect.Type;

public class AllWeatherDeserialize implements JsonDeserializer<AllWeather> {
    @Override
    public AllWeather deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        AllWeather allWeather = new AllWeather();

        allWeather.setWeather(jsonDeserializationContext.deserialize(jsonObject.get("weather"), Weather.class));
        allWeather.setWeatherMain(jsonDeserializationContext.deserialize(jsonObject.get("main"), WeatherMain.class));

        return allWeather;
    }
}
