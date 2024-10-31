package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.util.Collection;

public interface UserDataAccess {
    UserData addUserData(UserData userData) throws ResponseException;

    UserData getUserData(String username) throws ResponseException;

    Collection<UserData> listUserDatas() throws ResponseException;

    void deleteAllUserDatas() throws ResponseException;
}


