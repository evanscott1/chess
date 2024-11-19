package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ForbiddenException;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import websocket.commands.ConnectCommand;
import websocket.commands.ResignCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class ResignService extends BaseService {
    public ResignService(GameConnectionManager gameConnectionManager) {
        super(gameConnectionManager);
    }

    public void resign(ResignCommand resignCommand, Session session) throws IOException, ResponseException {

        ConnectionManager connectionManager;

        Server.gameService.checkUserAuth(resignCommand.getAuthToken());
        ServiceHelper.setUserGameCommandUsername(resignCommand);

        int gameID = resignCommand.getGameID();

        if(gameConnectionManager.getConnectionManager(gameID) == null) {
            throw new ForbiddenException("Game finished.");
        } else {
            connectionManager = gameConnectionManager.getConnectionManager(gameID);


            if (!ServiceHelper.isPlayer(gameID, resignCommand.getUsername())) {
                throw new ForbiddenException("Cannot make move.");
            }


            String message = "You resigned.";
            NotificationMessage notificationMessage = new NotificationMessage(message);
            connectionManager.get(resignCommand.getUsername()).send(new Gson().toJson(notificationMessage));

            message = String.format("%s resigned the game.", resignCommand.getUsername());
            connectionManager.broadcast(resignCommand.getUsername(), new NotificationMessage(message));

            gameConnectionManager.removeConnectionManager(gameID);
        }

    }
}
