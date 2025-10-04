package boev.app.snake_game_net_4.model;

import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.controllers.utils.MessageDialogHelper;
import boev.app.snake_game_net_4.events.RenderGameFieldEvent;
import boev.app.snake_game_net_4.events.StartNewGameAnimationEvent;
import boev.app.snake_game_net_4.events.UpdateAvailableGamesEvent;
import boev.app.snake_game_net_4.events.UpdateGameScoresEvent;
import boev.app.snake_game_net_4.events.model.*;
import boev.app.snake_game_net_4.events.switchs.SwitchToGameEvent;
import boev.app.snake_game_net_4.model.cell.Coordinate;
import boev.app.snake_game_net_4.model.communication.GameAnnouncementMsgHandler;
import boev.app.snake_game_net_4.model.communication.Message;
import boev.app.snake_game_net_4.model.communication.udp.CommunicationManager;
import boev.app.snake_game_net_4.model.communication.udp.GameMessageHandler;
import boev.app.snake_game_net_4.model.communication.udp.MySocket;
import boev.app.snake_game_net_4.model.communication.udp.UDPMessageReceiverAndSender;
import boev.app.snake_game_net_4.model.conventers.GameMessagesCreator;
import boev.app.snake_game_net_4.model.gameplayers.GamePlayer;
import boev.app.snake_game_net_4.model.gameplayers.InetInfo;
import boev.app.snake_game_net_4.model.gameplayers.PlayerInfo;
import boev.app.snake_game_net_4.model.gameplayers.Role;
import boev.app.snake_game_net_4.model.snake.Course;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static boev.app.snake_game_net_4.model.communication.udp.InetConfig.MULTICAST_ADDRESS;
import static boev.app.snake_game_net_4.model.communication.udp.InetConfig.MULTICAST_PORT;

public class MainModel {
    private static final long GAME_ANNOUNCEMENT_TTL = 2000;
    private String gameName;
    private final InetAddress multicastAddress;
    private final Map<GameAnnouncement, Long> gamesRepository = new HashMap<>();
    private EventBus controllersEventBus;
    private final EventBus modelEventBus;
    private GameState gameState; // состояние игры
    private PlayerInfo me;
    private CommunicationManager communicationManager; // основная логика обработки сообщений
    private GameAnnouncementMsgHandler gameAnnouncementMsgHandler; // обработчик мультикастаных сообщений
    private GameMessageHandler gameMsgHandler ; // обработчик юникастных
    private final UDPMessageReceiverAndSender messageReceiverAndSender; // сам класс, который отвечает за взаимодействие с datagram
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private MainModel(UDPMessageReceiverAndSender messageReceiverAndSender,
                  GameAnnouncementMsgHandler gameAnnouncementMsgHandler, EventBus modelEventBus,
                  InetAddress multicastAddress) {
        this.messageReceiverAndSender = messageReceiverAndSender;
        this.multicastAddress = multicastAddress;
        this.modelEventBus = modelEventBus;
        this.gameAnnouncementMsgHandler = gameAnnouncementMsgHandler;
    }
    public static MainModel create() throws IOException {
        EventBus modelEventBus = new EventBus();
        GameAnnouncementMsgHandler gameAnnouncementMsgHandler = new GameAnnouncementMsgHandler(modelEventBus);
        InetAddress multicastAddress = InetAddress.getByName(MULTICAST_ADDRESS);
        MainModel model = new MainModel(UDPMessageReceiverAndSender.getInstance(), gameAnnouncementMsgHandler, modelEventBus, multicastAddress);
        modelEventBus.register(model);
        gameAnnouncementMsgHandler.start();
        return model;
    }

    public void createNewGame(GameState gameState, PlayerInfo playerInfo, String gameName) throws IOException {
        this.gameState = gameState;
        this.me = playerInfo;
        this.gameName = gameName;
        if (addFirstPlayer(gameState, playerInfo)) {
            return;
        }
        setNewGameCommunicationManager(gameState, playerInfo);
        startGameMessageHandler(gameState);
        scheduler.scheduleAtFixedRate(this::multicastAnnounceGame, 1, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::detectPlayersToSendPingMsg, 0, communicationManager.getTenthOfDelay() / 2, TimeUnit.MILLISECONDS);
        startPlayersExpiredDetectors(communicationManager);
    }

