package boev.app.snake_game_net_4.model.conventers;

import boev.app.snake_game_net_4.SnakesProto;
import boev.app.snake_game_net_4.model.gameplayers.Role;

public class NodeRoleConverter {
    private final static NodeRoleConverter INSTANCE = new NodeRoleConverter();

    private NodeRoleConverter() {
    }

    public static NodeRoleConverter getInstance() {
        return INSTANCE;
    }

    public Role snakesProtoToNodeRole(SnakesProto.NodeRole role) {
        return switch (role) {
            case MASTER -> Role.MASTER;
            case NORMAL -> Role.NORMAL;
            case VIEWER -> Role.VIEWER;
            case DEPUTY -> Role.DEPUTY;
        };
    }

    public SnakesProto.NodeRole nodeRoleToSnakesProto(Role nodeRole) {
        return switch (nodeRole) {
            case MASTER -> SnakesProto.NodeRole.MASTER;
            case NORMAL -> SnakesProto.NodeRole.NORMAL;
            case VIEWER -> SnakesProto.NodeRole.VIEWER;
            case DEPUTY -> SnakesProto.NodeRole.DEPUTY;
        };
    }
}
