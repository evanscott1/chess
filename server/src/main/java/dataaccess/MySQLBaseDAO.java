package dataaccess;

import exception.ResponseException;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class MySQLBaseDAO {
    protected final String[] createStatements = {};
    public MySQLBaseDAO() throws ResponseException {
        configureDatabase();
    }

    private String hashUserPassword(String username, String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


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
