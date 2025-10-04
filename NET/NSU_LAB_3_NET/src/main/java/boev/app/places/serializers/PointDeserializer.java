package boev.app.places.serializers;

import com.google.gson.*;
import boev.app.places.info.Point;

import java.lang.reflect.Type;

public class PointDeserializer implements JsonDeserializer<Point>{
    @Override
    public Point deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Point point = new Point();

        point.setLat(jsonObject.get("lat").getAsDouble());
        point.setLng(jsonObject.get("lng").getAsDouble());
        return point;
    }
}
