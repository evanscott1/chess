package client;

import exception.ResponseException;
import server.ServerFacade;

public class ReplObserve extends ReplBase {

    private ServerFacade server;
    private String authToken;

    public ReplObserve(ServerFacade server) throws ResponseException {
        super(server);
    }


    public ReplResponse evalMenu(String cmd, String... params) throws Exception {

        return switch (cmd) {
            case "redraw" -> redrawChessBoard();
            case "highlight" -> highlightLegalMoves();
            case "leave" -> leaveGame();
            case "quit" -> quitGame();
            default -> help();
        };
    }



    private ReplResponse help() {
        return new ReplResponse(State.OBSERVATION, """
                    - redraw - outputs current board
                    - highlight <position> - possible moves of a piece
                    - leave - a game
                    - quit - playing chess
                    - help - with possible commands
                    """);
    }





}
