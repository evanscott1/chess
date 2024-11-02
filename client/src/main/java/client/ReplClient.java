package client;

import exception.ResponseException;
import userservicerecords.LoginRequest;
import userservicerecords.LoginResult;
import userservicerecords.RegisterRequest;
import userservicerecords.RegisterResult;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ReplClient {

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResult result = server.register(request);
            state = State.LOGGEDIN;
            authToken = result.authToken();
            return String.format("You signed in as %s.", result.username());
        }
        throw new ResponseException(400, "Expected: register <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            LoginResult result = server.login(request);
            state = State.LOGGEDIN;
            authToken = result.authToken();
            return String.format("You signed in as %s.", result);
        }
        throw new ResponseException(400, "Expected: login <username> <password>");
    }
}
