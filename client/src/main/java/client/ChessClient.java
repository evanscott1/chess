package client;

import exception.BadRequestException;
import exception.ForbiddenException;
import exception.ResponseException;
import server.ServerFacade;
import userservicerecords.LoginRequest;
import userservicerecords.LoginResult;
import userservicerecords.RegisterRequest;
import userservicerecords.RegisterResult;

import java.util.Arrays;


public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;
    private final ReplLogin replLogin;
    private String authToken = null;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        try {
            replLogin = new ReplLogin(this.server, serverUrl);
        } catch (ResponseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);

            if (state == State.LOGGEDOUT) {
                return evalMenu(cmd, params);
            } else if (state == State.LOGGEDIN) {
                return processReplResponse(replLogin.eval(cmd, params));
            } else {
                assert false;
                throw new RuntimeException("There was a problem with chess.");
            }

        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String processReplResponse(ReplResponse response) {
        state = response.state();
        return response.message();
    }

    private String evalMenu(String cmd, String... params) throws Exception {
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String register(String... params) throws Exception {
        if (params.length == 3) {
            try {
                RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
                RegisterResult result = server.register(request);
                state = State.LOGGEDIN;
                replLogin.setAuthToken(result.authToken());
                replLogin.setUsername(params[0]);
                authToken = result.authToken();
                return String.format("You signed in as %s.", result.username());
            } catch (ResponseException e) {
                if (e.statusCode() == 403) {
                    throw new ForbiddenException("Username already taken.");
                }
                ExceptionHandler.handleResponseException(e.statusCode());
            } catch (Exception e) {
                throw new Exception("There was an error while trying to register.");
            }


        }
        throw new BadRequestException("Expected: register <username> <password> <email>");
    }

    private String login(String... params) throws Exception {
        if (params.length == 2) {
            try {
                LoginRequest request = new LoginRequest(params[0], params[1]);
                LoginResult result = server.login(request);
                state = State.LOGGEDIN;
                replLogin.setAuthToken(result.authToken());
                this.authToken = result.authToken();
                return String.format("You signed in as %s.", result.username());
            } catch (ResponseException e) {
                if (e.statusCode() == 401) {
                    throw new ForbiddenException("Please check that your username and password are entered correctly.");
                }
                ExceptionHandler.handleResponseException(e.statusCode());
            } catch (Exception e) {
                throw new Exception("There was an error while trying to login.");
            }


        }
        throw new BadRequestException("Expected: login <username> <password>");
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
