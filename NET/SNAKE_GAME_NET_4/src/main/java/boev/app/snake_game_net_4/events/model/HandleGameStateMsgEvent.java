package boev.app.snake_game_net_4.events.model;

import boev.app.snake_game_net_4.model.GameState;
import boev.app.snake_game_net_4.model.communication.udp.MySocket;
import boev.app.snake_game_net_4.model.gameplayers.GamePlayer;

import java.util.List;

public record HandleGameStateMsgEvent(GameState newGameState, List<GamePlayer> players, MySocket senderSocket,
                                      long msgSeq) {
}
