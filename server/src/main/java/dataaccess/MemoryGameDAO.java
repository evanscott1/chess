package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class MemoryGameDAO implements GameDataAccess{
    @Override
    public GameData addGameData(GameData gameData) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGameDatas() throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData getGameData(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData updateGameData(GameData gameData) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllGameDatas() throws DataAccessException {

    }
}
