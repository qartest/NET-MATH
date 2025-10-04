package boev.app.places.wrappers;

import boev.app.places.info.InterestingPlace;
import boev.app.places.place.Place;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PlaceDetailsUpdater implements Consumer<Place> {
    private final TextArea textArea;
    private final WeatherService weatherService = new WeatherService();
    private final InterestingPlaceService interestingPlace = new InterestingPlaceService();
    @Override
    public void accept(Place place) {
        if(place != null){
            Platform.runLater(() -> textArea.setText("Загружаем информацию"));
            updatePlaceDetails(place);
        }
    }

    private void updatePlaceDetails(Place place){
        if(place.getAllWeather() == null){

            CompletableFuture<Void> future = CompletableFuture.allOf(weatherService.fetchWeather(place), interestingPlace.fetchInterestingPlaces(place).thenCompose(place1 -> interestingPlace.fetchDescription(place1))).exceptionally(ex -> {

                Platform.runLater(() -> textArea.setText("Ошибка загрузки погоды: " + ex.getMessage()));
                return null;
            });
           future.thenAccept(rezult -> {
               place.createFinallyDescription();
               Platform.runLater(() -> textArea.setText(place.getFinallyDescription()));
           });
        }
        else{
            Platform.runLater(() -> textArea.setText(place.getFinallyDescription()));
        }
    }




}
