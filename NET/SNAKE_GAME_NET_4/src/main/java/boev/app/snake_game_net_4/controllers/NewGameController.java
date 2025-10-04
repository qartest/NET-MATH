package boev.app.snake_game_net_4.controllers;

import boev.app.snake_game_net_4.controllers.utils.CheckInput;
import boev.app.snake_game_net_4.controllers.utils.MessageDialogHelper;
import boev.app.snake_game_net_4.events.StartNewGameEvent;
import boev.app.snake_game_net_4.events.switchs.SwitchToGameEvent;
import boev.app.snake_game_net_4.exception.InputException;
import boev.app.snake_game_net_4.events.switchs.SwitchToStartPage;
import boev.app.snake_game_net_4.model.GameConfig;
import boev.app.snake_game_net_4.model.GameState;
import boev.app.snake_game_net_4.model.gameplayers.PlayerInfo;
import com.google.common.eventbus.EventBus;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NewGameController {
    private EventBus eventBus;
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField foodStaticField;
    @FXML
    private TextField delayField;
    @FXML
    private TextField gameNameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private void back(){
        eventBus.post(new SwitchToStartPage());
    }

    @FXML
    private void startGame(){
        try{
            String widthStr = widthField.getText();
            CheckInput.checkWidth(widthStr);
            int width = Integer.parseInt(widthStr);

            String heightStr = heightField.getText();
            CheckInput.checkHeight(heightStr);
            int height = Integer.parseInt(heightStr);

            String foodStaticStr = foodStaticField.getText();
            CheckInput.checkFoodStatic(foodStaticStr);
            int foodStatic = Integer.parseInt(foodStaticStr);

            String delayStr = delayField.getText();
            CheckInput.checkDelay(delayStr);
            int delay = Integer.parseInt(delayStr);

            String nickname = nicknameField.getText();
            CheckInput.checkNickname(nickname);

            String gameName = gameNameField.getText();
            CheckInput.checkGameName(gameName);

            GameConfig gameConfig = new GameConfig(width, height, foodStatic, delay);
            GameState gameState = new GameState(gameConfig);

            eventBus.post(new StartNewGameEvent(gameState, new PlayerInfo(nickname), gameName));
            eventBus.post(new SwitchToGameEvent());
        } catch (InputException e){
            MessageDialogHelper.inform(e.getMessage());
        }

    }

    void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
