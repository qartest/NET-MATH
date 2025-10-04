package boev.app.snake_game_net_4.model.conventers;

import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.model.GameAnnouncement;
import boev.app.snake_game_net_4.model.communication.udp.MySocket;

public class GameAnnouncementConverter {
    private static final GameAnnouncementConverter INSTANCE = new GameAnnouncementConverter();

    private GameAnnouncementConverter() {
    }

    public static GameAnnouncementConverter getInstance() {
        return INSTANCE;
    }

    public GameAnnouncement snakesProtoToGameAnnouncement(SnakesProto.GameAnnouncement gameAnnouncement,
                                                          MySocket senderSocket) {
        return new GameAnnouncement(senderSocket, gameAnnouncement.getGameName(),
                gameAnnouncement.getPlayers().getPlayersCount(),
                GameConfigConverter.getInstance().snakesProtoToGameConfig(gameAnnouncement.getConfig()),
                gameAnnouncement.getCanJoin());
    }
}
