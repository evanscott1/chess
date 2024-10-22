package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
    AuthData addAuthData(AuthData authData) throws DataAccessException;

    AuthData getAuthData(String authToken) throws DataAccessException;

    void deleteAuthData(String authToken) throws DataAccessException;

    void deleteAllAuthDatas() throws DataAccessException;
}
