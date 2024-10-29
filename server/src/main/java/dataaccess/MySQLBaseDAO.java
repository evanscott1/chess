package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLBaseDAO {
    protected final String[] createStatements = {};
    public MySQLBaseDAO() throws ResponseException {
        configureDatabase();
    }

    private <T> T readJsonResultToObject(ResultSet rs, Class<T> objectClass) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, objectClass);
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
