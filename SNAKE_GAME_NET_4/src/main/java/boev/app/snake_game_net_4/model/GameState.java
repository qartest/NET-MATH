package boev.app.snake_game_net_4.model;

import boev.app.snake_game_net_4.model.cell.*;
import boev.app.snake_game_net_4.model.gameplayers.PlayerInfo;
import boev.app.snake_game_net_4.model.snake.*;


import java.util.*;

public class GameState {
    private final GameConfig gameConfig;
    private final List<PlayerInfo> playerInfos = new ArrayList<>();
    private final List<Snake> snakes = new ArrayList<>();
    private final List<Coordinate> food = new ArrayList<>();
    private int stateOrder = 0;

    public GameState(GameConfig gameConfig){
        this.gameConfig = gameConfig;
    }

    public boolean addNewPlayer(PlayerInfo playerInfo, Coordinate coordinate){
        Segment tail = chooseRandomTail(coordinate);
        if (tail == null) {
            return false;
        }

        Snake snake = new Snake(coordinate, tail.getCourse().getOpposite(), playerInfo.getId(), tail.getCoordinate());
        synchronized (snakes) {
            snakes.add(snake);
        }
        synchronized (playerInfos) {
            playerInfos.add(playerInfo);
        }
        return true;
    }

    public Coordinate findFreeArea() {
        Cell[][] field = new CellManager().createField(this);
        int width = gameConfig.width();
        int height = gameConfig.height();

        int rand_i = new Random().nextInt(width);
        int rand_j = new Random().nextInt(height);
        for (int i = rand_i; i < width + rand_i; ++i) {
            for (int j = rand_j; j < height + rand_j; ++j) {
                if (isSquareFree(field, i % width, j % height)) {
                    if (field[i % width][j % height] instanceof EmptyCell) {
                        return new Coordinate(i % width, j % height);
                    }
                }
            }
        }
        return null;
    }

    private int amountAliveSnakes() {
        synchronized (snakes) {
            int count = 0;
            for (Snake snake : snakes) {
                if (snake.getState() == SnakeState.ALIVE) {
                    count++;
                }
            }
            return count;
        }
    }

    public void steerSnake(int snakeId, Course newCourse) {
//        System.out.println("Меняю змейку" + snakeId);
        Optional<Snake> optionalSnake = findSnake(snakeId);
//        if(optionalSnake.isPresent()){
//            System.out.println(optionalSnake.get().getState());
//            System.out.println(optionalSnake.get().getPlayerId());
//        }
        optionalSnake.ifPresent(snake -> snake.turn(newCourse));

    }

    private Optional<Snake> findSnake(int snakeId) {
        synchronized (snakes) {
            return snakes.stream().filter(snake -> snake.getPlayerId() == snakeId).findFirst();
        }
    }

    private List<Integer> detectCollisions() {
        List<Collision> collisions = findMurders();
        List<Integer> ids = new ArrayList<>();
        for (Collision collision : collisions) {
            ids.add(collision.notLiveId());
            if (collision.isSuicide()) {
                removeSnake(collision.killerId());
            } else {
                increasePlayerScore(collision.killerId());
                removeSnake(collision.notLiveId());
            }
        }
        return ids;
    }

    private void increasePlayerScore(int id) {
        PlayerInfo playerInfo = findPlayer(id).orElse(null);
        if (playerInfo == null) {
            return;
        }
        playerInfo.increaseScore();
    }

    private Optional<PlayerInfo> findPlayer(int id) {
        synchronized (playerInfos) {
            return playerInfos.stream().filter(player -> player.getId() == id).findFirst();
        }
    }
    private void removeSnake(int id) {
        synchronized (snakes) {
            Random random = new Random();
            Snake snake = snakes.stream().filter(s -> s.getPlayerId() == id).findFirst().orElse(null);
            if (snake != null) {
                List<Segment> body = snake.getBody();
                int bodySize = body.size();
                for (int i = 1; i < bodySize; ++i) {
                    if (random.nextBoolean()) {
                        food.add(body.get(i).getCoordinate());
                    }
                }
                snakes.remove(snake);
            }
        }
    }


