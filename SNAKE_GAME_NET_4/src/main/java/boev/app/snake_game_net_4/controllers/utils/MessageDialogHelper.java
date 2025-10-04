package boev.app.snake_game_net_4.controllers.utils;

import javafx.scene.control.Alert;

public class MessageDialogHelper {
    public static void inform(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Warning");
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void error(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }
}
