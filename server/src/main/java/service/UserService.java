package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import service.UserServiceRecords.*;

import java.util.UUID;


public class UserService {

    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public UserService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException, DataAccessException, BadRequestException, UnauthorizedException, ForbiddenException {
        UserData u = userDataAccess.getUserData(registerRequest.username());

        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new BadRequestException("Register request has empty required fields");
        }

        if(u != null) {
            throw new ForbiddenException("Username already exists");
        }
            u = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            UserData newU = userDataAccess.addUserData(u);
            LoginRequest loginRequest = new LoginRequest(newU.username(), newU.password());
            LoginResult loginResult = login(loginRequest);

            return new RegisterResult(loginResult.username(), loginResult.authToken());
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException, ResponseException {
        UserData u = userDataAccess.getUserData(loginRequest.username());
        AuthData authData = authDataAccess.getAuthDataByUsername(loginRequest.username());
        if (u == null) {
            throw new UnauthorizedException("User not found");
        }
        //TODO: Put user already logged in back since the "Normal User Login" test says normal is letting a user create multiple authTokens
//        if(authData != null) {
//            throw new UnauthorizedException("User already logged in");
//        }
        if(!u.password().equals(loginRequest.password())) {
            throw new UnauthorizedException("User password does not match");
        }

        authData = authDataAccess.addAuthData(new AuthData(UUID.randomUUID().toString(), loginRequest.username()));
        System.out.println(authData.authToken() + " " + authData.username());
        return new LoginResult(authData.username(), authData.authToken());

    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException, ResponseException{
        AuthData authData = authDataAccess.getAuthData(logoutRequest.authToken());

        if(authData == null) {
            throw new UnauthorizedException("User not logged in");
        }

        authDataAccess.deleteAuthData(logoutRequest.authToken());
        return new LogoutResult();
    }
}
