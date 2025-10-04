package boev.app.snake_game_net_4.events;

import boev.app.snake_game_net_4.model.GameAnnouncement;

import java.util.List;

public record UpdateAvailableGamesEvent(List<GameAnnouncement> availableGames) {
}

