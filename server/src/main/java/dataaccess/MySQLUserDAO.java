package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.sql.SQLException;
import java.util.Collection;

public class MySQLUserDAO implements UserDataAccess {

    public MySQLUserDAO() throws ResponseException {
        configureDatabase();
    }

    @Override
    public UserData addUserData(UserData userData) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<UserData> listUserDatas() throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllUserDatas() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(256) NOT NULL,
                `password` char(60) NOT NULL
                `json` TEXT DEFAULT NULL,
                PRIMARY KEY (`id`),
                INDEX(username),
                INDEX(password)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900
            """
    };

    private void configureDatabase() throws ResponseException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
