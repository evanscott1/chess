package dataaccess;


import model.AuthData;
import model.UserData;

import java.util.Collection;


public class MySQLAuthDAO extends MySQLBaseDAO<AuthData> implements AuthDataAccess {


    @Override
    public AuthData addAuthData(AuthData authData) throws DataAccessException {
        addT(authData);
        return authData;
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        return getT("authToken", authToken, AuthData.class);
    }

    @Override
    public Collection<AuthData> listAuthDatas() throws DataAccessException {
        return listTs(AuthData.class);
    }

    @Override
    public void deleteAuthData(String authToken) throws DataAccessException {
        deleteT("authToken", authToken);
    };

    @Override
    public void deleteAllAuthDatas() throws DataAccessException {
        deleteAllTs();
    }

    @Override
    protected String getTableName() {
        return "auth";
    }


    @Override
    protected String[] getCreateStatements() {
        return new String[] {
                """
            CREATE TABLE IF NOT EXISTS  auth (
                `authToken` char(36) NOT NULL,
                `username` varchar(256) NOT NULL,
                `json` TEXT DEFAULT NULL,
                PRIMARY KEY (`authToken`),
                INDEX(username),
                INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs
            """
        };
    }

}
