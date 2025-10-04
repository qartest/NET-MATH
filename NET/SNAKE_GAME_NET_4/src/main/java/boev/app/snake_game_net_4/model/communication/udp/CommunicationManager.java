package boev.app.snake_game_net_4.model.communication.udp;

import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.events.model.StartMasterRoutineEvent;
import boev.app.snake_game_net_4.model.GameAnnouncement;
import boev.app.snake_game_net_4.model.GameState;
import boev.app.snake_game_net_4.model.cell.Coordinate;
import boev.app.snake_game_net_4.model.communication.Message;
import boev.app.snake_game_net_4.model.communication.UnconfirmedGameMessagesStorage;
import boev.app.snake_game_net_4.model.conventers.GameMessagesCreator;
import boev.app.snake_game_net_4.model.conventers.NodeRoleConverter;
import boev.app.snake_game_net_4.model.gameplayers.*;
import boev.app.snake_game_net_4.model.snake.Course;
import com.google.common.eventbus.EventBus;


import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;


import static boev.app.snake_game_net_4.model.communication.udp.InetConfig.MULTICAST_ADDRESS;
import static boev.app.snake_game_net_4.model.communication.udp.InetConfig.MULTICAST_PORT;

public class CommunicationManager {
    private final UnconfirmedGameMessagesStorage unconfirmedGameMessagesStorage = new UnconfirmedGameMessagesStorage();
    private static final int ATTEMPTS_TO_JOIN = 200;
    private final AllGamePlayers gamePlayersStorage = new AllGamePlayers();
    private final UDPMessageReceiverAndSender messageReceiverAndSender;
    private final MySocket multicastSocket;

    private final EventBus modelEventBus;
    private final AtomicInteger msgSeq = new AtomicInteger(0);
    private final long delay;
    private final long tenthOfDelay;
    private final Map<Integer, Long> lastSteerMsgSeq = new HashMap<>();

    private CommunicationManager(UDPMessageReceiverAndSender messageReceiverAndSender,
                                 InetAddress multicastInetAddress, int delay, EventBus modelEventBus) {
        this.messageReceiverAndSender = messageReceiverAndSender;
        this.multicastSocket = new MySocket(multicastInetAddress, MULTICAST_PORT);
        this.delay = delay;
        this.tenthOfDelay = delay / 10;
        this.modelEventBus = modelEventBus;
    }

    public long getDelay() {
        return delay;
    }

    public long getTenthOfDelay() {
        return tenthOfDelay;
    }

    public static CommunicationManager create(int delay, EventBus modelEventBus) throws IOException {
        InetAddress inetAddress = InetAddress.getByName(MULTICAST_ADDRESS);
        UDPMessageReceiverAndSender unicastMsgReceiverAndSender = UDPMessageReceiverAndSender.getInstance();
        return new CommunicationManager(unicastMsgReceiverAndSender, inetAddress, delay, modelEventBus);
    }

    public boolean handleGameStateMessage(GameState currentGameState, GameState newState, List<GamePlayer> gamePlayers,
                                          MySocket senderSocket, PlayerInfo me, long msgSeq) {
        synchronized (gamePlayersStorage) {
            GamePlayer maybeMaster = gamePlayersStorage.findGamePlayerBySocket(senderSocket).orElse(null);
            if (maybeMaster == null) {
                return false;
            }

            maybeMaster.updateLastCommunication();

            if (newState.getStateOrder() > currentGameState.getStateOrder()) {
                gamePlayersStorage.setGamePlayers(gamePlayers.stream().peek(GamePlayer::updateLastCommunication).toList());
                GamePlayer master = gamePlayersStorage.findGamePlayerByRole(Role.MASTER).orElse(null);
                if (master == null) {
                    return false;
                }
                gamePlayersStorage.delete(master);
                gamePlayersStorage.add(new GamePlayer(master.getPlayerInfo(),Role.MASTER, new InetInfo(senderSocket, Instant.now().toEpochMilli())));
                sendAckMsg(me.getId(), master.getId(), senderSocket, msgSeq);
                return true;
            }
            gamePlayers.stream().filter(p -> p.getRole() == Role.MASTER).findAny().ifPresent(m ->  sendAckMsg(me.getId(), m.getId(), senderSocket, msgSeq));
            return false;
        }
    }

