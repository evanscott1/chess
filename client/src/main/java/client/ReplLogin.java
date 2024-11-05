package client;

import exception.ResponseException;
import gameservicerecords.*;
import model.GameData;
import server.ServerFacade;
import ui.ChessBoardMaker;
import userservicerecords.LogoutRequest;
import userservicerecords.LogoutResult;

import java.util.ArrayList;
import java.util.HashMap;

public class ReplLogin {

    private ServerFacade server;
    private String authToken = null;
    private HashMap<Integer, Integer> gamesList = new HashMap<>();
    private int nextGameListID = 1;

    public ReplLogin(ServerFacade server) throws ResponseException {
        this.server = server;
    }

    public ReplResponse evalMenu(String cmd, String... params) throws Exception {
        return switch (cmd) {
            case "create" -> create(params);
            case "list" -> listGames();
            case "join" -> joinGame(params);
            case "observe" -> observeGame(params);
            case "logout" -> logout();
            case "quit" -> quitGame();
            default -> help();
        };
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private ReplResponse create(String... params) throws ResponseException {
        if (params.length == 1) {
            CreateGameRequest request = new CreateGameRequest(authToken, params[0]);
            CreateGameResult result = server.createGame(request);
            return new ReplResponse(State.LOGGEDIN, String.format("Created game. Name: %s.", params[0]));
        }
        throw new ResponseException(400, "Expected: create <NAME>");
    }

    private ReplResponse listGames() throws ResponseException {
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = server.listGames(request);
        gamesList.clear();
        nextGameListID = 1;
        StringBuilder buffer = new StringBuilder();
        for (GameData gameData : result.games()) {
            int listID = nextGameListID++;
            gamesList.put(listID, gameData.gameID());
            buffer.append(String.format("%s. %s\n", listID, gameData.gameName()));
        }
        return new ReplResponse(State.LOGGEDIN, buffer.toString());
    }

    public ReplResponse joinGame(String... params) throws Exception {
        if (params.length == 2) {
            int listID;
            try {
                listID = Integer.parseInt(params[0]);

                if (gamesList.containsKey(listID)) {

                    JoinGameRequest request = new JoinGameRequest(authToken, params[1].toUpperCase(), gamesList.get(listID));
                    JoinGameResult result = server.joinGame(request);
                    outputChessBoard(listID, params[1].toUpperCase());
                    return new ReplResponse(State.INPLAY, String.format("Joined game %s.", listID));
                }
                throw new ResponseException(400, "Not a valid ID. Please enter command 'list' and select a game ID.");
            } catch (ResponseException e) {
                ExceptionHandler.handleResponseException(e.statusCode());
            } catch (Exception e) {
                throw new Exception("Something went wrong1");
            }
        }
        throw new ResponseException(400, "Expected: join <ID> [WHITE|BLACK]");
    }

    public ReplResponse observeGame(String... params) throws ResponseException {
        if (params.length == 1) {
            int listID = Integer.parseInt(params[0]);
            if (gamesList.containsKey(listID)) {
                outputChessBoard(listID, "WHITE");
                return new ReplResponse(State.OBSERVATION, String.format("Joined game %s as an observer", listID));
            }
            throw new ResponseException(400, "Not a valid ID.");
        }
        throw new ResponseException(400, "Expected: join <ID> [WHITE|BLACK]");
    }

    private ReplResponse logout() throws ResponseException {
        LogoutRequest request = new LogoutRequest(authToken);
        LogoutResult result = server.logout(request);
        authToken = null;
        return new ReplResponse(State.LOGGEDOUT, "User logged out");
    }

    private ReplResponse quitGame() throws ResponseException {
        logout();
        return new ReplResponse(State.LOGGEDOUT, "quit");
    }

    private ReplResponse help() {
        return new ReplResponse(State.LOGGEDIN, """
                    - create <NAME> - a game
                    - list - games
                    - join <ID> [WHITE|BLACK]
                    - observe <ID> - a game
                    - logout - when you are done
                    - quit - playing chess
                    - help - with possible commands
                    """);
    }

    private void outputChessBoard(int listID, String teamColor) throws ResponseException{
        ListGamesResult gamesListResult = server.listGames(new ListGamesRequest(authToken));
        GameData gameData = new ArrayList<>(gamesListResult.games()).get(listID - 1);
        ChessBoardMaker.boardMaker(gameData, teamColor);
    }

}