    public void joinGame(GameAnnouncement gameAnnouncement, String nickname, boolean isViewer) throws IOException, InterruptedException {
        gameAnnouncementMsgHandler.interrupt();
        Role role = Role.NORMAL;
        if (isViewer) {
            role = Role.VIEWER;
        }
        this.gameName = gameAnnouncement.gameName();
        GameConfig gameConfig = gameAnnouncement.gameConfig();
        this.gameState = new GameState(gameConfig);
        this.communicationManager = CommunicationManager.create(gameConfig.delay(), modelEventBus);
        if (!startJoining(gameAnnouncement, nickname, role)) {
            return;
        }
        startGameMessageHandler(gameState);
        startPlayersExpiredDetectors(communicationManager);

    }

    @Subscribe
    public void handleJoinMsg(HandleJoinMsgEvent e) {
        communicationManager.handleJoinMessage(e.joinMsg(), e.senderSocket(), e.msgSeq(), gameState, me);
    }

    @Subscribe
    public void handleDiscoverMsg(HandleDiscoverMsgEvent e) {
        if (communicationManager != null) {
            communicationManager.unicastGameAnnounce(e.senderSocket(), me, gameState, gameName);
        }
    }

    @Subscribe
    public void handleGameStateMsg(HandleGameStateMsgEvent e) {
        if (communicationManager.handleGameStateMessage(gameState, e.newGameState(), e.players(), e.senderSocket(), me, e.msgSeq())) {
            gameState = e.newGameState();
            controllersEventBus.post(new RenderGameFieldEvent(gameState));
            controllersEventBus.post(new UpdateGameScoresEvent());
        }
    }

    @Subscribe
    public void handleAckMsg(HandleAckMsgEvent e) {
        communicationManager.handleAckMsg(e.senderID(), e.msgSeq());
    }

    @Subscribe
    public void handlePingMsg(HandlePingMsgEvent e) {
        communicationManager.handlePingMsg(e.senderSocket(), me, e.msgSeq());
    }

    @Subscribe
    public void handleSteerMsg(HandleSteerMsgEvent e) {
        communicationManager.handleSteerMsg(gameState, e.newCourse(), e.senderSocket(), me, e.msgSeq());
    }

    @Subscribe
    void handleRoleChangeMsg(HandleRoleChangeMsgEvent e) {
        communicationManager.handleRoleChangeMsg(e.senderRole(), e.receiverRole(), e.senderId(), e.receiverId(), e.msgSeq(), me, e.senderSocket());
    }

    @Subscribe
    public void handleGameAnnouncement(HandleGameAnnouncementMsgEvent e) {
        synchronized (gamesRepository) {
            gamesRepository.put(e.gameAnnouncement(), Instant.now().toEpochMilli());
            if (controllersEventBus != null) {
                controllersEventBus.post(new UpdateAvailableGamesEvent(gamesRepository.keySet().stream().toList()));
            }
        }
    }

    @Subscribe
    public void handleErrorMsg(HandleErrorMsgEvent e) {
        communicationManager.handleErrorMsg(e.senderSocket(), me, e.msgSeq());
    }