    public void handlePingMsg(MySocket senderSocket, PlayerInfo me, long messageSeq) {
        synchronized (gamePlayersStorage) {
            GamePlayer gamePlayer = gamePlayersStorage.findGamePlayerBySocket(senderSocket).orElse(null);
            if (gamePlayer == null) {
                return;
            }
            gamePlayer.updateLastCommunication();
            sendAckMsg(me.getId(), gamePlayer.getId(), senderSocket, messageSeq);
        }
    }

    public void handleSteerMsg(GameState gameState, Course newCourse,
                               MySocket senderSocket, PlayerInfo me, long msgSeq) {

        synchronized (gamePlayersStorage) {
            if (isRole(Role.MASTER, me.getId())) {
                GamePlayer gamePlayer = gamePlayersStorage.findGamePlayerBySocket(senderSocket).orElse(null);
                if (gamePlayer == null) {
                    return;
                }
                Long lastMsgSeq = lastSteerMsgSeq.get(gamePlayer.getId());
                if (lastMsgSeq != null) {
                    if (msgSeq < lastMsgSeq) {
                        return;
                    }
                }
                lastSteerMsgSeq.put(gamePlayer.getId(), msgSeq);
                gameState.steerSnake(gamePlayer.getId(), newCourse);
                gamePlayer.updateLastCommunication();
                sendAckMsg(me.getId(), gamePlayer.getId(), senderSocket, msgSeq);
            }
        }
    }
    public void handleRoleChangeMsg(Role senderRole, Role receiverRole,
                                    int senderId, int receiverId, long messageSeq, PlayerInfo me,
                                     MySocket senderSocket) {
        synchronized (gamePlayersStorage) {
            if (isRole(Role.MASTER, me.getId()) && isRole(Role.DEPUTY, senderId) && senderRole == Role.VIEWER) {
                gamePlayersStorage.findGamePlayerByRole(Role.NORMAL).ifPresent(p -> {
                    p.setRole(Role.DEPUTY);
                    sendRoleChangeMsg
                            (Role.MASTER, Role.DEPUTY, me.getId(), p.getId(), p.getSocket());
                });
            } else if (isRole(Role.DEPUTY, me.getId()) && receiverRole == Role.MASTER) {
                modelEventBus.post(new StartMasterRoutineEvent());

                gamePlayersStorage.findGamePlayerByRole(Role.NORMAL).ifPresent(p -> {
                    p.setRole(Role.DEPUTY);
                    sendRoleChangeMsg(Role.MASTER, Role.DEPUTY, me.getId(), p.getId(), p.getSocket());
                });

            }

            gamePlayersStorage.findGamePlayerById(senderId).ifPresent(p -> {
                p.setRole(senderRole);
                p.updateLastCommunication();
            });

            gamePlayersStorage.findGamePlayerById(receiverId).ifPresent(p -> p.setRole(receiverRole));
            sendAckMsg(me.getId(), senderId, senderSocket, messageSeq);
        }
    }

    public void handleAckMsg(int senderId, long messageSeq) {
        synchronized (unconfirmedGameMessagesStorage) {
            unconfirmedGameMessagesStorage.confirm(messageSeq);
        }
        synchronized (gamePlayersStorage) {
            gamePlayersStorage.findGamePlayerById(senderId).ifPresent(GamePlayer::updateLastCommunication);
        }
    }

