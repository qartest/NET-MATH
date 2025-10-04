package boev.app.snake_game_net_4.model.communication.udp;



import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.events.model.*;
import boev.app.snake_game_net_4.model.GameConfig;
import boev.app.snake_game_net_4.model.GameState;
import boev.app.snake_game_net_4.model.communication.Message;
import boev.app.snake_game_net_4.model.conventers.CourseConverter;
import boev.app.snake_game_net_4.model.conventers.GamePlayersConverter;
import boev.app.snake_game_net_4.model.conventers.GameStateConverter;
import boev.app.snake_game_net_4.model.conventers.NodeRoleConverter;
import boev.app.snake_game_net_4.model.gameplayers.GamePlayer;
import boev.app.snake_game_net_4.model.gameplayers.Role;
import boev.app.snake_game_net_4.model.snake.Course;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GameMessageHandler extends Thread{
    private final EventBus modelEventBus;
    private final GameConfig gameConfig;
    public GameMessageHandler(EventBus modelEventBus, GameConfig gameConfig){
        this.gameConfig = gameConfig;
        this.modelEventBus = modelEventBus;
    }

    public void run(){

        try {
            UDPMessageReceiverAndSender messageReceiverAndSender = UDPMessageReceiverAndSender.getInstance();

            while (!this.isInterrupted()) {

                Message message = messageReceiverAndSender.receive();

                SnakesProto.GameMessage gameMessage = message.getMessage();

                MySocket senderSocket =  message.getSocket();

                if (gameMessage.hasSteer() && !this.isInterrupted()) {
                    SnakesProto.GameMessage.SteerMsg steer = gameMessage.getSteer();
                    Course newCourse = CourseConverter.getInstance().snakesProtoToDirection(steer.getDirection());
                    CompletableFuture.runAsync(() -> modelEventBus.post(new HandleSteerMsgEvent(newCourse, senderSocket, gameMessage.getMsgSeq())));
                    continue;
                }

                if (gameMessage.hasState() && !this.isInterrupted()) {
                    SnakesProto.GameState snakesProtoState = gameMessage.getState().getState();
                    GameState newState = GameStateConverter.getInstance().snakesProtoToGameState(snakesProtoState, gameConfig);
                    List<GamePlayer> players = GamePlayersConverter.getInstance().snakesProtoToGamePlayers(snakesProtoState.getPlayers());

                    CompletableFuture.runAsync(() -> modelEventBus.post(new HandleGameStateMsgEvent(newState, players, senderSocket, gameMessage.getMsgSeq())));
                    continue;
                }

                if (gameMessage.hasJoin() && !this.isInterrupted()) {
                    SnakesProto.GameMessage.JoinMsg joinMsg = gameMessage.getJoin();
                    CompletableFuture.runAsync(() ->modelEventBus.post(new HandleJoinMsgEvent(joinMsg, senderSocket, gameMessage.getMsgSeq())));
                    continue;
                }

                if (gameMessage.hasPing() && !this.isInterrupted()) {
                    CompletableFuture.runAsync(() ->modelEventBus.post(new HandlePingMsgEvent(senderSocket, gameMessage.getMsgSeq())));
                    continue;
                }

                if (gameMessage.hasAck() && !this.isInterrupted()) {
                    CompletableFuture.runAsync(() ->modelEventBus.post(new HandleAckMsgEvent(gameMessage.getSenderId(), gameMessage.getMsgSeq())));
                    continue;
                }

                if (gameMessage.hasError() && !this.isInterrupted()) {
                    CompletableFuture.runAsync(() ->modelEventBus.post(new HandleErrorMsgEvent(senderSocket, gameMessage.getMsgSeq())));
                    continue;
                }

                if (gameMessage.hasRoleChange() && !this.isInterrupted()) {
                    SnakesProto.GameMessage.RoleChangeMsg roleChangeMsg = gameMessage.getRoleChange();
                    Role senderRole = NodeRoleConverter.getInstance().snakesProtoToNodeRole(roleChangeMsg.getSenderRole());
                    Role receiverRole = NodeRoleConverter.getInstance().snakesProtoToNodeRole(roleChangeMsg.getReceiverRole());
                    CompletableFuture.runAsync(() ->modelEventBus.post(new HandleRoleChangeMsgEvent(senderRole, receiverRole, gameMessage.getSenderId(), gameMessage.getReceiverId(), senderSocket, gameMessage.getMsgSeq())));
                }
            }
        } catch (IOException ignored){

        }
    }

}
