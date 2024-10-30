package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public abstract class MySQLBaseDAO<T> {
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

    public int addT(T t) throws DataAccessException {
        List<Field> nonObjectFields = Arrays.stream(t.getClass().getDeclaredFields()).filter(f -> isSimpleType(f.getType())).toList();

        StringBuilder columnNames = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        List<Object> params = new ArrayList<>();

        Iterator<Field> iterator = nonObjectFields.iterator();
        try {
            while (iterator.hasNext()) {
                Field field = iterator.next();
                field.setAccessible(true);
                columnNames.append(field.getName());
                placeholders.append("?");

                if ( iterator.hasNext()) {
                    columnNames.append(", ");
                    placeholders.append(", ");
                }

                params.add(field.get(t));
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        columnNames.append(", json");
        placeholders.append(", ?");
        var statement = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columnNames, placeholders);

        params.add(new Gson().toJson(t));

        return executeUpdate(statement, params.toArray());
    }

    private boolean isSimpleType(Class<?> type) {
        return type.isPrimitive() ||
                type.equals(String.class) ||
                type.equals(Integer.class) ||
                type.equals(Long.class) ||
                type.equals(Double.class) ||
                type.equals(Float.class) ||
                type.equals(Boolean.class) ||
                type.equals(Character.class);
    }

    public T getT(String where, String value, Class<T> objectClass) throws DataAccessException {
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


    public int updateT(T t, String where, Object value) throws DataAccessException {
        Field[] fields = t.getClass().getDeclaredFields();

        StringBuilder columnNames = new StringBuilder();

        List<Object> params = new ArrayList<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);

                if (isSimpleType(field.getType())) {
                    if (!params.isEmpty()) {columnNames.append(", ");}

                    columnNames.append(field.getName()).append(" = ?");
                    params.add(field.get(t));
                }
            }
            if (!columnNames.isEmpty()) {columnNames.append(", ");}
            columnNames.append("json = ?");
            params.add(new Gson().toJson(t));

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing field values: " + e.getMessage(), e);
        }


        var statement = String.format("UPDATE %s SET %s WHERE %s=?", table, columnNames, where);

        params.add(value);


        return executeUpdate(statement, params.toArray());

    }

    public Collection<T> listTs(Class<T> objectClass) throws DataAccessException {
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

    public void deleteAllTs() throws DataAccessException {
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

    protected  T readJsonResultToObject(ResultSet rs, Class<T> objectClass) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, objectClass);
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
