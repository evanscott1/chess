package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    private final ChessGame game;
    private ChessGame.TeamColor teamColor;

    public LoadGameMessage(String message, ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.message = message;
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setTeamColor(ChessGame.TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
