package dataaccess;

import model.UserData;
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
    public void deleteAllUserDatas() throws DataAccessException {
        deleteAllTs();
    }

}
