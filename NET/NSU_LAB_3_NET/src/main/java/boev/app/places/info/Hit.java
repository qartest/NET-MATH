package boev.app.places.info;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class Hit {
    private Point point;

    private List<Double> extent;

    private String name;

    private String country;

    @SerializedName("countrycode")
    private String countrycode;

    @SerializedName("state")
    private String state;

    @SerializedName("osm_id")
    private long osm_id;

    @SerializedName("osm_type")
    private String osm_type;

    @SerializedName("osm_key")
    private String osm_key;

    @SerializedName("osm_value")
    private String osm_value;

}

