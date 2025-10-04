package boev.app.snake_game_net_4.events.model;

import boev.app.snake_game_net_4.model.communication.udp.MySocket;

public record HandlePingMsgEvent(MySocket senderSocket, long msgSeq) {
}
