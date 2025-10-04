package boev.app.snake_game_net_4.model.gameplayers;


import boev.app.snake_game_net_4.model.communication.udp.MySocket;


import java.net.InetAddress;
import java.net.Socket;
import java.time.Instant;

public class InetInfo {
    private final MySocket socket;
    private long lastCommunicationTime;

    public InetInfo(InetAddress inetAddress, int port, long lastCommunicationTime) {
        this.socket = new MySocket(inetAddress, port);
        this.lastCommunicationTime = lastCommunicationTime;
    }

    public InetInfo(MySocket socket, long lastCommunicationTime){
        this.socket = socket;
        this.lastCommunicationTime = lastCommunicationTime;
    }
    public void updateLastCommunication(){
        lastCommunicationTime = Instant.now().toEpochMilli();
    }

    public MySocket getSocket() {
        return socket;
    }

    public long getLastCommunicationTime() {
        return lastCommunicationTime;
    }

    public void setLastCommunicationTime(long lastCommunicationTime) {
        this.lastCommunicationTime = lastCommunicationTime;
    }
}
