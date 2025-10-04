package boev.app.snake_game_net_4.model.gameplayers;


import boev.app.snake_game_net_4.model.communication.udp.MySocket;

import java.net.Socket;


public class GamePlayer {
    private final PlayerInfo playerInfo;
    private final InetInfo inetInfo;

    public GamePlayer(PlayerInfo playerInfo, Role role, InetInfo inetInfo) {
        this.playerInfo = playerInfo;
        playerInfo.setRole(role);
        this.inetInfo = inetInfo;
    }


    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if (o instanceof GamePlayer gamePlayer){
            return gamePlayer.playerInfo.equals(this.playerInfo);
        }else {
            return false;
        }
    }

    public long getLastCommunicationTime() {
        return inetInfo.getLastCommunicationTime();
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public InetInfo getInetInfo() {
        return inetInfo;
    }

    public void updateLastCommunication() {
        inetInfo.updateLastCommunication();
    }

    public MySocket getSocket() {
        return inetInfo.getSocket();
    }

    public int getId() {
        return playerInfo.getId();
    }

    public Role getRole() {
        return playerInfo.getRole();
    }

    public void setRole(Role role){
        playerInfo.setRole(role);
    }
}
