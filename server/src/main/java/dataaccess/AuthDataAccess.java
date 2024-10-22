package dataaccess;

public interface AuthDataAccess {
    AuthDataAccess addAuthData(AuthDataAccess authData) throws DataAccessException;

    AuthDataAccess getAuthData(String authToken) throws DataAccessException;

    void deleteAuthData(String authToken) throws DataAccessException;

    void deleteAllAuthDatas() throws DataAccessException;
}
