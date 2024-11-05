package dataaccess;

import exception.ResponseException;
import model.UserData;
import exception.BadRequestException;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO extends MemoryBaseDAO<UserData> implements UserDataAccess {

    final private HashMap<Integer, UserData> userDatas = new HashMap<>();

    @Override
    public UserData addUserData(UserData userData) throws ResponseException {
        if (userData.username().isEmpty() || userData.password().isEmpty() || userData.email().isEmpty()) {
            throw new BadRequestException("UserData fields cannot be empty");
        }
        return addT(userData);
    }

    @Override
    public UserData getUserData(String username) throws ResponseException {
        if (username.isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }
        return getT("username", username);
    }

    @Override
    public Collection<UserData> listUserDatas() throws ResponseException {
        return ts.values();
    }

    @Override
    public void deleteAllUserDatas() throws ResponseException {
        deleteAllTs();
    }

}
