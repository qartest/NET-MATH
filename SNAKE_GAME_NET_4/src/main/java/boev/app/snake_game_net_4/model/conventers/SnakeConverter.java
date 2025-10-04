package boev.app.snake_game_net_4.model.conventers;

import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.model.cell.Coordinate;
import boev.app.snake_game_net_4.model.snake.Course;
import boev.app.snake_game_net_4.model.snake.Segment;
import boev.app.snake_game_net_4.model.snake.Snake;
import boev.app.snake_game_net_4.model.snake.SnakeState;

import java.util.List;
import java.util.Objects;

public class SnakeConverter {
    private static final SnakeConverter INSTANCE = new SnakeConverter();

    private SnakeConverter() {
    }

    static SnakeConverter getInstance() {
        return INSTANCE;
    }

    public Snake snakesProtoToSnake(SnakesProto.GameState.Snake snake, Coordinate fieldSize) {
        Coordinate head = CoordinateConverter.getInstance().snakesProtoToCoordinate(snake.getPoints(0));
        Course headCourse = CourseConverter.getInstance().snakesProtoToDirection(snake.getHeadDirection());
        Snake modelSnake = new Snake(head, headCourse, snake.getPlayerId(), headCourse.getOpposite().nextCoordinate(head, fieldSize.x(), fieldSize.y()));

        Coordinate last = headCourse.getOpposite().nextCoordinate(head, fieldSize.x(), fieldSize.y());
        int snakeLength = snake.getPointsCount();
        for (int i = 2; i < snakeLength; ++i) {
            Coordinate current = CoordinateConverter.getInstance().snakesProtoToCoordinate(snake.getPoints(i));
            if (current.x() == 0) {
                for (int j = 0; j < Math.abs(current.y()); j++) {
                    Coordinate shift = new Coordinate(0, current.y() > 0 ? 1 : -1);
                    modelSnake.addNewSegment(Objects.requireNonNull(Course.coordinate2Course(shift)), fieldSize.x(), fieldSize.y());
                    last = new Coordinate(last.x(), (last.y() + shift.y() + fieldSize.y()) % fieldSize.y());
                }
            } else if (current.y() == 0) {
                for (int j = 0; j < Math.abs(current.x()); j++) {
                    Coordinate shift = new Coordinate(current.x() > 0 ? 1 : -1, 0);
                    modelSnake.addNewSegment(Objects.requireNonNull(Course.coordinate2Course(shift)), fieldSize.x(), fieldSize.y());
                    last = new Coordinate((last.x() + shift.x() + fieldSize.x()) % fieldSize.x(), last.y());
                }
            } else {
                return null;
            }
            last = current;
        }
        SnakeState state = SnakesStateConverter.snakesProtoToSnakesState(snake.getState());
        if (state == SnakeState.ZOMBIE) {
            modelSnake.setZombie();
        }
        return modelSnake;
    }

    public SnakesProto.GameState.Snake snakeToSnakesProto(Snake modelSnake) {
        SnakesProto.GameState.Snake.Builder snakeBuilder = SnakesProto.GameState.Snake.newBuilder();

        SnakesProto.GameState.Snake.SnakeState state = SnakesStateConverter.snakesStateToSnakesProto(modelSnake.getState());
        SnakesProto.Direction headDirection =
                CourseConverter.getInstance().directionToSnakesProto(modelSnake.getHead().getCourse());

        snakeBuilder.setState(state).setHeadDirection(headDirection)
                .setPlayerId(modelSnake.getPlayerId());

        SnakesProto.GameState.Coord headCoord =
                CoordinateConverter.getInstance().coordinateToSnakeProto(modelSnake.getHead().getCoordinate());
        snakeBuilder.addPoints(headCoord);

        List<Segment> body = modelSnake.getBody();
        for (int i = 1; i < body.size(); ++i) {
            Coordinate shift = body.get(i).getCourse().getOpposite().course2Coordinate();
            snakeBuilder.addPoints(CoordinateConverter.getInstance().coordinateToSnakeProto(shift));
        }
        return snakeBuilder.build();
    }
}
