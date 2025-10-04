package boev.app.snake_game_net_4.controllers;

import boev.app.snake_game_net_4.Main;
import boev.app.snake_game_net_4.events.*;
import boev.app.snake_game_net_4.events.switchs.*;
import boev.app.snake_game_net_4.model.MainModel;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;


public class MainController {
    private final static String START_PAGE_FXML = "start_page.fxml";
    private final static String NEW_GAME_SETTINGS_FXML = "new_game_config.fxml";
    private final static String GAME_FIELD_FXML = "game_field.fxml";
    private final static String AVAILABLE_GAMES_LIST_FXML = "available_games_list.fxml";

    private Scene startPageScene;
    private Scene newGameSettingsScene;
    private Scene gameFieldScene;
    private Scene availableGamesListScene;

    private Stage stage;
    private final EventBus eventBus = new EventBus();

    private GameController gameController;
    private AvailableGamesListController availableGamesListController;

    public MainController(){

    }

    public void start() throws IOException{
        loadFXML();
        eventBus.register(this);
        eventBus.post(new SwitchToStartPage());
        stage.show();

    }

    private void loadFXML() throws IOException{
        FXMLLoader loaderStart = new FXMLLoader(Main.class.getResource(START_PAGE_FXML));
        Parent root = loaderStart.load();
        startPageScene = new Scene(root);
        updateControllers(loaderStart);

        FXMLLoader loaderCreate = new FXMLLoader(Main.class.getResource(NEW_GAME_SETTINGS_FXML));
        Parent root1 = loaderCreate.load();
        newGameSettingsScene = new Scene(root1);
        updateControllers(loaderCreate);

        FXMLLoader loadGame = new FXMLLoader(Main.class.getResource(GAME_FIELD_FXML));
        Parent root2 = loadGame.load();
        gameFieldScene = new Scene(root2);
        updateControllers(loadGame);

        FXMLLoader loadListGames = new FXMLLoader(Main.class.getResource(AVAILABLE_GAMES_LIST_FXML));
        Parent root3 = loadListGames.load();
        availableGamesListScene = new Scene(root3);
        updateControllers(loadListGames);

    }

    private void updateControllers(FXMLLoader loader){
        var controller = loader.getController();
        if (controller instanceof StartController c) {
            c.setEventBus(eventBus);
        }
        else if (controller instanceof NewGameController c) {
            c.setEventBus(eventBus);
        }
        else if(controller instanceof GameController c){
            c.setEventBus(eventBus);
            this.gameController = c;
        }
        else if (controller instanceof AvailableGamesListController c) {
            this.availableGamesListController = c;
            this.availableGamesListController.setEventBus(eventBus);
        }
    }
    @Subscribe
    public void switchToStartPage(SwitchToStartPage e) {
        Platform.runLater(() -> stage.setScene(startPageScene));
    }

    @Subscribe
    public void switchToCreateNewGame(SwitchToNewGame e){
        Platform.runLater(() -> stage.setScene(newGameSettingsScene));
    }

    @Subscribe
    public void startGame(StartNewGameEvent e) {
        gameController.createNewGame(e.gameState(), e.playerInfo(), e.gameName());
    }

    @Subscribe
    public void renderField(RenderGameFieldEvent e) {
        Platform.runLater(() -> gameController.renderField(e.gameState()));
    }

    @Subscribe
    public void startNewGameAnimation(StartNewGameAnimationEvent e){
        Platform.runLater(() -> gameController.startAnimation(e.delay()));
    }

    @Subscribe
    public void switchToNewGameConfig(SwitchToNewGame e) {
        Platform.runLater(() -> stage.setScene(newGameSettingsScene));
    }

    @Subscribe
    public void switchToGameField(SwitchToGameEvent e) {
        Platform.runLater(() -> stage.setScene(gameFieldScene));
    }

    @Subscribe
    public void switchToAvailableGamesList(SwitchToAvailableGamesListEvent e) {
        try {
            availableGamesListController.setModel(MainModel.create());
            Platform.runLater(() -> stage.setScene(availableGamesListScene));
        } catch (IOException ignored) {

        }
    }

    @Subscribe
    public void updateScores(UpdateGameScoresEvent e) {
        Platform.runLater(() -> gameController.reloadGameScores());
    }

    @Subscribe
    public void switchToStartPage(SwitchToStartPageEvent e) {
        Platform.runLater(() -> stage.setScene(startPageScene));
    }

    @Subscribe
    public void exit(ExitAppEvent e) {
        stage.close();
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void updateAvailableList(UpdateAvailableGamesEvent e) {
        Platform.runLater(() -> availableGamesListController.updateListOfGames(e.availableGames()));
    }
    @Subscribe
    public void joinGame(JoinGameEvent e) {
        gameController.joinGame(e.model(), e.gameAnnouncement(), e.nickname(), e.isViewer());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
