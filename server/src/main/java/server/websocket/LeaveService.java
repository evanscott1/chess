package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
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

    public void leave(ConnectCommand connectCommand, Session session) throws IOException, ResponseException {

        ConnectionManager connectionManager;

        Server.gameService.checkUserAuth(connectCommand.getAuthToken());


        int gameID = connectCommand.getGameID();
        connectionManager = gameConnectionManager.getConnectionManager(gameID);
        connectionManager.remove(connectCommand.getUsername());



        String notificationMessage = String.format("%s left the game.", connectCommand.getUsername());
        connectionManager.broadcast(connectCommand.getUsername(), new NotificationMessage(notificationMessage));


    }




}
