package boev.app.snake_game_net_4.model.utils;

import java.util.Random;

public class GeneratorID {
    private static int id = Math.abs(new Random().nextInt());
    public static int generateID(){
        return ++id;
    }
}
