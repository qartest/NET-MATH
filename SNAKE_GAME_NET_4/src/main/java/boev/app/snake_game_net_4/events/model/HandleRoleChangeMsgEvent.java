package boev.app.snake_game_net_4.events.model;

import boev.app.snake_game_net_4.model.communication.udp.MySocket;
import boev.app.snake_game_net_4.model.gameplayers.Role;

public record HandleRoleChangeMsgEvent(Role senderRole, Role receiverRole, int senderId, int receiverId, MySocket senderSocket,
                                       long msgSeq) {
}
