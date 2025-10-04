package boev.app.snake_game_net_4.events;

import boev.app.snake_game_net_4.model.GameState;

public record RenderGameFieldEvent(GameState gameState) {
}
