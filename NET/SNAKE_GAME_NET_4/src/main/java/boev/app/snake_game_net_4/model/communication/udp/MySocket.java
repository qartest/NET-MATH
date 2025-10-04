package boev.app.snake_game_net_4.model.communication.udp;

import java.net.InetAddress;

public record MySocket(InetAddress inetAddress, int port) {
}
