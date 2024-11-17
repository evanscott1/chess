package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    private final ChessGame game;

    public LoadGameMessage(String message, ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.message = message;
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
