package boev.app.snake_game_net_4.model.cell;

import boev.app.snake_game_net_4.model.GameState;
import boev.app.snake_game_net_4.model.snake.Segment;
import boev.app.snake_game_net_4.model.snake.Snake;

import java.util.List;

public class CellManager {
    public Cell[][] createField(GameState gameState){
        int width = gameState.getGameConfig().width();
        int height = gameState.getGameConfig().height();
        Cell[][] field = new Cell[width][height];

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                field[i][j] = new EmptyCell();
            }
        }

        List<Snake> snakes = gameState.getSnakes();
        snakes.forEach(snake -> {
            List<Segment> body = snake.getBody();
            body.forEach(snakeSegment -> {
                        int x = snakeSegment.getCoordinate().x();
                        int y = snakeSegment.getCoordinate().y();
                        field[x][y] = new SnakeCell(snake.getPlayerId());
                    }
            );
        });

        List<Coordinate> foods = gameState.getFood();
        foods.forEach(food -> {
            int x = food.x();
            int y = food.y();
            field[x][y] = new FoodCell();
        });

        return field;

    }
}
