package model;

import chess.ChessGame;
import com.google.gson.*;

import java.util.Objects;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameData gameData = (GameData) o;
        return gameID == gameData.gameID && Objects.equals(game, gameData.game) && Objects.equals(gameName, gameData.gameName) && Objects.equals(whiteUsername, gameData.whiteUsername) && Objects.equals(blackUsername, gameData.blackUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }

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
