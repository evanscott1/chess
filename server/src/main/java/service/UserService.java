package service;

import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import exception.BadRequestException;
import exception.ForbiddenException;
import exception.ResponseException;
import exception.UnauthorizedException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import userservicerecords.*;

import java.util.UUID;


public class UserService {

    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public UserService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {



        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new BadRequestException("Register request has empty required fields");
        }

        UserData u = userDataAccess.getUserData(registerRequest.username());


        if (u != null) {
            throw new ForbiddenException("Username already exists");
        }

        String hashedPassword = hashStringBCrypt(registerRequest.password());
        u = new UserData(registerRequest.username(), hashedPassword, registerRequest.email());
        UserData newU = userDataAccess.addUserData(u);
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        LoginResult loginResult = login(loginRequest);

        return new RegisterResult(loginResult.username(), loginResult.authToken());
    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        UserData u = userDataAccess.getUserData(loginRequest.username());

        if (u == null) {
            throw new UnauthorizedException("User not found");
        }

        if (!verifyUser(loginRequest.password(), u.password())) {
            throw new UnauthorizedException("User password does not match");
        }

        AuthData authData = authDataAccess.addAuthData(new AuthData(UUID.randomUUID().toString(), loginRequest.username()));
        return new LoginResult(authData.username(), authData.authToken());

    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws ResponseException {
        AuthData authData = authDataAccess.getAuthData(logoutRequest.authToken());

        if (authData == null) {
            throw new UnauthorizedException("User not logged in");
        }

        authDataAccess.deleteAuthData(logoutRequest.authToken());
        return new LogoutResult();
    }

    private String hashStringBCrypt(String value) {
        return BCrypt.hashpw(value, BCrypt.gensalt());
    }

    boolean verifyUser(String providedClearTextPassword, String hashedPassword) {
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }
}
