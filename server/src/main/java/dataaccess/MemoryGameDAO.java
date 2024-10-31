package dataaccess;

import exception.ResponseException;
import model.GameData;
import service.BadRequestException;

import java.util.Collection;

public class MemoryGameDAO extends MemoryBaseDAO<GameData> implements GameDataAccess {
    int nextGameId = 1;


    @Override
    public GameData addGameData(GameData gameData) throws ResponseException {
        if (gameData.gameName().isEmpty()) {
            throw new BadRequestException("Game name cannot be empty.");
        }
        gameData = gameData.setGameId(nextGameId++);
        return addT(gameData);
    }


    @Override
    public GameData getGameData(int gameID) throws ResponseException {
        if (gameID < 1) {
            throw new BadRequestException("Game cannot be less than 1.");
        }
        return getT("gameID", Integer.toString(gameID));
    }

    @Override
    public Collection<GameData> listGameDatas() throws ResponseException {
        return listTs();
    }

    @Override
    public GameData updateGameData(GameData gameData) throws ResponseException {
        if (gameData.gameName().isEmpty()) {
            throw new BadRequestException("Game name cannot be empty.");
        }
        return updateT(gameData, "gameID", Integer.toString(gameData.gameID()));
    }

    @Override
    public void deleteAllGameDatas() throws ResponseException {
        deleteAllTs();
        nextGameId = 1;
    }
}
