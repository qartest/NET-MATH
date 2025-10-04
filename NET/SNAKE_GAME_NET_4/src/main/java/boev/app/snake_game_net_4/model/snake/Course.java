package boev.app.snake_game_net_4.model.snake;

import boev.app.snake_game_net_4.model.cell.Coordinate;

public enum Course {
    UP(0),
    DOWN(1),
    LEFT(2),
    RIGHT(3);
    private final int courseIdentifier;
    private static final int[] X = {0, 0, -1, 1};
    private static final int[] Y = {-1, 1, 0, 0};

    Course(int i){
        courseIdentifier = i;
    }

    public Coordinate course2Coordinate(){
        return new Coordinate(X[courseIdentifier], Y[courseIdentifier]);
    }

    public Course getOpposite(){
        return switch (this) {
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case DOWN -> UP;
            case RIGHT -> LEFT;
        };
    }

    public static Course coordinate2Course(Coordinate coordinate){
        if(coordinate.x() == 0 && coordinate.y() == -1) return UP;
        if (coordinate.x() == 1 && coordinate.y() ==  0) return RIGHT;
        if (coordinate.x() == 0 && coordinate.y() ==  1) return DOWN;
        if (coordinate.x() == -1 && coordinate.y() == 0) return LEFT;
        return null;
    }

    public Coordinate nextCoordinate(Coordinate oldCoordinate, int fieldWidth, int fieldHeight) {
        return new Coordinate((oldCoordinate.x() + X[courseIdentifier] + fieldWidth) % fieldWidth,
                (oldCoordinate.y() + Y[courseIdentifier] + fieldHeight) % fieldHeight);
    }
}
