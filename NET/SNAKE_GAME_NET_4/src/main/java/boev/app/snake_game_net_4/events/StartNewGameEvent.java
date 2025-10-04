package boev.app.snake_game_net_4.events;

import boev.app.snake_game_net_4.model.GameState;
import boev.app.snake_game_net_4.model.gameplayers.PlayerInfo;

public record StartNewGameEvent(GameState gameState, PlayerInfo playerInfo, String gameName) {
}
