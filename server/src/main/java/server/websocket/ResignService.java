package server.websocket;

import com.google.gson.Gson;
import exception.ForbiddenException;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import websocket.commands.ResignCommand;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class ResignService extends BaseService {
    public ResignService(GameConnectionManager gameConnectionManager) {
        super(gameConnectionManager);
    }

    public void resign(ResignCommand resignCommand, Session session) throws IOException, ResponseException {

        ConnectionManager connectionManager;

        Server.gameService.checkUserAuth(resignCommand.getAuthToken());
        setUserGameCommandUsername(resignCommand);

        int gameID = resignCommand.getGameID();

        if (gameConnectionManager.getConnectionManager(gameID) == null) {
            throw new ForbiddenException("Game finished.");
        } else {
            connectionManager = gameConnectionManager.getConnectionManager(gameID);


            if (!isPlayer(gameID, resignCommand.getUsername())) {
                throw new ForbiddenException("Cannot resign as observer.");
            }


            String message = "You resigned.";
            NotificationMessage notificationMessage = new NotificationMessage(message);
            connectionManager.get(resignCommand.getUsername()).send(new Gson().toJson(notificationMessage));

            message = String.format("%s resigned the game.", resignCommand.getUsername());
            connectionManager.broadcast(resignCommand.getUsername(), new NotificationMessage(message));

        }

    }
}
