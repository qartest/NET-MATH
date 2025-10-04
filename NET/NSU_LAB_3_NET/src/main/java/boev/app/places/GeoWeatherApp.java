package boev.app.places;

import boev.app.places.wrappers.GraphHopperService;
import boev.app.places.wrappers.PlaceDetailsUpdater;
import boev.app.places.wrappers.PlaceListCell;
import boev.app.places.place.Place;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GeoWeatherApp extends Application {

    private static final GraphHopperService graphHopperService = new GraphHopperService();

    @Override
    public void start(Stage stage) {

        TextField inputField = new TextField();
        inputField.setPromptText("Введите название места...");

        Button searchButton = new Button("Искать");
        TextArea resultArea = new TextArea();

        TextArea  listView1 = new TextArea ();
        listView1.setEditable(false);

        ListView<Place> listView = new ListView<>();

        VBox root = new VBox(listView);

        resultArea.setEditable(false);

        VBox layout = new VBox(10, inputField, searchButton, root);
        layout.setStyle("-fx-padding: 20;");

        VBox info = new VBox(listView1);

        HBox begin = new HBox(10, layout, info);
        begin.setStyle("-fx-padding: 20;");

        listView.setCellFactory(lv -> new PlaceListCell(new PlaceDetailsUpdater(listView1)));

        searchButton.setOnAction(event -> {
            String locationName = inputField.getText().trim();
//            listView.getItems().clear();
            if (locationName.isEmpty()) {
                resultArea.setText("Введите название места!");
                return;
            }
            resultArea.setText("Ищем данные для: " + locationName);
            graphHopperService.fetchCoordinates(locationName).thenAccept(result ->
                Platform.runLater(() -> {
                    result.getHits().forEach(hit -> {

                        listView.getItems().add(new Place(hit));
                    });
                })
            ).exceptionally(ex -> {
                resultArea.setText("Произошла ошибка: " + ex.getMessage());
                return null;
            });
        });

        Scene scene = new Scene(begin, 800, 600);
        stage.setScene(scene);
        stage.setTitle("GeoWeatherApp");
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}