    public void handleJoinMessage(SnakesProto.GameMessage.JoinMsg joinMsg, MySocket senderSocket, long msgSeq,
                                  GameState gameState, PlayerInfo me) {

        Role role = NodeRoleConverter.getInstance().snakesProtoToNodeRole(joinMsg.getRequestedRole());
        if (role != Role.NORMAL && role != Role.VIEWER) {
            return;
        }

        synchronized (gamePlayersStorage) {

            GamePlayer sender = gamePlayersStorage.findGamePlayerBySocket(senderSocket).orElse(null);
            if (sender != null) {
                sendAckMsg(me.getId(), sender.getId(), senderSocket, msgSeq);
                return;
            }

            if (isRole(Role.MASTER, me.getId())) {
                String playerName = joinMsg.getPlayerName();
                InetInfo inetInfo = new InetInfo(senderSocket, -1);

                PlayerInfo playerInfo = new PlayerInfo(playerName);
                GamePlayer newGamePlayer = new GamePlayer(playerInfo, role, inetInfo);

                if (role != Role.VIEWER) {
                    Coordinate freeAreaCoordinate = gameState.findFreeArea();
                    if (freeAreaCoordinate != null) {
                        if (gameState.addNewPlayer(playerInfo, freeAreaCoordinate)) {
                            if (gamePlayersStorage.findGamePlayerByRole(Role.DEPUTY).isEmpty()) {
                                newGamePlayer.setRole(Role.DEPUTY);
                            }
                        } else {
                            sendErrorMsg("Can't find free area", newGamePlayer.getSocket());
                            return;
                        }
                    } else {
                        sendErrorMsg("Can't find free area", newGamePlayer.getSocket());
                        return;
                    }
                }

                gamePlayersStorage.add(newGamePlayer);
                sendAckMsg(me.getId(), playerInfo.getId(), senderSocket, msgSeq);
            }
        }
    }

    public void handleErrorMsg(MySocket senderSocket, PlayerInfo me, long messageSeq) {
        synchronized (gamePlayersStorage) {
            GamePlayer gamePlayer = gamePlayersStorage.findGamePlayerBySocket(senderSocket).orElse(null);
            if (gamePlayer == null) {
                return;
            }
            gamePlayer.updateLastCommunication();
            sendAckMsg(me.getId(), gamePlayer.getId(), senderSocket, messageSeq);
        }
    }
    public void detectPlayersToSendPingMsg(PlayerInfo me) {
        synchronized (gamePlayersStorage) {
            if (!isRole(Role.MASTER, me.getId())) {
                GamePlayer master = gamePlayersStorage.findGamePlayerByRole(Role.MASTER).orElse(null);
                if (master != null) {
                    if (master.getLastCommunicationTime() > tenthOfDelay) {
                        sendPingMsg(me.getId(), master.getId(), master.getSocket());
                    }
                }
            } else {
                List<GamePlayer> toSendPingMsgPlayers =
                        gamePlayersStorage.findForLastCommunicationPlayer(tenthOfDelay, me.getId());

                for (GamePlayer player : toSendPingMsgPlayers) {
                    if (me.getId() != player.getId()) {
                        sendPingMsg(me.getId(), player.getId(), player.getSocket());
                    }
                }
            }
        }
    }

    public void findExpired(PlayerInfo me, GameState gameState) {
        synchronized (gamePlayersStorage) {
            List<GamePlayer> expired = gamePlayersStorage.findForLastCommunicationPlayer(delay, me.getId());
            expired.forEach(player -> {
                gameState.handlePlayerLeave(player.getId());
                this.handleExpire(player, me);
            });
        }
    }



