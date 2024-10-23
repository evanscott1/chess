package dataaccess;

import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface UserDataAccess {
    UserData addUserData(UserData userData) throws DataAccessException;

    UserData getUserData(String username) throws DataAccessException;

    Collection<UserData> listUserDatas() throws DataAccessException;

    void deleteAllUserDatas() throws DataAccessException;
}


