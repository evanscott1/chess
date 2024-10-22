package dataaccess;

public class MemoryAuthDAO implements AuthDataAccess{
    @Override
    public AuthDataAccess addAuthData(AuthDataAccess authData) throws DataAccessException {
        return null;
    }

    @Override
    public AuthDataAccess getAuthData(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuthData(String authToken) throws DataAccessException {

    }

    @Override
    public void deleteAllAuthDatas() throws DataAccessException {

    }
}
