package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLUserDAO extends MySQLBaseDAO implements UserDataAccess {

    private final String table = "user";

    @Override
    public UserData addUserData(UserData userData) throws DataAccessException {
        String hashedPassword = hashStringBCrypt(userData.password());
        UserData hashedUser = new UserData(userData.username(), hashedPassword, userData.email());
        addT(table, hashedUser);
        return userData;
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        return getT(table, "username", username, UserData.class);
    }

    @Override
    public Collection<UserData> listUserDatas() throws DataAccessException {
        return listTs(table, UserData.class);
    }

    @Override
    public void deleteAllUserDatas() throws DataAccessException {
        deleteAllTs(table);
    }


    @Override
    protected String[] getCreateStatements() {
        return new String[] {
                """
            CREATE TABLE IF NOT EXISTS  user (
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(256) NOT NULL,
                `password` char(60) NOT NULL,
                `email` varchar(256) NOT NULL,
                `json` TEXT DEFAULT NULL,
                PRIMARY KEY (`id`),
                INDEX(username),
                INDEX(password)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs
            """
        };
    }

}
