module boev.app.places {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires static lombok;
    requires com.google.gson;


    opens boev.app.places to javafx.fxml;
    exports boev.app.places;
    exports boev.app.places.info;
    opens boev.app.places.info to javafx.fxml;
    exports boev.app.places.wrappers;
    opens boev.app.places.wrappers to javafx.fxml;
}