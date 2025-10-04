package boev.app.snake_game_net_4.model;

import boev.app.snake_game_net_4.model.communication.udp.MySocket;

public record GameAnnouncement(MySocket senderSocket, String gameName, int countOfPlayers, GameConfig gameConfig,
                               boolean canJoin) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameAnnouncement that = (GameAnnouncement) o;
        return gameName.equals(that.gameName);
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nPlayers: %d, Width: %d, Height: %d, Food: %d, Delay: %d",
                gameName,
                countOfPlayers,
                gameConfig().width(),
                gameConfig().height(),
                gameConfig().food(),
                gameConfig.delay());
    }
}
