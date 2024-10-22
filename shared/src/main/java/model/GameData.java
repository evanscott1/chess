package model;

import chess.ChessGame;
import com.google.gson.*;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public GameData setGameId(int gameId) {
        return new GameData(gameId, this.whiteUsername, this.blackUsername, this.gameName, this.game);
    }

    public GameData setWhiteUsername(String username) {
        return new GameData(this.gameID, username, this.blackUsername, this.gameName, this.game);
    }

    public GameData setBlackUsername(String username) {
        return new GameData(this.gameID, this.whiteUsername, username, this.gameName, this.game);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
