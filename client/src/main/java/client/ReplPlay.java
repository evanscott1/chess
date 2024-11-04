package client;

import exception.ResponseException;
import server.ServerFacade;
import ui.ChessBoardMaker;

public class ReplPlay {

    private ServerFacade server;
    private String authToken = null;

    public ReplPlay(ServerFacade server) throws ResponseException {
        this.server = server;
    }

    public ReplResponse evalInPlayMenu(String cmd, String... params) throws ResponseException {
        return switch (cmd) {
            case "move" -> makeMove(params);
            case "leave" -> leaveGame();
            case "quit" -> quitGame();
            default -> help();
        };
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private ReplResponse makeMove(String... params) throws ResponseException {
//        ChessBoardMaker.main(new String[] {""});
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
