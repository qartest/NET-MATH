package boev.app.snake_game_net_4.model.communication;

import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.events.model.HandleDiscoverMsgEvent;
import boev.app.snake_game_net_4.events.model.HandleGameAnnouncementMsgEvent;
import boev.app.snake_game_net_4.model.communication.udp.MySocket;
import boev.app.snake_game_net_4.model.communication.udp.UDPMulticastMessageReceiver;
import boev.app.snake_game_net_4.model.conventers.GameAnnouncementConverter;
import com.google.common.eventbus.EventBus;

import java.io.IOException;

public class GameAnnouncementMsgHandler  extends Thread{
    private final EventBus modelEventBus;

    public GameAnnouncementMsgHandler(EventBus modelEventBus) {
        this.modelEventBus = modelEventBus;
    }

    @Override
    public void run() {
        try {
            UDPMulticastMessageReceiver multicastMessageReceiver = UDPMulticastMessageReceiver.getInstance();
            while (!this.isInterrupted()) {

                Message message = multicastMessageReceiver.receive();
                MySocket senderSocket = message.getSocket();
                SnakesProto.GameMessage msg = message.getMessage();

                if (msg.hasAnnouncement() && !this.isInterrupted()) {
                    msg.getAnnouncement().getGamesList().forEach(game ->
                            modelEventBus.post(new HandleGameAnnouncementMsgEvent(GameAnnouncementConverter.getInstance().snakesProtoToGameAnnouncement(game, senderSocket))));
                    continue;
                }

                if (msg.hasDiscover() && !this.isInterrupted()) {
                    modelEventBus.post(new HandleDiscoverMsgEvent(senderSocket));
                }

            }
            multicastMessageReceiver.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
