package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.Collection;

public class MemoryAuthDAO extends MemoryBaseDAO<AuthData> implements AuthDataAccess {
    @Override
    public AuthData addAuthData(AuthData authData) throws DataAccessException {
        return addT(authData);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        return getT("authToken", authToken);
    }

    //I admit I was wrong. This method probably shouldn't exist.
    public AuthData getAuthDataByUsername(String username) throws DataAccessException {
        return getT("username", username);
    }

    @Override
    public Collection<AuthData> listAuthDatas() throws DataAccessException {
        return ts.values();
    }

    @Override
    public void deleteAuthData(String authToken) throws DataAccessException {
        deleteT("authToken", authToken);
    }

    @Override
    public void deleteAllAuthDatas() throws DataAccessException {
        deleteAllTs();
    }
}
