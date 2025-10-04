package boev.app.snake_game_net_4.model.communication;

import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.model.communication.udp.MySocket;

import java.net.Socket;
import java.security.MessageDigestSpi;

public class Message {
    private final SnakesProto.GameMessage message;
    private final MySocket socket;
    private long sentAt;

    public Message(SnakesProto.GameMessage message, MySocket socket){
        this.message = message;
        this.socket = socket;
        sentAt = -1;
    }

    public Message(SnakesProto.GameMessage message, MySocket socket, long sentAt){
        this.message = message;
        this.socket = socket;
        this.sentAt = sentAt;
    }

    public SnakesProto.GameMessage getMessage() {
        return message;
    }

    public MySocket getSocket() {
        return socket;
    }

    public long getSentAt() {
        return sentAt;
    }

    public void setSentAt(long sentAt) {
        this.sentAt = sentAt;
    }
}
