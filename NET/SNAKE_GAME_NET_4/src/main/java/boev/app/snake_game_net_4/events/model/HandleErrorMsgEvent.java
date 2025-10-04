package boev.app.snake_game_net_4.events.model;

import boev.app.snake_game_net_4.model.communication.udp.MySocket;

public record HandleErrorMsgEvent(MySocket senderSocket, long msgSeq) {
}
