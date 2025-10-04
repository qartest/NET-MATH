package boev.app.places.info;


import lombok.Data;

import java.util.List;

@Data
public class InterestingPlace {
    private int id;
    private Properties properties;
    private List<Double> coordinates;
    private String description;
}
