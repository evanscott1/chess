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

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, BadRequestException, UnauthorizedException, ForbiddenException {
        UserData u = userDataAccess.getUserData(registerRequest.username());

        if (registerRequest.username().isEmpty() || registerRequest.password().isEmpty() || registerRequest.email().isEmpty()) {
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

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException, UnauthorizedException {
        UserData u = userDataAccess.getUserData(loginRequest.username());
        AuthData authData = authDataAccess.getAuthDataByUsername(loginRequest.username());
        if (u == null) {
            throw new UnauthorizedException("User not found");
        }
        if(authData != null) {
            throw new UnauthorizedException("User already logged in");
        }
        if(!u.password().equals(loginRequest.password())) {
            throw new UnauthorizedException("User password does not match");
        }

        authData = authDataAccess.addAuthData(new AuthData(UUID.randomUUID().toString(), loginRequest.username()));
        return new LoginResult(authData.username(), authData.authToken());

    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException, UnauthorizedException{
        AuthData authData = authDataAccess.getAuthData(logoutRequest.authToken());

        if(authData == null) {
            throw new UnauthorizedException("User not logged in");
        }

        authDataAccess.deleteAuthData(logoutRequest.authToken());
        return new LogoutResult();
    }
}