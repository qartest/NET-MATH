package boev.app.snake_game_net_4.controllers;

import boev.app.snake_game_net_4.events.ExitAppEvent;
import boev.app.snake_game_net_4.events.switchs.SwitchToAvailableGamesListEvent;
import boev.app.snake_game_net_4.events.switchs.SwitchToNewGame;
import com.google.common.eventbus.EventBus;
import javafx.fxml.FXML;

public class StartController {

    private EventBus eventBus;

    @FXML
    private void createGame(){
        eventBus.post(new SwitchToNewGame());
    }
    @FXML
    private void joinGame(){
        eventBus.post(new SwitchToAvailableGamesListEvent());
    }
    @FXML
    private void exit(){
        eventBus.post(new ExitAppEvent());
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
