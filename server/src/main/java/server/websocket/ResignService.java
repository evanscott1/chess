package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ForbiddenException;
import exception.ResponseException;
import model.AuthData;
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
        AuthData authData = Server.authDataAccess.getAuthData(resignCommand.getAuthToken());
        resignCommand.setUsername(authData.username());

        int gameID = resignCommand.getGameID();

        if(gameConnectionManager.isFinished(gameID)) {
            throw new ForbiddenException("Game finished.");
        } else {
            gameConnectionManager.removeConnectionManager(gameID);

            connectionManager = gameConnectionManager.getConnectionManager(gameID);

            String notificationMessage = String.format("%s resigned the game.", resignCommand.getUsername());
            connectionManager.broadcast(resignCommand.getUsername(), new NotificationMessage(notificationMessage));
        }

    }
}
