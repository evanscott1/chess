package dataaccess;


import model.GameData;
import model.UserData;

import java.util.Collection;


public class MySQLGameDAO extends MySQLBaseDAO implements GameDataAccess {

    @Override
    public GameData addGameData(GameData gameData) throws DataAccessException {
        int gameID = addT(gameData);
        gameData.setGameId(gameID);
        return gameData;
    }

    @Override
    public GameData getGameData(int gameID) throws DataAccessException {
        return getT("gameID", Integer.toString(gameID), GameData.class);
    }

    @Override
    public GameData updateGameData(GameData gameData) throws DataAccessException {
        return getT("gameID", Integer.toString(gameID), GameData.class);
    }

    @Override
    public Collection<GameData> listGameDatas() throws DataAccessException {
        return listTs(GameData.class);
    }

    @Override
    public void deleteAllGameDatas() throws DataAccessException {
        deleteAllTs();
    }

    @Override
    protected String getTableName() {
        return "game";
    }


    @Override
    protected String[] getCreateStatements() {
        return new String[] {
                """
            CREATE TABLE IF NOT EXISTS  game (
                `gameID` int NOT NULL AUTO_INCREMENT,
                `whiteUsername` varchar(256) NOT NULL,
                `blackUsername` varchar(256) NOT NULL,
                `gameName` varchar(256) NOT NULL,
                `game` TEXT DEFAULT NULL,
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
