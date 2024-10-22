package dataaccess;

import model.AuthData;

public class MemoryAuthDAO extends MemoryBaseDAO<AuthData> implements AuthDataAccess{
    @Override
    public AuthData addAuthData(AuthData authData) throws DataAccessException {
        return addT(authData);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        return getT("authToken", authToken);
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
