package boev.app.snake_game_net_4.controllers;

import boev.app.snake_game_net_4.controllers.utils.MessageDialogHelper;
import boev.app.snake_game_net_4.events.switchs.SwitchToStartPageEvent;
import boev.app.snake_game_net_4.exception.GameException;
import boev.app.snake_game_net_4.model.GameAnnouncement;
import boev.app.snake_game_net_4.model.GameConfig;
import boev.app.snake_game_net_4.model.GameState;
import boev.app.snake_game_net_4.model.MainModel;
import boev.app.snake_game_net_4.model.cell.*;
import boev.app.snake_game_net_4.model.gameplayers.PlayerInfo;
import boev.app.snake_game_net_4.model.snake.Course;
import com.google.common.eventbus.EventBus;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class GameController {
    private final static Color EVEN_CELL_COLOR = Color.BLACK;
    private final static Color ODD_CELL_COLOR = Color.BLACK.brighter();
    private final static Color FOOD_COLOR = Color.RED;
    @FXML
    public Label gameNameLabel;
    @FXML
    private Canvas gameField;
    @FXML
    private ListView<PlayerInfo> gameScoresList;

    private final HashMap<Integer, Color> snakesColors = new HashMap<>();
    private EventBus eventBus;
    private MainModel model;
    private Timeline animation;

    void createNewGame(GameState gameState, PlayerInfo playerInfo, String gameName) {
        try {
            gameNameLabel.setText(gameName);
            model = MainModel.create();
            model.setControllersEventBus(eventBus);
            model.createNewGame(gameState, playerInfo, gameName);
            renderField(gameState);
            gameScoresList.setCellFactory(list -> new ColorRectCell());
            startAnimation(gameState.getGameConfig().delay());
        } catch (IOException e) {
            MessageDialogHelper.error(new GameException("Error while starting game").getMessage());
        }
    }

        void joinGame(MainModel model, GameAnnouncement gameAnnouncement, String nickname, boolean isViewer) {
            try {
                gameNameLabel.setText(gameAnnouncement.gameName());
                this.model = model;
                gameScoresList.setCellFactory(list -> new ColorRectCell());
                model.joinGame(gameAnnouncement, nickname, isViewer);
            } catch (IOException | InterruptedException e) {
                MessageDialogHelper.error(new GameException("Error while starting game").getMessage());
            }
        }

    void renderField(GameState gameState) {
        GameConfig config = gameState.getGameConfig();
        int width = config.width();
        int height = config.height();

        double fieldWidth = gameField.getWidth();
        double fieldHeight = gameField.getHeight();

        double cellSize = Math.min(fieldWidth / width, fieldHeight / height);

        Cell[][] field = new CellManager().createField(gameState);

        drawField(field, width, height, cellSize);
    }

    void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    void reloadGameScores() {
        ObservableList<PlayerInfo> playerInfos = FXCollections.observableList(model.getCurrentPlayers());

        gameScoresList.setItems(playerInfos);
    }

    void stopAnimation() {
        if (animation != null) {
            animation.stop();
        }
    }

    void startAnimation(int delay) {
        stopAnimation();
        animation = new Timeline(new KeyFrame(Duration.millis(delay), ae -> {
            model.changeGameState();
            reloadGameScores();
            renderField(model.getGameState());
        }));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }

    private void drawField(Cell[][] field, int width, int height, double cellSize) {
        GraphicsContext gc = gameField.getGraphicsContext2D();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = field[i][j];
                if (cell instanceof EmptyCell) {
                    gc.setFill((i + j) % 2 == 0 ? EVEN_CELL_COLOR : ODD_CELL_COLOR);
                } else if (cell instanceof FoodCell) {
                    gc.setFill(FOOD_COLOR);
                } else if (cell instanceof SnakeCell snakeCell) {
                    colorSnake(snakeCell, gc);
                }
                gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
    }

    private void colorSnake(SnakeCell snakeCell, GraphicsContext gc) {
        Color random = generateRandomColor();
        Color current = snakesColors.putIfAbsent(snakeCell.getID(), random);
        if (current == null) {
            gc.setFill(random);
        } else {
            gc.setFill(current);
        }
    }

    @FXML
    private void endGame() {
        if (animation != null) {
            animation.stop();
        }
        model.end();
        model = null;
        eventBus.post(new SwitchToStartPageEvent());
    }

    @FXML
    private void steerSnake(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case W -> model.steerSnake(Course.UP);
            case A -> model.steerSnake(Course.LEFT);
            case S -> model.steerSnake(Course.DOWN);
            case D -> model.steerSnake(Course.RIGHT);
        }
    }

    private class ColorRectCell extends ListCell<PlayerInfo> {
        @Override
        public void updateItem(PlayerInfo item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                Rectangle rect = new Rectangle(10, 10);
                rect.setFill(snakesColors.get(item.getId()));
                setGraphic(rect);
                setText(item.toString());
                setBackground(new Background(new BackgroundFill(Color.WHITE.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));
                if (item.getId() == model.getMe().getId()) {
                    setBackground(new Background(new BackgroundFill(Color.GRAY.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            } else {
                setGraphic(null);
                setText(null);
                setBackground(new Background(new BackgroundFill(Color.WHITE.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }
    private Color generateRandomColor() {
        Random random = new Random();

        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        return Color.rgb(red, green, blue);
    }
}


