package boev.app.snake_game_net_4.events.model;

import boev.app.snake_game_net_4.model.communication.udp.MySocket;
import boev.app.snake_game_net_4.model.snake.Course;

public record HandleSteerMsgEvent(Course newCourse, MySocket senderSocket, long msgSeq) {
}
