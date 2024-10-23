package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import service.clearServiceRecords.ClearRequest;
import service.clearServiceRecords.ClearResult;

public class ClearService {

    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;

    public ClearService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess, GameDataAccess gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }


    public ClearResult clear(ClearRequest clearRequest) throws DataAccessException {
        authDataAccess.deleteAllAuthDatas();
        gameDataAccess.deleteAllGameDatas();
        userDataAccess.deleteAllUserDatas();
        ;

        return new ClearResult();
    }
}
