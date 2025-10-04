package boev.app.snake_game_net_4;

import boev.app.snake_game_net_4.controllers.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        MainController mainController = new MainController();
        mainController.setStage(stage);
        mainController.start();
        SnakesProto.GameMessage.SteerMsg steerMessage = SnakesProto.GameMessage.SteerMsg.newBuilder()
                .setDirection(SnakesProto.Direction.UP)  // Устанавливаем направление
                .build();

        // Создаем GameMessage с обязательным полем msg_seq
        SnakesProto.GameMessage message = SnakesProto.GameMessage.newBuilder()
                .setMsgSeq(1)  // Обязательно устанавливаем msg_seq
                .setSteer(steerMessage)  // Пример использования SteerMsg
                .build();
    }

    public static void main(String[] args) {
        launch();
    }
}