    private void handleExpire(GamePlayer gamePlayer, PlayerInfo me) {
        synchronized (gamePlayersStorage) {
            if (isRole(Role.MASTER, me.getId())) {
                synchronized (unconfirmedGameMessagesStorage) {
                    unconfirmedGameMessagesStorage.cancelConfirmation(gamePlayer.getId());
                }
                if (gamePlayer.getRole() == Role.DEPUTY) {
                    gamePlayersStorage.findGamePlayerByRole(Role.NORMAL).ifPresent(p -> {
                        p.setRole(Role.DEPUTY);
                        sendRoleChangeMsg(Role.MASTER, Role.DEPUTY, me.getId(), p.getId(), p.getSocket());
                    });
                }
                gamePlayersStorage.delete(gamePlayer);
                return;
            }

            if (isRole(Role.NORMAL, me.getId()) && gamePlayer.getRole() == Role.MASTER) {
                GamePlayer deputy = gamePlayersStorage.findGamePlayerByRole(Role.DEPUTY).orElse(null);
                if (deputy == null) {
                    return;
                }
                synchronized (unconfirmedGameMessagesStorage) {
                    unconfirmedGameMessagesStorage.replaceDestination(gamePlayer.getId(), deputy.getSocket());
                }
                deputy.setRole(Role.MASTER);
                gamePlayersStorage.delete(gamePlayer);
                return;
            }

            if (isRole(Role.DEPUTY, me.getId()) && gamePlayer.getRole() == Role.MASTER) {
                synchronized (unconfirmedGameMessagesStorage) {
                    unconfirmedGameMessagesStorage.cancelConfirmation(gamePlayer.getId());
                }
                GamePlayer mePlayer = gamePlayersStorage.findGamePlayerById(me.getId()).orElse(null);
                if (mePlayer == null) {
                    return;
                }
                mePlayer.setRole(Role.MASTER);
                gamePlayersStorage.delete(gamePlayer);
                gamePlayersStorage.getGamePlayers().forEach(player -> {
                    if (player.getId() != me.getId()) {
                        sendRoleChangeMsg(Role.MASTER, player.getRole(), me.getId(), player.getId(), player.getSocket());
                        player.updateLastCommunication();
                    }
                });
                modelEventBus.post(new StartMasterRoutineEvent());
                gamePlayersStorage.findGamePlayerByRole(Role.NORMAL).ifPresent(p -> {
                    p.setRole(Role.DEPUTY);
                    sendRoleChangeMsg(Role.MASTER, Role.DEPUTY, me.getId(), p.getId(), p.getSocket());
                }); // назначаем заместителя


            }
        }
    }

    public void resendUnconfirmed() {
        synchronized (unconfirmedGameMessagesStorage) {
            unconfirmedGameMessagesStorage.getUnconfirmedMessages().forEach(message -> {
                long instant = Instant.now().toEpochMilli();
                if (instant - message.getSentAt() > tenthOfDelay) {
                    if (message.getSocket().port() == 0) {
                        unconfirmedGameMessagesStorage.confirm(message.getMessage().getMsgSeq());
                    } else {
                        CompletableFuture.runAsync(() -> sendMessage(message));
                    }
                }
            });
        }
    }

    public void addMaster(GamePlayer gamePlayer) {
        synchronized (gamePlayersStorage) {
            if (gamePlayersStorage.findGamePlayerByRole(Role.MASTER).isEmpty()
                    && gamePlayersStorage.findGamePlayerById(gamePlayer.getId()).isEmpty()) {
                gamePlayer.setRole(Role.MASTER);
                gamePlayersStorage.add(gamePlayer);
            }
        }
    }



    public void nextState(PlayerInfo me, GameState gameState) {
        if (isRole(Role.MASTER, me.getId())) {
            List<Integer> ids = gameState.change();
            synchronized (gamePlayersStorage) {
                handleDeadPlayers(me, ids);
                gameState.addPlayerInfos(gamePlayersStorage.getGamePlayers().stream().map(GamePlayer::getPlayerInfo).toList());
                gamePlayersStorage.getGamePlayers().forEach(player -> {
                    if (player.getId() != me.getId()) {
                        sendGameStateMsg(me.getId(), player.getId(), gameState, player);
                    }
                });
            }
        }
    }

    public void sendJoinMsg(GameAnnouncement gameAnnouncement, String nickname, Role role) {
        int msgSeq = nextMsgSeq();
        SnakesProto.GameMessage joinMsg = GameMessagesCreator.getInstance().createJoinMsg(gameAnnouncement.gameName(), nickname, role, msgSeq);
        Message message = new Message(joinMsg, gameAnnouncement.senderSocket());
        synchronized (unconfirmedGameMessagesStorage) {
            unconfirmedGameMessagesStorage.add(message);
        }
        sendMessage(message);
    }

