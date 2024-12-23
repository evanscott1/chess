package dataaccess;


import exception.ResponseException;
import model.UserData;
import exception.BadRequestException;

import java.util.Collection;


public class MySQLUserDAO extends MySQLBaseDAO<UserData> implements UserDataAccess {

    @Override
    public UserData addUserData(UserData userData) throws ResponseException {
        if (userData.username().isEmpty() || userData.password().isEmpty() || userData.email().isEmpty()) {
            throw new BadRequestException("UserData fields cannot be empty");
        }
        addT(userData);
        return userData;
    }

    @Override
    public UserData getUserData(String username) throws ResponseException {
        if (username.isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }
        return getT("username", username, UserData.class);
    }

    @Override
    public Collection<UserData> listUserDatas() throws ResponseException {
        return listTs(UserData.class);
    }

    @Override
    public void deleteAllUserDatas() throws ResponseException {
        deleteAllTs();
    }

    @Override
    protected String getTableName() {
        return "user";
    }


    @Override
    protected String[] getCreateStatements() {
        return new String[]{
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
