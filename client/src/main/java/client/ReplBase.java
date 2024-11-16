package client;

import exception.ForbiddenException;
import exception.ResponseException;
import gameservicerecords.ListGamesRequest;
import gameservicerecords.ListGamesResult;
import model.GameData;
import server.ServerFacade;
import ui.ChessBoardMaker;
import userservicerecords.LogoutRequest;
import userservicerecords.LogoutResult;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class ReplBase {

    private ServerFacade server;
    private String authToken = null;
    private HashMap<Integer, Integer> gamesList = new HashMap<>();
    private int nextGameListID = 1;


    public ReplBase(ServerFacade server) throws ResponseException {
        this.server = server;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    protected ReplResponse redrawChessBoard() {
        return null;
    }

    protected ReplResponse highlightLegalMoves() {
        return null;
    }

    protected ReplResponse leaveGame() {
        return new ReplResponse(State.LOGGEDIN, "Leave");
    }

    protected ReplResponse logout() throws Exception {
        try {
            LogoutRequest request = new LogoutRequest(authToken);
            LogoutResult result = server.logout(request);
            authToken = null;
            return new ReplResponse(State.LOGGEDOUT, "User logged out");
        } catch (ResponseException e) {
            if (e.statusCode() == 403) {
                throw new ForbiddenException("Username already taken.");
            }
            ExceptionHandler.handleResponseException(e.statusCode());
        } catch (Exception e) {
            throw new Exception("There was an error while trying to log out.");
        }
        throw new RuntimeException("Something went wrong. You may need to restart Chess.");
    }

    protected ReplResponse quitGame() throws Exception {
        logout();
        return new ReplResponse(State.LOGGEDOUT, "quit");
    }

    protected void outputChessBoard(int listID, String teamColor) throws ResponseException {
        ListGamesResult gamesListResult = server.listGames(new ListGamesRequest(authToken));
        GameData gameData = new ArrayList<>(gamesListResult.games()).get(listID - 1);
        ChessBoardMaker.boardMaker(gameData, teamColor);
    }
}
