package boev.app.snake_game_net_4.model.conventers;

import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.model.snake.SnakeState;

public class SnakesStateConverter {
    private final static SnakesStateConverter INSTANCE = new SnakesStateConverter();

    private SnakesStateConverter() {
    }

    public SnakesStateConverter getInstance() {
        return INSTANCE;
    }

    public static SnakeState snakesProtoToSnakesState(SnakesProto.GameState.Snake.SnakeState snakeState) {
        return switch (snakeState) {
            case ALIVE -> SnakeState.ALIVE;
            case ZOMBIE -> SnakeState.ZOMBIE;
        };
    }

    public static SnakesProto.GameState.Snake.SnakeState snakesStateToSnakesProto(SnakeState modelState) {
        return switch (modelState) {
            case ALIVE -> SnakesProto.GameState.Snake.SnakeState.ALIVE;
            case ZOMBIE -> SnakesProto.GameState.Snake.SnakeState.ZOMBIE;
        };
    }
}
