package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import service.UserServiceRecords.*;

import java.util.UUID;


public class UserService {

    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    UserService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        boolean isUser = userDataAccess.getUserData(registerRequest.username()) == null;

        if(!isUser) {
            UserData u = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            UserData newU = userDataAccess.addUserData(u);
            LoginRequest loginRequest = new LoginRequest(newU.username(), newU.password());
            LoginResult loginResult = login(loginRequest);
            return new RegisterResult(loginResult.username(), loginResult.authToken());
        }

        return null;
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData u = userDataAccess.getUserData(loginRequest.username());
        if (u.password().equals(loginRequest.password())) {
            AuthData authData = authDataAccess.addAuthData(new AuthData(UUID.randomUUID().toString(), loginRequest.username()));
            return new LoginResult(authData.username(), authData.authToken());
        }
        return null;
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException{
        authDataAccess.deleteAuthData(logoutRequest.authToken());
        return new LogoutResult();
    }
}
