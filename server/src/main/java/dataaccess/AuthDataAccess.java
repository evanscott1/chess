package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.Collection;

public interface AuthDataAccess {
    AuthData addAuthData(AuthData authData) throws DataAccessException;

    AuthData getAuthData(String authToken) throws DataAccessException;



    Collection<AuthData> listAuthDatas() throws DataAccessException;

    void deleteAuthData(String authToken) throws DataAccessException;

    void deleteAllAuthDatas() throws DataAccessException;
}