    @Subscribe
    public void startMasterRoutine(StartMasterRoutineEvent e) {
        gameAnnouncementMsgHandler = new GameAnnouncementMsgHandler(modelEventBus);
        gameAnnouncementMsgHandler.start();
        controllersEventBus.post(new StartNewGameAnimationEvent(gameState.getGameConfig().delay()));
        scheduler.scheduleAtFixedRate(this::detectPlayersToSendPingMsg, 0, communicationManager.getTenthOfDelay() / 2, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(this::multicastAnnounceGame, 1, 1, TimeUnit.SECONDS);
    }

    public void stopScheduler() {
        scheduler.shutdownNow();
    }

    public void end() {
        stopScheduler();
        communicationManager.close(me);
        gameMsgHandler.interrupt();
        gameAnnouncementMsgHandler.interrupt();
        modelEventBus.unregister(this);
    }

    public void changeGameState() {
        communicationManager.nextState(me, gameState);
    }

    public void steerSnake(Course newCourse) {
        communicationManager.steer(newCourse, me, gameState);
    }

    private void startPlayersExpiredDetectors(CommunicationManager communicationManager) {
        scheduler.scheduleAtFixedRate(this::resendUnconfirmedMsg, 0, communicationManager.getTenthOfDelay() / 2, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(this::findExpired, 0, communicationManager.getDelay() / 2, TimeUnit.MILLISECONDS);
    }

    public void multicastAnnounceGame() {
        communicationManager.multicastGameAnnounce(gameState, me, gameName);
    }

    private void detectPlayersToSendPingMsg() {
        communicationManager.detectPlayersToSendPingMsg(me);
    }

    private void findExpired() {
        communicationManager.findExpired(me, gameState);
    }

    private void resendUnconfirmedMsg() {
        communicationManager.resendUnconfirmed();
    }

    private boolean addFirstPlayer(GameState gameState, PlayerInfo playerInfo) {
         gameState.generateNewFood();
        Coordinate free = gameState.findFreeArea();
        if (free != null) {
            gameState.addNewPlayer(playerInfo, free);
        } else {
            MessageDialogHelper.error("Can't create new game");
            return true;
        }
        return false;
    }

    private void setNewGameCommunicationManager(GameState gameState, PlayerInfo playerInfo) throws IOException {
        CommunicationManager communicationManager = CommunicationManager.create(gameState.getGameConfig().delay(), modelEventBus);
        GamePlayer master = new GamePlayer(playerInfo, Role.MASTER, new InetInfo(null, 0, 0));
        communicationManager.addMaster(master);
        this.communicationManager = communicationManager;
    }


    private void startGameMessageHandler(GameState gameState) {
        gameMsgHandler = new GameMessageHandler(modelEventBus, gameState.getGameConfig());
        gameMsgHandler.start();
    }

    public void setControllersEventBus(EventBus controllersEventBus) {
        this.controllersEventBus = controllersEventBus;
    }

    public List<PlayerInfo> getCurrentPlayers() {
        return gameState.getPlayers().stream().sorted(Comparator.comparing(PlayerInfo::getScore).reversed().thenComparing(PlayerInfo::getNickname)).toList();
    }

    public void sendDiscoverMsg() throws IOException {
        SnakesProto.GameMessage gameMessage = GameMessagesCreator.getInstance().createDiscoverMsg(0);
        messageReceiverAndSender.send(new Message(gameMessage, new MySocket(multicastAddress, MULTICAST_PORT)));
    }

    public void removeExpiredGames() {
        synchronized (gamesRepository) {
            long now = Instant.now().toEpochMilli();
            Set<Map.Entry<GameAnnouncement, Long>> toDelete = gamesRepository.entrySet()
                    .stream().filter(entry -> now - entry.getValue() > GAME_ANNOUNCEMENT_TTL).collect(Collectors.toSet());
            toDelete.forEach(entry -> gamesRepository.remove(entry.getKey()));
            if (!toDelete.isEmpty()) {
                controllersEventBus.post(new UpdateAvailableGamesEvent(gamesRepository.keySet().stream().toList()));
            }
        }
    }

    private boolean startJoining(GameAnnouncement gameAnnouncement, String nickname, Role role) throws IOException {
        communicationManager.sendJoinMsg(gameAnnouncement, nickname, role);
        int id = communicationManager.receiveAskMsgAfterJoinMsg(role);
        if (id == -1) {
            MessageDialogHelper.error("Can't join this game");
            return false;
        }
        else if(id == -10){
            MessageDialogHelper.error("Can't join this game");
            return false;
        }
        this.me = new PlayerInfo(nickname, id);
        controllersEventBus.post(new SwitchToGameEvent());
        return true;
    }

    public GameState getGameState() {
        return gameState;
    }

    public PlayerInfo getMe() {
        return me;
    }
}
