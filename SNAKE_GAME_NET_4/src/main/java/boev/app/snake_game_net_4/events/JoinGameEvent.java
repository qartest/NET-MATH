package boev.app.snake_game_net_4.events;

import boev.app.snake_game_net_4.model.GameAnnouncement;
import boev.app.snake_game_net_4.model.MainModel;

public record JoinGameEvent(MainModel model, GameAnnouncement gameAnnouncement, String nickname, boolean isViewer) {
}
