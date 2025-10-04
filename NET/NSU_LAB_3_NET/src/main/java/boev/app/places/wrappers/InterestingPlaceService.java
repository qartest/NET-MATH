package boev.app.places.wrappers;

import boev.app.places.info.InterestingPlace;
import boev.app.places.info.Properties;
import boev.app.places.place.AllInterestingPlace;
import boev.app.places.place.Place;
import boev.app.places.serializers.AllInterestingPlaceDeserialize;
import boev.app.places.serializers.InterestingPlaceDeserialize;
import boev.app.places.serializers.PropertiesDeserialize;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InterestingPlaceService {
    private static final String PLACE_API_URL = "https://api.opentripmap.com/0.1/ru/places";
    private static final String PLACE_API_KEY = "5ae2e3f221c38a28845f05b6525262d2e9501766df7dc543333148a9";
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Properties.class, new PropertiesDeserialize()).registerTypeAdapter(InterestingPlace.class, new InterestingPlaceDeserialize())
            .registerTypeAdapter(AllInterestingPlace.class, new AllInterestingPlaceDeserialize()).create();
    private static final int Radius = 10000;
    private AtomicInteger live;
    private AtomicInteger die;


    public CompletableFuture<Place> fetchInterestingPlaces(Place place){
        String url;
        synchronized (place){
            url = PLACE_API_URL + "/radius?" + "apikey=" + PLACE_API_KEY + "&radius=" + Radius + "&lon=" + place.getPoint().getLng() + "&lat=" + place.getPoint().getLat();
        }
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        HttpClient httpClient = HttpClient.newHttpClient();

        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
            if(response.statusCode() != 200){
                System.out.println(response.body());
                throw new RuntimeException("Ошибка при запросе интересных мест: " + response.body());
            }
//            System.out.println(response.body());
            AllInterestingPlace allInterestingPlace = GSON.fromJson(response.body(), AllInterestingPlace.class);
//            System.out.println(allInterestingPlace);

            place.setAllInterestingPlace(allInterestingPlace);

            return place;
        });
    }

    public CompletableFuture<Void> fetchDescription(Place place){
        if(place.getAllInterestingPlace() != null){
            List<CompletableFuture<Void>> futures = place.getAllInterestingPlace().getPlaces().stream()
                    .map(obj -> downloadDescription(obj)).collect(Collectors.toList());

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        }
        throw new RuntimeException("Не загрузились интересные места");

    }

    private CompletableFuture<Void> downloadDescription(InterestingPlace place) {
        if (place.getDescription() == null) {
            String url = PLACE_API_URL + "/xid/" + place.getProperties().getXid() + "?apikey=" + PLACE_API_KEY;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
                if (response.statusCode() != 200) {
//                    System.out.println("Я УМЕР");
                    System.out.println(response.body());
                    place.setDescription("ОН ПАЛ СМЕРТЬЮ ХРАБРЫХ");
                } else{
//                    System.out.println("Я жив!!!!!!!");
                    setLastString(response.body(), place);
                }

                return null;
            });
        }
        return null;
    }

    private void setLastString(String body, InterestingPlace place){
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        place.setDescription(setStringValue("otm", jsonObject));
    }

    private String setStringValue(String value, JsonObject jsonObject){
        String answer;
        if(jsonObject.has(value)){
            answer = jsonObject.get(value).getAsString();
        }else{
            answer = "БЛЯ Я ХЗ ЧТО СЮДА ВСТАВЛЯТЬ, Я НЕ ПОНИМАЮ, Я ЗАГРУЗИЛ, НО ЧТО ВСТАВЛЯТЬ ХЗ";
        }
        return answer;
    }
}
