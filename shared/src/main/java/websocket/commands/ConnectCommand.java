package websocket.commands;

public class ConnectCommand extends UserGameCommand {

    private final JoinType joinType;

    public enum JoinType {
        OBSERVER,
        PLAYER
    }

    public ConnectCommand(String authToken, Integer gameID, JoinType joinType) {
        super(CommandType.CONNECT, authToken, gameID);
        this.joinType = joinType;
    }

    public JoinType getJoinType() {
        return joinType;
    }
}
