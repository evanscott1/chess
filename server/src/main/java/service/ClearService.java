package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import exception.ResponseException;
import service.clearservicerecords.ClearRequest;
import service.clearservicerecords.ClearResult;

public class ClearService {

    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;

    public ClearService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess, GameDataAccess gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }


    public ClearResult clear(ClearRequest clearRequest) throws ResponseException {
        authDataAccess.deleteAllAuthDatas();
        gameDataAccess.deleteAllGameDatas();
        userDataAccess.deleteAllUserDatas();
        ;

        return new ClearResult();
    }
}
