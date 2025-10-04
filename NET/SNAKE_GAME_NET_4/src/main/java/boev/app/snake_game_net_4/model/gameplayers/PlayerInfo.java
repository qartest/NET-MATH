package boev.app.snake_game_net_4.model.gameplayers;

import boev.app.snake_game_net_4.model.utils.GeneratorID;


public class PlayerInfo {
    private final String nickname;
    private final int id;
    private int score = 0;
    private Role role = Role.NORMAL;

    public PlayerInfo(String nickname){
        this.nickname=nickname;
        this.id = GeneratorID.generateID();
    }

    public PlayerInfo(String nickname, int id){
        this.nickname=nickname;
        this.id = id;
    }

    public PlayerInfo(String nickname, int id, int score){
        this.nickname=nickname;
        this.id = id;
        this.score = score;
    }

    public void increaseScore(){
        score++;
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString(){
        String role = switch (this.role){
            case DEPUTY -> "DEPUTY";
            case MASTER -> "MASTER";
            case NORMAL -> "NORMAL";
            case VIEWER -> "VIEWER";
        };
        if (this.role == Role.VIEWER){
            return role + " " + nickname;
        }
        return role + " " + nickname + ": " + score + " points";
    }

    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if (o instanceof PlayerInfo playerInfo){
            return playerInfo.id == this.id;
        }else {
            return false;
        }
    }

}
