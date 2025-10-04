package boev.app.snake_game_net_4.model.snake;

import boev.app.snake_game_net_4.model.cell.Coordinate;


public class Segment {
    private Course course;
    private Coordinate coordinate;

    public Segment(Segment input){
        coordinate = input.getCoordinate();
        course = input.getCourse();
    }
    void replace(int width, int height){
        coordinate = course.nextCoordinate(coordinate, width, height);
    }

    public Segment(Course course, Coordinate coordinate){
        this.course = course;
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
