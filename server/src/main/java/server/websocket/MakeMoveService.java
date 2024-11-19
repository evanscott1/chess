package server.websocket;

import chess.ChessGame;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ForbiddenException;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import websocket.commands.MakeMoveCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MakeMoveService extends BaseService {


    public MakeMoveService(GameConnectionManager gameConnectionManager) {
        super(gameConnectionManager);
    }

    public void makeMove(MakeMoveCommand makeMoveCommand, Session session) throws IOException, ResponseException {

        ConnectionManager connectionManager;

        Server.gameService.checkUserAuth(makeMoveCommand.getAuthToken());
        setUserGameCommandUsername(makeMoveCommand);


        int gameID = makeMoveCommand.getGameID();
        if (gameConnectionManager.getConnectionManager(gameID) == null) {
            throw new ForbiddenException("Game finished.");
        } else {
            connectionManager = gameConnectionManager.getConnectionManager(gameID);

            GameData gameData = Server.gameDataAccess.getGameData(gameID);
            ChessGame game = gameData.game();

            if (!isPlayerTurn(gameID, makeMoveCommand.getUsername())) {
                throw new ForbiddenException("Cannot make move.");
            }


            try {
                game.makeMove(makeMoveCommand.getMove());
            } catch (InvalidMoveException e) {
                throw new ForbiddenException("Invalid chess move.");
            }
            Server.gameDataAccess.updateGameData(gameData);

            if (game.isInCheckmate(game.getTeamTurn())) {
                gameConnectionManager.removeConnectionManager(gameID);
            } else if (game.isInStalemate(game.getTeamTurn())) {
                gameConnectionManager.removeConnectionManager(gameID);
            }



            String loadGameNotification = String.format("You made move %s to %s.",
                    chessPositionToString(makeMoveCommand.getMove().getStartPosition()),
                    chessPositionToString(makeMoveCommand.getMove().getEndPosition()));
            LoadGameMessage loadGameMessage = new LoadGameMessage(null, gameData.game());
            connectionManager.get(makeMoveCommand.getUsername()).send(new Gson().toJson(loadGameMessage));

            loadGameNotification = String.format("%s made move %s to %s.", makeMoveCommand.getUsername(),
                    chessPositionToString(makeMoveCommand.getMove().getStartPosition()),
                    chessPositionToString(makeMoveCommand.getMove().getEndPosition()));
            loadGameMessage = new LoadGameMessage(null, gameData.game());
            connectionManager.broadcast(makeMoveCommand.getUsername(), loadGameMessage);

            String notificationMessage = String.format("%s made move %s to %s.", makeMoveCommand.getUsername(),
                    chessPositionToString(makeMoveCommand.getMove().getStartPosition()),
                    chessPositionToString(makeMoveCommand.getMove().getEndPosition()));
            connectionManager.broadcast(makeMoveCommand.getUsername(), new NotificationMessage(notificationMessage));
        }







    }

    private String chessPositionToString(ChessPosition pos) {

        ArrayList<String> headers = new ArrayList<>(List.of("a", "b", "c", "d", "e", "f", "g", "h"));

            return pos.getRow() + headers.get(pos.getColumn() - 1);

        }

    }

