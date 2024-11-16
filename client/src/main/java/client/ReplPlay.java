package client;

import exception.ResponseException;
import server.ServerFacade;
import ui.ChessBoardMaker;

public class ReplPlay extends ReplBase{


    private int gameID = 0;

    public ReplPlay(ServerFacade server) throws ResponseException {
        super(server);
    }


    public ReplResponse evalMenu(String cmd, String... params) throws Exception {
        return switch (cmd) {
            case "move" -> makeMove(params);
            case "redraw" -> redrawChessBoard();
            case "highlight" -> highlightLegalMoves();
            case "leave" -> leaveGame();
            case "resign" -> resignGame();
            case "quit" -> quitGame();
            default -> help();
        };
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }


    private ReplResponse makeMove(String... params) throws ResponseException {
        return new ReplResponse(State.INPLAY, "Not available.");
    }


    private ReplResponse resignGame() {
        return null;
    }

    private ReplResponse help() {
        return new ReplResponse(State.INPLAY, """
                    - move <startposition> <endposition> - to make a chess move
                    - redraw - outputs current board
                    - highlight <position> - possible moves of a piece
                    - leave - a game
                    - resign - forfeit the game
                    - quit - playing chess
                    - help - with possible commands
                    """);
    }

}
