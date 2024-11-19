package client;

import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import server.ServerFacade;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;

public class ReplPlay extends ReplBase {


    public ReplPlay(ServerFacade server, String serverURL) throws ResponseException {
        super(server);
        this.serverURL = serverURL;
    }


    public ReplResponse evalMenu(String cmd, String... params) throws Exception {
        return switch (cmd) {
            case "move" -> makeMove(params);
            case "redraw" -> new ReplResponse(State.INPLAY, redrawChessBoard());
            case "highlight" -> new ReplResponse(State.INPLAY, highlightLegalMoves(params));
            case "leave" -> leaveGame();
            case "resign" -> resignGame();
            case "quit" -> quitGame();
            default -> help();
        };
    }

    public void setTeamColor(String color) {
        this.teamColor = color;
    }


    private ReplResponse makeMove(String... params) throws ResponseException {

        ChessPosition startPosition = new ChessPosition(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
        ChessPosition endPosition = new ChessPosition(Integer.parseInt(params[2]), Integer.parseInt(params[3]));
        ChessMove move = new ChessMove(startPosition, endPosition, null);

        MakeMoveCommand makeMoveCommand = new MakeMoveCommand(authToken, listID, move);
        makeMoveCommand.setUsername(username);
        ws = new WebSocketFacade(serverURL);
        ws.makeMove(makeMoveCommand);


        return new ReplResponse(State.INPLAY, "");
    }


    private ReplResponse resignGame() throws ResponseException {

        ResignCommand resignCommand = new ResignCommand(authToken, listID);
        resignCommand.setUsername(username);
        ws = new WebSocketFacade(serverURL);
        ws.resign(resignCommand);

        return new ReplResponse(State.LOGGEDIN, "");
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
