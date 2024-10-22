package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDataAccess {
    UserData addUserData(UserData userData) throws DataAccessException;

    UserData getUserData(String username) throws DataAccessException;

    void deleteAllUserDatas() throws DataAccessException;
}


