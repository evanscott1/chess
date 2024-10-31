package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public interface AuthDataAccess {
    AuthData addAuthData(AuthData authData) throws ResponseException;

    AuthData getAuthData(String authToken) throws ResponseException;



    Collection<AuthData> listAuthDatas() throws ResponseException;

    void deleteAuthData(String authToken) throws ResponseException;

    void deleteAllAuthDatas() throws ResponseException;
}
