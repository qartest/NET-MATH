package boev.app.snake_game_net_4.events.model;

import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.model.communication.udp.MySocket;

public record HandleJoinMsgEvent(SnakesProto.GameMessage.JoinMsg joinMsg, MySocket senderSocket, long msgSeq) {
}