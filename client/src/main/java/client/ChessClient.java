package client;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import gameservicerecords.*;
import model.GameData;
import server.ServerFacade;
import userservicerecords.*;

import java.util.Arrays;
import java.util.HashMap;

public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;
    ReplLogin replLogin = new ReplLogin(server);

    private String authToken = null;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;

    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);

            if(state == State.LOGGEDOUT) {
                return evalLoggedOutMenu(cmd, params);
            } else if (state == State.LOGGEDIN) {

                return processReplResponse(repl.evalLoggedInMenu(cmd, params));
            } else if (state == State.INPLAY) {
                return evalInPlayMenu(cmd, params);
            } else if (state == State.OBSERVATION) {
                return evalObserveMenu(cmd, params);
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

    private String evalInPlayMenu(String cmd, String... params) throws ResponseException{
        return switch (cmd) {
            case "move" -> makeMove(params);
            case "leave" -> leaveGame();
            case "quit" -> quitGame();
            default -> help();
        };
    }

    private String evalObserveMenu(String cmd, String... params) throws ResponseException{
        return switch (cmd) {
            case "leave" -> leaveGame();
            case "quit" -> "quit";
            default -> help();
        };
    }


    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            visitorName = String.join("-", params);
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResult result = server.register(request);
            state = State.LOGGEDIN;
            authToken = result.authToken();
            replLogin.setAuthToken(authToken);
            return String.format("You signed in as %s.", result.username());
        }
        throw new ResponseException(400, "Expected: register <username> <password> <email>");
    }

    private String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            LoginResult result = server.login(request);
            state = State.LOGGEDIN;
            authToken = result.authToken();
            replLogin.setAuthToken(authToken);
            return String.format("You signed in as %s.", result);
        }
        throw new ResponseException(400, "Expected: login <username> <password>");
    }





    public String makeMove(String... params) throws ResponseException {

        return "Not available.";
    }

    private String leaveGame() throws ResponseException {
        state = State.LOGGEDIN;
        return "User left game.";
    }



    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - register <username> <password> <email> - to create an account
                    - login <username> <password> - to play chess
                    - quit - playing chess
                    = help - with possible commands
                    """;
        } else if (state == State.LOGGEDIN) {

        } else if (state == State.INPLAY) {
            return """
                    - move <startposition> <endposition> - to make a chess move
                    - leave - a game
                    - quit - playing chess
                    - help - with possible commands
                    """;
        } else if (state == State.OBSERVATION) {
            return null;
        } else {
            assert false;
            return null;
        }

    }


}
