package boev.app.snake_game_net_4.model.conventers;

import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.model.snake.Course;

public class CourseConverter {
    private final static CourseConverter INSTANCE = new CourseConverter();

    private CourseConverter() {
    }

    public static CourseConverter getInstance() {
        return INSTANCE;
    }

    public Course snakesProtoToDirection(SnakesProto.Direction direction) {
        return switch (direction) {
            case UP -> Course.UP;
            case DOWN -> Course.DOWN;
            case LEFT -> Course.LEFT;
            case RIGHT -> Course.RIGHT;
        };
    }

    public SnakesProto.Direction directionToSnakesProto(Course course) {
        return switch (course) {
            case UP -> SnakesProto.Direction.UP;
            case DOWN -> SnakesProto.Direction.DOWN;
            case LEFT -> SnakesProto.Direction.LEFT;
            case RIGHT -> SnakesProto.Direction.RIGHT;
        };
    }
}
