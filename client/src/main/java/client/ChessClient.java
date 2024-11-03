package client;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;

import server.ServerFacade;
import userservicerecords.*;

import java.util.Arrays;


public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;
    private ReplLogin replLogin;
    private ReplPlay replPlay;
    private ReplObserve replObserve;

    private String authToken = null;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        try {
            replLogin = new ReplLogin(this.server);
            replPlay = new ReplPlay(this.server);
            replObserve = new ReplObserve(this.server);
        } catch (ResponseException e) {

        }

    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);

            if(state == State.LOGGEDOUT) {
                return evalLoggedOutMenu(cmd, params);
            } else if (state == State.LOGGEDIN) {
                return processReplResponse(replLogin.evalLoggedInMenu(cmd, params));
            } else if (state == State.INPLAY) {
                ReplPlay replPlay = new ReplPlay(server, authToken);
                return processReplResponse(replPlay.evalInPlayMenu(cmd, params));
            } else if (state == State.OBSERVATION) {
                ReplObserve replObserve = new ReplObserve(server, authToken);
                return processReplResponse(replObserve.evalObserveMenu(cmd, params));
            } else {
                assert false;
                return null;
            }

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String processReplResponse(ReplResponse response) {
        state = response.state();
        return response.message();
    }

    private String evalLoggedOutMenu(String cmd, String... params) throws ResponseException{
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResult result = server.register(request);
            state = State.LOGGEDIN;
            replLogin.setAuthToken(result.authToken());
            authToken = result.authToken();
            return String.format("You signed in as %s.", result.username());
        }
        throw new ResponseException(400, "Expected: register <username> <password> <email>");
    }

    private String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            LoginResult result = server.login(request);
            state = State.LOGGEDIN;
            replLogin.setAuthToken(result.authToken());
            authToken = result.authToken();
            return String.format("You signed in as %s.", result);
        }
        throw new ResponseException(400, "Expected: login <username> <password>");
    }


    public String help() {
            return """
                    - register <username> <password> <email> - to create an account
                    - login <username> <password> - to play chess
                    - quit - playing chess
                    = help - with possible commands
                    """;
    }


}
