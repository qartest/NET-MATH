package boev.app.snake_game_net_4.model.snake;

public record Collision(int killerId, int notLiveId) {
    public boolean isSuicide(){
        return killerId == notLiveId;
    }
}