    private List<Collision> findMurders() {
        synchronized (snakes) {
            List<Collision> collisions = new ArrayList<>();
            snakes.forEach(snake -> {
                for (Snake otherSnake : snakes) {
                    if (snake.isSuicide()) {
                        collisions.add(new Collision(snake.getPlayerId(), snake.getPlayerId()));
                        break;
                    }
                    if (otherSnake.getPlayerId() != snake.getPlayerId()
                            && otherSnake.isCollision(snake.getHead().getCoordinate())) {
                        collisions.add(new Collision(otherSnake.getPlayerId(), snake.getPlayerId()));
                    }
                }
            });
            return collisions;
        }
    }

    private void moveSnakes(int fieldWidth, int fieldHeight) {
        synchronized (snakes) {
            Set<Coordinate> ateFood = new HashSet<>();
            for (Snake snake : snakes) {
                Coordinate lastHead = snake.getHead().getCoordinate();
                Coordinate headCoordinate = snake.moveHead(fieldWidth, fieldHeight);
                if (food.contains(headCoordinate)) {
                    snake.grow(fieldWidth, fieldHeight, lastHead);
                    ateFood.add(headCoordinate);
                    increasePlayerScore(snake.getPlayerId());
                } else{
                    snake.moveBody(fieldWidth, fieldHeight);
                }
            }
            food.removeAll(ateFood);
        }
    }

    private boolean isSquareFree(Cell[][] field, int x, int y){
        for (int dx = 0; dx < 5; dx++) {
            for (int dy = 0; dy < 5; dy++) {
                int wrappedX = (x + dx) % gameConfig.width();
                int wrappedY = (y + dy) % gameConfig.height();
                if (field[wrappedX][wrappedY] instanceof SnakeCell) {
                    return false;
                }
            }
        }
        return true;

    }

    void generateNewFood() {
        Random random = new Random();
        Cell[][] field = new CellManager().createField(this);
        int foodStatic = gameConfig.food();
        int width = gameConfig.width();
        int height = gameConfig.height();
        int amountAliveSnakes = amountAliveSnakes();
        while (food.size() < foodStatic + amountAliveSnakes) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (field[x][y] instanceof EmptyCell) {
                field[x][y] = new FoodCell();
                food.add(new Coordinate(x, y));
            }
        }
    }

    public void handlePlayerLeave(int playerId) {
        synchronized (snakes) {
            snakes.stream()
                    .filter(s -> s.getPlayerId() == playerId).findAny()
                    .ifPresent(Snake::setZombie);
        }
        synchronized (playerInfos) {
            playerInfos.removeIf(player -> player.getId() == playerId);
        }
    }


    private Segment chooseRandomTail(Coordinate head){
        List<Course> courses = new ArrayList<>(List.of(Course.UP, Course.DOWN, Course.LEFT, Course.RIGHT));
        Collections.shuffle(courses);

        for (Course course : courses){
            Coordinate nextcoordinate = course.nextCoordinate(head, gameConfig.width(), gameConfig.height());
            if(!food.contains(nextcoordinate)){
                return new Segment(course, nextcoordinate);
            }
        }
        return null;
    }

    public void addFood(List<Coordinate> food) {
        this.food.addAll(food);
    }

    public void addSnakes(List<Snake> snakes) {
        this.snakes.addAll(snakes);
    }

    public void addPlayerInfos(List<PlayerInfo> playerInfos) {
        synchronized (this.playerInfos) {
            this.playerInfos.clear();
            this.playerInfos.addAll(playerInfos);
        }
    }

    public List<Integer> change() {
        int fieldWidth = gameConfig.width();
        int fieldHeight = gameConfig.height();

        moveSnakes(fieldWidth, fieldHeight);

        List<Integer> removedPlayers = detectCollisions();

        generateNewFood();
        stateOrder++;

        return removedPlayers;
    }

    public List<PlayerInfo> getPlayers() {
        return Collections.unmodifiableList(playerInfos);
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public List<PlayerInfo> getPlayerInfos() {
        return playerInfos;
    }

    public List<Snake> getSnakes() {
        return snakes;
    }

    public List<Coordinate> getFood() {
        return food;
    }

    public int getStateOrder() {
        return stateOrder;
    }

    public void setStateOrder(int stateOrder) {
        this.stateOrder = stateOrder;
    }
}
