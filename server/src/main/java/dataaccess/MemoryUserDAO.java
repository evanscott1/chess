package dataaccess;

import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO extends MemoryBaseDAO<UserData> implements UserDataAccess  {

    final private HashMap<Integer, UserData> userDatas = new HashMap<>();

    @Override
    public UserData addUserData(UserData userData) throws DataAccessException {
        return addT(userData);
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        return getT("username", username);
    }

    @Override
    public Collection<UserData> listUserDatas() throws DataAccessException {
        return ts.values();
    }

    @Override
    public void deleteAllUserDatas() throws DataAccessException {
        deleteAllTs();
    }

}
