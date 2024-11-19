package server.websocket;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import websocket.commands.LeaveCommand;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class LeaveService extends BaseService {
    public LeaveService(GameConnectionManager gameConnectionManager) {
        super(gameConnectionManager);
    }

    public void leave(LeaveCommand leaveCommand, Session session) throws IOException, ResponseException {

        ConnectionManager connectionManager;

        Server.gameService.checkUserAuth(leaveCommand.getAuthToken());
        AuthData authData = Server.authDataAccess.getAuthData(leaveCommand.getAuthToken());
        leaveCommand.setUsername(authData.username());

        int gameID = leaveCommand.getGameID();
        connectionManager = gameConnectionManager.getConnectionManager(gameID);
        connectionManager.remove(leaveCommand.getUsername());

        GameData gameData = Server.gameDataAccess.getGameData(gameID);


        if (isPlayer(gameID, leaveCommand.getUsername())) {
            if (gameData.whiteUsername() != null) {
                if (gameData.whiteUsername().equals(leaveCommand.getUsername())) {
                    gameData = gameData.setWhiteUsername(null);
                }
            }
            if (gameData.blackUsername() != null) {
                if (gameData.blackUsername().equals(leaveCommand.getUsername())) {
                    gameData = gameData.setBlackUsername(null);
                }
            }

        }


        Server.gameDataAccess.updateGameData(gameData);

        String notificationMessage = String.format("%s left the game.", leaveCommand.getUsername());
        connectionManager.broadcast(leaveCommand.getUsername(), new NotificationMessage(notificationMessage));


    }


}
