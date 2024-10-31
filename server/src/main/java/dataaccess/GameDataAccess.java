package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    GameData addGameData(GameData gameData) throws ResponseException;

    Collection<GameData> listGameDatas() throws ResponseException;

    GameData getGameData(int gameID) throws ResponseException;

    GameData updateGameData(GameData gameData) throws ResponseException;

    void deleteAllGameDatas() throws ResponseException;
}
