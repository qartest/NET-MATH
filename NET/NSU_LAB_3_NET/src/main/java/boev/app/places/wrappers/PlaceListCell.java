package boev.app.places.wrappers;

import boev.app.places.place.Place;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class PlaceListCell extends ListCell<Place> {
    private final Button detailsButton = new Button("Подробнее");
    private final HBox hbox = new HBox(10);
    private final Consumer<Place> onDetailsClicked;

    public PlaceListCell(Consumer<Place> onDetailsClicked){
        this.onDetailsClicked = onDetailsClicked;
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().add(detailsButton);

        detailsButton.setOnAction(event -> {
            Place place = getItem();
            if (place != null) {
                onDetailsClicked.accept(place);
            }
        });
    }
    @Override
    protected void updateItem(Place place, boolean empty){
        super.updateItem(place, empty);
        if (empty || place == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(place.getName());
            setGraphic(hbox);
        }
    }
}
