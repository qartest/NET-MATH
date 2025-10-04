package boev.app.places.info;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GraphHopperResponse {
    private List<Hit> hits;
}
