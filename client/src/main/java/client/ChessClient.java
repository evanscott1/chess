package client;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import gameservicerecords.CreateGameRequest;
import gameservicerecords.CreateGameResult;
import gameservicerecords.ListGamesRequest;
import gameservicerecords.ListGamesResult;
import model.GameData;
import server.ServerFacade;
import userservicerecords.LoginRequest;
import userservicerecords.LoginResult;
import userservicerecords.RegisterRequest;
import userservicerecords.RegisterResult;

import java.util.Arrays;
import java.util.HashMap;

public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;
    private HashMap<Integer, String> gamesList = new HashMap<>();
    private int nextGameListID = 1;
    private String authToken;

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
                return evalLoggedInMenu(cmd, params);
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

    private String evalLoggedOutMenu(String cmd, String... params) throws ResponseException{
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String evalLoggedInMenu(String cmd, String... params) throws ResponseException{
        return switch (cmd) {
            case "create" -> create(params);
            case "list" -> listGames(params);
            case "join" -> listPets();
            case "observe" -> ;
            case "logout" -> ;
            case "quit" -> ;
            default -> help();
        };
    }

    private String evalInPlayMenu(String cmd, String... params) throws ResponseException{
        return switch (cmd) {
            case "move" -> register(params);
            case "leave" -> login(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String evalObserveMenu(String cmd, String... params) throws ResponseException{
        return switch (cmd) {
            case "move" -> register(params);
            case "leave" -> login(params);
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
            return String.format("You signed in as %s.", result);
        }
        throw new ResponseException(400, "Expected: login <username> <password>");
    }

    private String listGames() throws ResponseException {
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = server.listGames(request);
        gamesList.clear();
        nextGameListID = 1;
        StringBuilder gamesListOutput = new StringBuilder();
        for (GameData gameData : result.games()) {
            int id = nextGameListID++;
            String name = gameData.gameName();
            gamesList.put(id, name);
            gamesListOutput.append(String.format("%s. %s\n", id, name));
        }

        return gamesListOutput.toString();
    }

    private String create(String... params) throws ResponseException {
        if (params.length == 1) {
            CreateGameRequest request = new CreateGameRequest(authToken, params[0]);
            CreateGameResult result = server.createGame(request);
            return String.format("Created game. ID: %s.", result.gameID());
        }
        throw new ResponseException(400, "Expected: create <NAME>");
    }

    public String adoptAllPets() throws ResponseException {
        var buffer = new StringBuilder();
        for (var pet : server.listPets()) {
            buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
        }

        server.deleteAllPets();
        return buffer.toString();
    }

    public String signOut() throws ResponseException {
        ws.leavePetShop(visitorName);
        ws = null;
        state = State.SIGNEDOUT;
        return String.format("%s left the shop", visitorName);
    }

    private Pet getPet(int id) throws ResponseException {
        for (var pet : server.listPets()) {
            if (pet.id() == id) {
                return pet;
            }
        }
        return null;
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
            return """
                    - create <NAME> - a game
                    - list - games
                    - join <ID> [WHITE|BLACK]
                    - observer <ID> - a game
                    - logout - when you are done
                    - quit - playing chess
                    - help - with possible commands
                    """;
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
