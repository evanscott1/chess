package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    GameData addGameData(GameData gameData) throws DataAccessException;

    Collection<GameData> listGameDatas() throws DataAccessException;

    GameData getGameData(int gameID) throws DataAccessException;

    GameData updateGameData(GameData gameData) throws DataAccessException;

    void deleteAllGameDatas() throws DataAccessException;
}
