package client;

import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import server.ServerFacade;
import ui.ChessBoardMaker;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReplPlay extends ReplBase {
    public enum InPlayState {
        RESIGN,
        NULL
    }
    InPlayState inPlayState = InPlayState.NULL;


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

        if (params.length == 4) {
            try {
                ArrayList<String> headers = new ArrayList<>(List.of("a", "b", "c", "d", "e", "f", "g", "h"));
                ChessPosition startPosition = ChessBoardMaker.findStartPosition(headers, params[0], params[1]);
                ChessPosition endPosition = ChessBoardMaker.findStartPosition(headers, params[2], params[3]);
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                MakeMoveCommand makeMoveCommand = new MakeMoveCommand(authToken, listID, move);
                makeMoveCommand.setUsername(username);
                ws = new WebSocketFacade(serverURL, teamColor);
                ws.makeMove(makeMoveCommand);
            } catch (Exception e) {
                throw new ResponseException(400, "Please check your move input.");
            }




        return new ReplResponse(State.INPLAY, "");
        }
        throw new ResponseException(400, "Expected: move <startNum> <startAlpha> <endNum> <endAlpha>");
    }


    private ReplResponse resignGame() throws ResponseException {
        System.out.println("Confirm (Y/N):");
        Scanner scanner = new Scanner(System.in);

        String line = scanner.nextLine();
        if (line.toLowerCase().equals("y")) {
            ResignCommand resignCommand = new ResignCommand(authToken, listID);
            resignCommand.setUsername(username);
            ws = new WebSocketFacade(serverURL, teamColor);
            ws.resign(resignCommand);
        }


        return new ReplResponse(State.INPLAY, "");
    }

    private ReplResponse help() {
        return new ReplResponse(State.INPLAY, """
                - move <startNum> <startAlpha> <endNum> <endAlpha> - to make a chess move
                - redraw - outputs current board
                - highlight <num> <alpha> - possible moves of a piece
                - leave - a game
                - resign - forfeit the game
                - quit - playing chess
                - help - with possible commands
                """);
    }

}
