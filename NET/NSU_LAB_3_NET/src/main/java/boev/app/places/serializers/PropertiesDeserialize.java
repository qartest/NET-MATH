package boev.app.places.serializers;

import boev.app.places.info.Properties;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PropertiesDeserialize implements JsonDeserializer<Properties> {
    @Override
    public Properties deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Properties properties = new Properties();

        properties.setName(setStringValue("name", jsonObject));
        properties.setDist(jsonObject.get("dist").getAsDouble());
        properties.setXid(setStringValue("xid", jsonObject));
        return properties;
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
