package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.messages.LoadGameMessage;
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

        String notificationMessage = String.format("%s left the game.", leaveCommand.getUsername());
        connectionManager.broadcast(leaveCommand.getUsername(), new NotificationMessage(notificationMessage));


    }




}
