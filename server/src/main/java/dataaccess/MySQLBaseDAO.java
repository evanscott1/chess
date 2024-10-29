package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLBaseDAO {
    private String[] createStatements = {};
    private String table = "";

    public MySQLBaseDAO() {
        try {
            this.createStatements = getCreateStatements();
            this.table = getTableName();
            configureDatabase();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> int addT(T t) throws DataAccessException {
        Field[] fields = t.getClass().getDeclaredFields();

        StringBuilder columnNames = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        Object[] params = new Object[fields.length + 1];
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);

                columnNames.append(fields[i].getName());
                placeholders.append("?");

                if (i < fields.length - 1) {
                    columnNames.append(", ");
                    placeholders.append(", ");
                }

                params[i] = fields[i].get(t);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        columnNames.append(", json");
        placeholders.append(", ?");
        var statement = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columnNames, placeholders);

        params[fields.length] = new Gson().toJson(t);

        return executeUpdate(statement, params);
    }

    public <T> T getT(String where, String value, Class<T> objectClass) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = String.format("SELECT json FROM %s WHERE %s=?", table, where);
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, value);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readJsonResultToObject(rs, objectClass);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data from %s: %s", table, e.getMessage()));
        }
        return null;
    }


    public T updateT(T t, String attributeValue, String value) {
        Integer key = findHashMapKeyByAttribute(ts, attributeValue, value);
        ts.replace(key, t);
        return t;
    }

    public <T> Collection<T> listTs(Class<T> objectClass) throws DataAccessException {
        var result = new ArrayList<T>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = String.format("SELECT json FROM %s", table);
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readJsonResultToObject(rs, objectClass));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data from %s: %s", table, e.getMessage()));
        }
        return result;
    }

    public void deleteT(String where, String value) throws DataAccessException {
        var statement = String.format("DELETE FROM %s WHERE %s=?", table, value);
        executeUpdate(statement, value);
    }

    public <T> void deleteAllTs() throws DataAccessException {
        var statement = String.format("TRUNCATE %s", table);
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    protected  <T> T readJsonResultToObject(ResultSet rs, Class<T> objectClass) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, objectClass);
    }

    protected String hashStringBCrypt(String value) {
        return BCrypt.hashpw(value, BCrypt.gensalt());
    }

    protected String[] getCreateStatements() {
        return new String[] {};
    }

    protected String getTableName() {
        return "";
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
