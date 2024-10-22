package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class MemoryGameDAO extends MemoryBaseDAO<GameData> implements GameDataAccess{
    int nextGameId = 1;

    @Override
    public GameData addGameData(GameData gameData) throws DataAccessException {
        gameData.setGameId(nextGameId++);
        return addT(gameData);
    }

    @Override
    public Collection<GameData> listGameDatas() throws DataAccessException {
        return ts.values();
    }

    @Override
    public GameData getGameData(int gameID) throws DataAccessException {
        return getT("gameID", Integer.toString(gameID));
    }

    @Override
    public GameData updateGameData(GameData gameData) throws DataAccessException {
        updateT(gameData,"gameID", Integer.toString(gameData.gameID()));
        return gameData;
    }

    @Override
    public void deleteAllGameDatas() throws DataAccessException {
        deleteAllTs();
    }
}
