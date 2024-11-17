package client;

import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.BadRequestException;
import exception.ForbiddenException;
import exception.ResponseException;
import gameservicerecords.*;
import model.GameData;
import server.ServerFacade;
import ui.ChessBoardMaker;
import userservicerecords.LogoutRequest;
import userservicerecords.LogoutResult;
import userservicerecords.RegisterRequest;
import userservicerecords.RegisterResult;
import websocket.commands.ConnectCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ReplLogin extends ReplBase {

    private State state = State.LOGGEDIN;
    private ReplPlay replPlay;
    private ReplObserve replObserve;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private String serverURL;


    public ReplLogin(ServerFacade server, String serverURL, NotificationHandler notificationHandler) throws ResponseException {
        super(server);
        this.serverURL = serverURL;
        this.notificationHandler = notificationHandler;
        try {
            replPlay = new ReplPlay(this.server);
            replObserve = new ReplObserve(this.server);
        } catch (ResponseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ReplResponse eval(String cmd, String... params) throws  Exception {
            if (state == State.LOGGEDIN) {
                return evalMenu(cmd, params);
            } else if (state == State.INPLAY) {
                return updateLoginState(replPlay.evalMenu(cmd, params));
            } else if (state == State.OBSERVATION) {
                return updateLoginState(replObserve.evalMenu(cmd, params));
            } else {
                assert false;
                throw new RuntimeException("There was a problem with chess.");
            }
    }


    public ReplResponse evalMenu(String cmd, String... params) throws Exception {
        return switch (cmd) {
            case "create" -> updateLoginState(create(params));
            case "list" -> updateLoginState(listGames());
            case "join" -> updateLoginState(joinGame(params));
            case "observe" -> updateLoginState(observeGame(params));
            case "logout" -> logout();
            case "quit" -> quitGame();
            default -> help();
        };
    }

    private ReplResponse updateLoginState(ReplResponse res) {
        state = res.state();
        return new ReplResponse(State.LOGGEDIN, res.message());
    }


    private ReplResponse create(String... params) throws Exception {
        if (params.length == 1) {
            try {
                CreateGameRequest request = new CreateGameRequest(authToken, params[0]);
                CreateGameResult result = server.createGame(request);
                return new ReplResponse(State.LOGGEDIN, String.format("Created game. Name: %s.", params[0]));
            } catch (ResponseException e) {
                ExceptionHandler.handleResponseException(e.statusCode());
            } catch (Exception e) {
                throw new Exception("There was an error while trying to create game.");
            }

        }
        throw new BadRequestException("Expected: create <NAME>");
    }

    private ReplResponse listGames() throws Exception {
        try {
            ListGamesRequest request = new ListGamesRequest(authToken);
            ListGamesResult result = server.listGames(request);
            gamesList.clear();
            nextGameListID = 1;
            StringBuilder buffer = new StringBuilder();
            for (GameData gameData : result.games()) {
                int listID = nextGameListID++;
                String whiteU = "";
                String blackU = "";
                gamesList.put(listID, gameData.gameID());

                if (gameData.whiteUsername() != null) {
                    whiteU = gameData.whiteUsername();
                }
                if(gameData.blackUsername() != null) {
                    blackU = gameData.blackUsername();
                }
                buffer.append(String.format("%s. %s: WhiteUsername: %s, BlackUsername: %s\n",
                        listID, gameData.gameName(), whiteU, blackU));
            }
            return new ReplResponse(State.LOGGEDIN, buffer.toString());
        } catch (ResponseException e) {
            if (e.statusCode() == 403) {
                throw new ForbiddenException("Username already taken.");
            }
            ExceptionHandler.handleResponseException(e.statusCode());
        } catch (Exception e) {
            throw new Exception("There was an error while trying to list games.");
        }
        throw new RuntimeException("Something went wrong. You may need to restart Chess.");
    }

    public ReplResponse joinGame(String... params) throws Exception {
        if (params.length == 2) {
            int listID;
            try {
                listID = Integer.parseInt(params[0]);

                if (gamesList.containsKey(listID)) {
                    String teamColor = params[1].toUpperCase();
                    JoinGameRequest request = new JoinGameRequest(authToken, teamColor, gamesList.get(listID));
                    JoinGameResult result = server.joinGame(request);
                    replPlay.setListID(listID);
                    replPlay.setTeamColor(teamColor);
                    outputChessBoard(listID, teamColor);
                    return new ReplResponse(State.INPLAY, String.format("Joined game %s.", listID));
                }
                throw new BadRequestException("Not a valid ID. Please enter command 'list' and select a game ID.");
            } catch (ResponseException e) {
                ExceptionHandler.handleResponseException(e.statusCode());
            } catch (Exception e) {
                throw new Exception("There was an error while trying to join game.");
            }
        }
        throw new BadRequestException("Expected: join <ID> [WHITE|BLACK]");
    }

    public ReplResponse observeGame(String... params) throws Exception {
        if (params.length == 1) {
            try {
                int listID = Integer.parseInt(params[0]);
                if (gamesList.containsKey(listID)) {
                    replObserve.setListID(listID);
                    replObserve.setUsername(username);
                    outputChessBoard(listID, "WHITE");

                    ConnectCommand connectCommand = new ConnectCommand(authToken, listID, ConnectCommand.JoinType.OBSERVER);
                    connectCommand.setUsername(username);
                    ws = new WebSocketFacade(serverURL, notificationHandler);
                    ws.connectGame(connectCommand);
                    return new ReplResponse(State.OBSERVATION, String.format("Joined game %s as an observer", listID));

                }
                throw new BadRequestException("Not a valid ID. Please enter command 'list' and select a game ID.");
            } catch (ResponseException e) {
                ExceptionHandler.handleResponseException(e.statusCode());
            } catch (Exception e) {
                throw new Exception("There was an error while trying to observe game.");
            }

        }
        throw new ResponseException(400, "Expected: join <ID> [WHITE|BLACK]");
    }

    @Override
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        replPlay.setAuthToken(authToken);
        replObserve.setAuthToken(authToken);
    }



    private ReplResponse help() {
        return new ReplResponse(State.LOGGEDIN, """
                    - create <NAME> - a game
                    - list - games
                    - join <ID> [WHITE|BLACK] - must enter 'list' to get game ID
                    - observe <ID> - a game
                    - logout - when you are done
                    - quit - playing chess
                    - help - with possible commands
                    """);
    }


}
