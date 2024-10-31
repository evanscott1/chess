package dataaccess;


import exception.ResponseException;
import model.AuthData;
import service.BadRequestException;

import java.util.Collection;


public class MySQLAuthDAO extends MySQLBaseDAO<AuthData> implements AuthDataAccess {


    @Override
    public AuthData addAuthData(AuthData authData) throws ResponseException {
        if (authData.authToken() == null || authData.username() == null) {
            throw new BadRequestException("DataAuth fields cannot be null");
        }
        addT(authData);
        return authData;
    }

    @Override
    public AuthData getAuthData(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new BadRequestException("Auth token cannot be empty or null");
        }

        return getT("authToken", authToken, AuthData.class);
    }

    @Override
    public Collection<AuthData> listAuthDatas() throws ResponseException {
        return listTs(AuthData.class);
    }

    @Override
    public void deleteAuthData(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new BadRequestException("Auth token cannot be empty or null");
        }

        deleteT("authToken", authToken);
    }

    ;

    @Override
    public void deleteAllAuthDatas() throws ResponseException {
        deleteAllTs();
    }

    @Override
    protected String getTableName() {
        return "auth";
    }


    @Override
    protected String[] getCreateStatements() {
        return new String[]{
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