    public int receiveAskMsgAfterJoinMsg(Role role) throws IOException{
        synchronized (gamePlayersStorage) {
            for (int i = 0; i < ATTEMPTS_TO_JOIN; i++) {
                Message response = messageReceiverAndSender.receive();
                SnakesProto.GameMessage gameMsg = response.getMessage();

//                System.out.println("Попытка " + (i + 1) + ": Получено сообщение: " + gameMsg);

                if (gameMsg.hasAck()) {
                    synchronized (unconfirmedGameMessagesStorage) {
                        unconfirmedGameMessagesStorage.confirm(gameMsg.getMsgSeq());
                    }
//                    System.out.println("Получено подтверждение с ID: " + gameMsg.getReceiverId());

                    GamePlayer master = new GamePlayer(new PlayerInfo("", gameMsg.getSenderId()), Role.MASTER,
                            new InetInfo(response.getSocket(), -1));
                    GamePlayer me = new GamePlayer(new PlayerInfo("", gameMsg.getReceiverId()), role,
                            new InetInfo(InetAddress.getLocalHost(), 0, -1));
                    addMaster(master);
                    if (!me.equals(master)) {
                        gamePlayersStorage.add(me);
                    }
                    return gameMsg.getReceiverId();
                }

                if (gameMsg.hasError()) {
//                    System.out.println("Получена ошибка: " + gameMsg.getError());
                    handleErrorMsg(response.getSocket(), new PlayerInfo("", 0), gameMsg.getMsgSeq());
                    return -1;
                }
            }
//            System.out.println("Не удалось подключиться после " + ATTEMPTS_TO_JOIN + " попыток.");
            return -1;
        }
    }

    public void steer(Course newCourse, PlayerInfo me, GameState currentGameState) {
        synchronized (gamePlayersStorage) {
            GamePlayer master = gamePlayersStorage.findGamePlayerByRole(Role.MASTER).orElse(null);
            if (master == null) {
                return;
            }
            if (master.getId() == me.getId()) {
                currentGameState.steerSnake(me.getId(), newCourse);
            } else {
                sendSteerMsg(newCourse, me, master);
            }
        }

    }

    public void multicastGameAnnounce(GameState gameState, PlayerInfo me, String gameName) {
        if (gameState == null) {
            return;
        }
        if (isRole(Role.MASTER, me.getId())) {
            sendGameAnnouncementMsg(multicastSocket, gameState, gameName);
        }
    }

    public void unicastGameAnnounce(MySocket socket, PlayerInfo me, GameState gameState, String gameName) {
        if (gameState == null) {
            return;
        }
        if (isRole(Role.MASTER, me.getId())) {
            sendGameAnnouncementMsg(socket, gameState, gameName);
        }
    }


    public void close(PlayerInfo me) {
        synchronized (gamePlayersStorage) {
            GamePlayer master = gamePlayersStorage.findGamePlayerByRole(Role.MASTER).orElse(null);
            if (master != null) {
                if (master.getId() == me.getId()) {
                    GamePlayer deputy = gamePlayersStorage.findGamePlayerByRole(Role.DEPUTY).orElse(null);
                    if (deputy != null) {
                        deputy.setRole(Role.MASTER);
                        sendRoleChangeMsg(Role.VIEWER, Role.MASTER, me.getId(), deputy.getId(), deputy.getSocket());
                    }
                } else {
                    sendRoleChangeMsg(Role.VIEWER, Role.MASTER, me.getId(), master.getId(), master.getSocket());
                }
            }
        }
        messageReceiverAndSender.close();
    }

    private void sendGameStateMsg(int senderID, int receiverID, GameState gameState, GamePlayer gamePlayer) {
        CompletableFuture.runAsync(() -> {
            synchronized (gamePlayersStorage) {
                SnakesProto.GameMessage gameStateMsg =
                        GameMessagesCreator.getInstance().createGameStateMsg(senderID, receiverID, gameState,
                                gamePlayersStorage.getGamePlayers(), nextMsgSeq());
                Message message = new Message(gameStateMsg, gamePlayer.getSocket());
                synchronized (unconfirmedGameMessagesStorage) {
                    unconfirmedGameMessagesStorage.add(message);
                }
                sendMessage(message);
            }
        });
    }

    private void sendGameAnnouncementMsg(MySocket toSendSocket, GameState gameState, String currentGameName) {
        CompletableFuture.runAsync(() -> {
            synchronized (gamePlayersStorage) {
                SnakesProto.GameMessage announcementMsg = GameMessagesCreator.getInstance().createAnnouncementMsg(
                        gameState.getGameConfig(), gamePlayersStorage.getGamePlayers(), currentGameName, true, nextMsgSeq());
                Message message = new Message(announcementMsg, toSendSocket);
                sendMessage(message);
            }
        });
    }

