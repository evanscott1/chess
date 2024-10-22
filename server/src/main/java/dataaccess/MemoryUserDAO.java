package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDataAccess {

    @Override
    public UserData addUserData(UserData userData) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllUserDatas() throws DataAccessException {

    }
}
