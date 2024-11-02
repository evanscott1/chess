package client;

import exception.ResponseException;
import server.ServerFacade;

public class ReplPlay {

    private ServerFacade server;
    private String authToken = null;

    public ReplPlay(ServerFacade server, String authToken) throws ResponseException {
        this.server = server;
        this.authToken = authToken;
    }

    public ReplResponse evalInPlayMenu(String cmd, String... params) throws ResponseException {
        return switch (cmd) {
            case "move" -> makeMove(params);
            case "leave" -> leaveGame();
            case "quit" -> quitGame();
            default -> help();
        };
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
