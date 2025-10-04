package boev.app.places.place;

import boev.app.places.info.InterestingPlace;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllInterestingPlace {
    private List<InterestingPlace> places;
}