    private void sendRoleChangeMsg(Role senderRole, Role receiverRole, int senderId, int receiverId, MySocket toSendSocket) {
        SnakesProto.GameMessage roleChangedMsg =
                GameMessagesCreator.getInstance().createRoleChangedMsg(senderRole, receiverRole, senderId, receiverId, nextMsgSeq());
        Message message = new Message(roleChangedMsg, toSendSocket);
        synchronized (unconfirmedGameMessagesStorage) {
            unconfirmedGameMessagesStorage.add(message);
        }
        sendMessage(message);
    }

    private int nextMsgSeq(){
        return msgSeq.incrementAndGet();
    }
    private boolean isRole(Role role, int id) {
        synchronized (gamePlayersStorage) {
            GamePlayer me = gamePlayersStorage.findGamePlayerById(id).orElse(null);
            if (me == null) {
                return false;
            }
            return me.getRole() == role;
        }
    }
    private void sendErrorMsg(String cause, MySocket toSendSocket) {
        CompletableFuture.runAsync(() -> {
            SnakesProto.GameMessage errorMsg =
                    GameMessagesCreator.getInstance().createErrorMsg(cause, nextMsgSeq());
            Message message = new Message(errorMsg, toSendSocket);
            synchronized (unconfirmedGameMessagesStorage) {
                unconfirmedGameMessagesStorage.add(message);
            }
            sendMessage(message);
        });
    }
    private void sendAckMsg(int senderId, int receiverId, MySocket toSendSocket, long messageSeq) {
        CompletableFuture.runAsync(() -> {
            SnakesProto.GameMessage ackMsg =
                    GameMessagesCreator.getInstance().createAckMsg(senderId, receiverId, messageSeq);
            Message message = new Message(ackMsg, toSendSocket);
            sendMessage(message);
        });
    }
    private void sendPingMsg(int senderId, int receiverId, MySocket toSendSocket) {
        CompletableFuture.runAsync(() -> {
            synchronized (gamePlayersStorage) {
                SnakesProto.GameMessage pingMsg =
                        GameMessagesCreator.getInstance().createPingMsg(senderId, receiverId, nextMsgSeq());
                Message message = new Message(pingMsg, toSendSocket);
                synchronized (unconfirmedGameMessagesStorage) {
                    unconfirmedGameMessagesStorage.add(message);
                }
                sendMessage(message);
            }
        });
    }
    private void sendSteerMsg(Course newCourse, PlayerInfo me, GamePlayer master) {
        CompletableFuture.runAsync(() -> {
            synchronized (gamePlayersStorage) {
                SnakesProto.GameMessage steerMsg = GameMessagesCreator.getInstance().createSteerMsg(newCourse, me.getId(), nextMsgSeq());
                Message message = new Message(steerMsg, master.getSocket());
                synchronized (unconfirmedGameMessagesStorage) {
                    unconfirmedGameMessagesStorage.add(message);
                }
                sendMessage(message);
            }
        });
    }

    private void sendMessage(Message message) {
        try {
            messageReceiverAndSender.send(message);
            message.setSentAt(Instant.now().toEpochMilli());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeadPlayers(PlayerInfo me, List<Integer> ids) {
        synchronized (gamePlayersStorage) {
            for (int id : ids) {
                if (me.getId() != id) {
                    GamePlayer player = gamePlayersStorage.findGamePlayerById(id).orElse(null);
                    if (player == null) {
                        continue;
                    }
                    if (player.getRole() == Role.DEPUTY) {
                        gamePlayersStorage.findGamePlayerByRole(Role.NORMAL).ifPresent(p -> {
                            p.setRole(Role.DEPUTY);
                            sendRoleChangeMsg(Role.MASTER, Role.DEPUTY, me.getId(), p.getId(), p.getSocket());
                        });
                    }
                    player.setRole(Role.VIEWER);
                    sendRoleChangeMsg(Role.MASTER, Role.VIEWER, me.getId(), id, player.getSocket());
                }
            }
        }
    }



}
