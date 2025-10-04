module boev.app.snake_game_net_4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.protobuf;
    requires com.google.common;
    requires com.google.errorprone.annotations;
    requires org.checkerframework.checker.qual;

    exports boev.app.snake_game_net_4;
    exports boev.app.snake_game_net_4.controllers to javafx.fxml, com.google.common;
    opens boev.app.snake_game_net_4.controllers to javafx.fxml, com.google.common;

    exports boev.app.snake_game_net_4.events.switchs to com.google.common;
    opens boev.app.snake_game_net_4.events.switchs to com.google.common;

    exports boev.app.snake_game_net_4.model to  com.google.common, com.google.protobuf;
    opens boev.app.snake_game_net_4.model to com.google.common, com.google.protobuf;


}