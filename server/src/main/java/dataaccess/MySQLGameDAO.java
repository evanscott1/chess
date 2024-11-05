package dataaccess;


import exception.ResponseException;
import model.GameData;
import exception.BadRequestException;

import java.util.Collection;


public class MySQLGameDAO extends MySQLBaseDAO<GameData> implements GameDataAccess {

    @Override
    public GameData addGameData(GameData gameData) throws ResponseException {
        if (gameData.gameName().isEmpty()) {
            throw new BadRequestException("Game name cannot be empty.");
        }
        int gameID = addT(gameData);
        GameData newGameData = gameData.setGameId(gameID);
        return updateGameData(newGameData);
    }

    @Override
    public GameData getGameData(int gameID) throws ResponseException {
        if (gameID < 1) {
            throw new BadRequestException("GameID cannot be less than 1.");
        }
        return getT("gameID", Integer.toString(gameID), GameData.class);
    }

    @Override
    public GameData updateGameData(GameData gameData) throws ResponseException {
        if (gameData.gameName().isEmpty()) {
            throw new BadRequestException("Game name cannot be empty.");
        }

        updateT(gameData, "gameID", gameData.gameID());
        return gameData;
    }

    @Override
    public Collection<GameData> listGameDatas() throws ResponseException {
        return listTs(GameData.class);
    }

    @Override
    public void deleteAllGameDatas() throws ResponseException {
        deleteAllTs();
    }

    @Override
    protected String getTableName() {
        return "game";
    }


    @Override
    protected String[] getCreateStatements() {
        return new String[]{
                """
            CREATE TABLE IF NOT EXISTS  game (
                `gameID` int NOT NULL AUTO_INCREMENT,
                `whiteUsername` varchar(256),
                `blackUsername` varchar(256),
                `gameName` varchar(256) NOT NULL,
                `json` TEXT DEFAULT NULL,
                PRIMARY KEY (`gameID`),
                INDEX(gameID),
                INDEX(whiteUsername),
                INDEX(blackUsername),
                INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs
            """
        };
    }

}
