package boev.app.places.place;

import boev.app.places.info.AllWeather;
import boev.app.places.info.Hit;
import boev.app.places.info.InterestingPlace;
import boev.app.places.info.Point;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Place {
    private Point point;
    private String name;
    private String country;
    private AllWeather allWeather;
    private AllInterestingPlace allInterestingPlace;
    private String finallyDescription;

    public Place(Hit hit){
        point = hit.getPoint();
        name = hit.getName();
        country = hit.getCountry();
        allWeather = null;
        allInterestingPlace = null;
    }
    public void createFinallyDescription(){
        String interestingPlaces = allInterestingPlace.getPlaces().stream().map(place1 -> getInformation(place1)).collect(Collectors.joining("\n"));
        finallyDescription =  String.format(
                "Название: %s\nСтрана: %s\nКоординаты: %.5f, %.5f\nПогода:%s\nТемпература: %.5f\n °C\n",
                name,
                country,
                point.getLat(),
                point.getLng(),
                allWeather.getWeather().getMain(),
                allWeather.getWeatherMain().getTemp() - 273.15
        ) + interestingPlaces;
    }

    private String getInformation(InterestingPlace place){
        return String.format("%s\n%s\n", place.getProperties().getName(), place.getDescription());
    }
}
