package boev.app.snake_game_net_4.model.snake;

import boev.app.snake_game_net_4.model.cell.Coordinate;


import java.util.ArrayList;
import java.util.List;



public class Snake {
    private final List<Segment> body = new ArrayList<>();
    private SnakeState state = SnakeState.ALIVE;
    private final int playerId;

    public Snake(Coordinate headCoordinate, Course headCourse, int playerId, Coordinate tailCoordinate) {
        this.playerId = playerId;
        body.add(new Segment(headCourse, headCoordinate));
        body.add(new Segment(headCourse, tailCoordinate));
    }

    public boolean isSuicide() {
        Coordinate head = getHead().getCoordinate();
        for (int i = 1; i < body.size(); ++i) {
            if (head.equals(body.get(i).getCoordinate())) {
                return true;
            }
        }
        return false;
    }

    public List<Segment> getBody() {
        return body;
    }

    public SnakeState getState() {
        return state;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isCollision(Coordinate coordinate) {
        return body.stream().anyMatch(segment -> segment.getCoordinate().equals(coordinate));
    }

    public void grow(int fieldWidth, int fieldHeight, Coordinate coordinate) {
        Segment newSegment = new Segment(body.get(0).getCourse(), coordinate);
        body.add(1, newSegment);
    }

    public Coordinate moveHead(int fieldWidth, int fieldHeight){
        body.get(0).replace(fieldWidth, fieldHeight);
        return body.get(0).getCoordinate();
    }
    public void moveBody(int fieldWidth, int fieldHeight) {
        for(int i = 1; i < body.size(); ++i){
            body.get(i).replace(fieldWidth, fieldHeight);
        }
        for (int i = body.size() - 1; i > 0; --i) {
            body.get(i).setCourse(body.get(i - 1).getCourse());
        }
    }
    public void addNewSegment(Course course, int fieldWidth, int fieldHeight) {
        Segment lastSegment = body.get(body.size() - 1);
        Coordinate newCoordinate = course.nextCoordinate(lastSegment.getCoordinate(), fieldWidth, fieldHeight);
        body.add(new Segment(course.getOpposite(), newCoordinate));
    }

    public void turn(Course newCourse) {
        if (state == SnakeState.ZOMBIE) {
            state = SnakeState.ALIVE;
        }
            Segment neck = body.get(1);
            if (neck.getCourse() == newCourse.getOpposite()) {
                return;
            }

        Segment head = body.get(0);
        head.setCourse(newCourse);
    }

    public Segment getHead() {
        return body.get(0);
    }

    public void setZombie() {
        state = SnakeState.ZOMBIE;
    }

}
