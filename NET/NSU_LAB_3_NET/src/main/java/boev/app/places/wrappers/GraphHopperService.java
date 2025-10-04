package boev.app.places.wrappers;

import boev.app.places.info.GraphHopperResponse;
import boev.app.places.info.Hit;
import boev.app.places.info.Point;
import boev.app.places.serializers.GraphHopperResponseDeserializer;
import boev.app.places.serializers.HitDeserializer;
import boev.app.places.serializers.PointDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor

public class GraphHopperService {
    private static final String GEOCODE_API_URL = "https://graphhopper.com/api/1/geocode";
    private static final String GRAPH_HOPPER_API_KEY = "3d801506-4c78-4380-b467-870cdf863886";

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Hit.class, new HitDeserializer())
            .registerTypeAdapter(Point.class, new PointDeserializer()).registerTypeAdapter(GraphHopperResponse.class, new GraphHopperResponseDeserializer()).create();

    public CompletableFuture<GraphHopperResponse> fetchCoordinates(String locationName) {
        // В реальной ситуации здесь будет запрос к API
;          String url = GEOCODE_API_URL + "?q=" + locationName + "&key=" + GRAPH_HOPPER_API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        System.out.println(response.body());
                        throw new RuntimeException("Ошибка при запросе координат: " + response.body());
                    }
//                    System.out.println(response.body());

                    return getResponse(response);
                });

    }
    private GraphHopperResponse getResponse(HttpResponse<String> input){
//        System.out.println(input.body());
        GraphHopperResponse graphHopperResponse = GSON.fromJson(input.body(), GraphHopperResponse.class);
//        System.out.println(graphHopperResponse.getHits());

        return graphHopperResponse;
    }

}