package client;

import exception.ResponseException;
import server.ServerFacade;

public class ReplObserve {

    private ServerFacade server;
    private String authToken;

    public ReplObserve(ServerFacade server, String authToken) throws ResponseException {
        this.server = server;
        this.authToken = authToken;
    }

    public ReplResponse evalObserveMenu(String cmd, String... params) throws ResponseException {

        return switch (cmd) {
            case "leave" -> leaveGame();
            case "quit" -> quitGame();
            default -> help();
        };
    }

    private ReplResponse leaveGame() {
        return new ReplResponse(State.LOGGEDIN, "Leave");
    }

    private ReplResponse quitGame() {
        return new ReplResponse(null, "quit");
    }

    private ReplResponse help() {
        return new ReplResponse(State.OBSERVATION, """
                    - leave - a game
                    - quit - playing chess
                    - help - with possible commands
                    """);
    }





}
