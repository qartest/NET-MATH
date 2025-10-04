package boev.app.snake_game_net_4.controllers;

import boev.app.snake_game_net_4.controllers.utils.CheckInput;
import boev.app.snake_game_net_4.controllers.utils.MessageDialogHelper;
import boev.app.snake_game_net_4.events.JoinGameEvent;
import boev.app.snake_game_net_4.events.switchs.SwitchToStartPageEvent;
import boev.app.snake_game_net_4.exception.InputException;
import boev.app.snake_game_net_4.model.GameAnnouncement;
import boev.app.snake_game_net_4.model.MainModel;
import com.google.common.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AvailableGamesListController {

    @FXML
    private ListView<GameAnnouncement> gamesList;
    @FXML
    private TextField nicknameField;
    @FXML
    private CheckBox viewerModeCheckBox;
    private EventBus eventBus;
    private MainModel model;
    private ScheduledExecutorService scheduler;

    void updateListOfGames(List<GameAnnouncement> gameAnnouncementList) {
        ObservableList<GameAnnouncement> items = FXCollections.observableArrayList(gameAnnouncementList);
        gamesList.setItems(items);
    }

    void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    void setModel(MainModel model) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow(); // Завершить предыдущий планировщик
        }
        model.setControllersEventBus(eventBus);
        this.model = model;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(model::removeExpiredGames, 1, 1, TimeUnit.SECONDS);
    }

    @FXML
    private void back() {
        scheduler.shutdownNow();
        eventBus.post(new SwitchToStartPageEvent());
    }

    @FXML
    private void chooseGame() {
        try {
            GameAnnouncement chosenGame = gamesList.getSelectionModel().getSelectedItem();
            if (chosenGame != null) {
                String nickname = nicknameField.getText();
                CheckInput.checkGameName(nickname);
                eventBus.post(new JoinGameEvent(model, chosenGame, nickname, viewerModeCheckBox.isSelected()));
                scheduler.shutdownNow();
            }
        } catch (InputException e) {
            MessageDialogHelper.inform(e.getMessage());
        }
    }

    @FXML
    private void discover() throws IOException {
        gamesList.getItems().clear();
        model.sendDiscoverMsg();
    }
}