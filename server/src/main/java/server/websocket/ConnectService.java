package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.BadRequestException;
import exception.ResponseException;
import model.AuthData;
import server.Server;
import websocket.commands.ConnectCommand;
import websocket.messages.ErrorMessage;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class ConnectService extends BaseService {
    public ConnectService(GameConnectionManager gameConnectionManager) {
        super(gameConnectionManager);
    }

    public void connect(ConnectCommand connectCommand, Session session) throws IOException, ResponseException {

        ConnectionManager connectionManager;

            Server.gameService.checkUserAuth(connectCommand.getAuthToken());
            AuthData authData = Server.authDataAccess.getAuthData(connectCommand.getAuthToken());
            connectCommand.setUsername(authData.username());

            ChessGame game;
        int gameID = connectCommand.getGameID();
            if (Server.gameDataAccess.getGameData(gameID) !=null) {
                game = Server.gameDataAccess.getGameData(gameID).game();
            } else {
                throw new BadRequestException("GameID does not exist");
            }



            if (gameConnectionManager.getConnectionManager(gameID) == null) {
                gameConnectionManager.addConnectionManager(gameID, new ConnectionManager());
            }


            connectionManager = gameConnectionManager.getConnectionManager(gameID);
            connectionManager.add(connectCommand.getUsername(), session);


            String loadGameNotification = String.format("You joined game %s as %s.", connectCommand.getGameID(), connectCommand.getJoinType());

            LoadGameMessage loadGameMessage = new LoadGameMessage(null, game);
            connectionManager.get(connectCommand.getUsername()).send(new Gson().toJson(loadGameMessage));


            String notificationMessage = String.format("%s joined the game as %s.", connectCommand.getUsername(), connectCommand.getJoinType());
            connectionManager.broadcast(connectCommand.getUsername(), new NotificationMessage(notificationMessage));


    }


}
