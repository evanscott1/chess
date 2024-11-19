package client;

import client.websocket.NotificationHandler;
import exception.ResponseException;
import server.ServerFacade;

public class ReplObserve extends ReplBase {


    public ReplObserve(ServerFacade server, String serverURL, NotificationHandler notificationHandler) throws ResponseException {
        super(server);
        teamColor = "WHITE";
        this.serverURL = serverURL;
        this.notificationHandler = notificationHandler;
    }


    public ReplResponse evalMenu(String cmd, String... params) throws Exception {

        return switch (cmd) {
            case "redraw" -> new ReplResponse(State.OBSERVATION, redrawChessBoard());
            case "highlight" -> new ReplResponse(State.OBSERVATION, highlightLegalMoves(params));
            case "leave" -> leaveGame();
            case "quit" -> quitGame();
            default -> help();
        };
    }


    private ReplResponse help() {
        return new ReplResponse(State.OBSERVATION, """
                - redraw - outputs current board
                - highlight <num> <alpha> - possible moves of a piece
                - leave - a game
                - quit - playing chess
                - help - with possible commands
                """);
    }


}
