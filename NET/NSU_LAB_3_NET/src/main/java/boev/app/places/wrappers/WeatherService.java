package boev.app.places.wrappers;

import boev.app.places.info.*;
import boev.app.places.place.AllInterestingPlace;
import boev.app.places.place.Place;
import boev.app.places.serializers.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class WeatherService {
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String OPENWEATHER_API_KEY = "97de8f284a9ac7dce2e08cb746d992bc";
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Weather.class, new WeatherDeserializer())
            .registerTypeAdapter(WeatherMain.class, new WeatherMainDeserializer()).registerTypeAdapter(AllWeather.class, new AllWeatherDeserialize()).create();


    public CompletableFuture<Void> fetchWeather(Place place) {
        String url;
        synchronized (place){
            url = WEATHER_API_URL + "?lat=" + place.getPoint().getLat() + "&lon=" + place.getPoint().getLng() + "&appid=" + OPENWEATHER_API_KEY;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        System.out.println(response.body());
                        throw new RuntimeException("Ошибка при запросе погоды: " + response.body());
                    }
//                    System.out.println(response.body());
                    AllWeather allWeather = GSON.fromJson(response.body(), AllWeather.class);
                    synchronized (place){
                        place.setAllWeather(allWeather);
                    }

                    return null;
                });
    }

}
