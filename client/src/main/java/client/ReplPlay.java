package client;

import exception.ResponseException;
import server.ServerFacade;
import ui.ChessBoardMaker;

public class ReplPlay {

    private ServerFacade server;
    private String authToken = null;
    private int gameID = 0;

    public ReplPlay(ServerFacade server) throws ResponseException {
        this.server = server;
    }

    public ReplResponse evalMenu(String cmd, String... params) throws ResponseException {
        return switch (cmd) {
            case "move" -> makeMove(params);
            case "leave" -> leaveGame();
            case "quit" -> quitGame();
            default -> help();
        };
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private ReplResponse makeMove(String... params) throws ResponseException {


        return new ReplResponse(State.INPLAY, "Not available.");
    }

    private ReplResponse leaveGame() throws ResponseException {
        return new ReplResponse(State.LOGGEDIN, "User left game.");
    }

    private ReplResponse quitGame() {
        return new ReplResponse(null, "quit");
    }

    private ReplResponse help() {
        return new ReplResponse(State.INPLAY, """
                    - move <startposition> <endposition> - to make a chess move
                    - leave - a game
                    - quit - playing chess
                    - help - with possible commands
                    """);
    }

}
