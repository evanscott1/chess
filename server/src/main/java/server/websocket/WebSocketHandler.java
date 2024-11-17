package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

import static server.Server.gameDataAccess;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, ResponseException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(command.getAuthToken(), session);
            case UserGameCommand.CommandType.LEAVE -> leave(command.getAuthToken(), session);
            case UserGameCommand.CommandType.RESIGN -> resign(command.getAuthToken(), session);
        }
    }

    private void connect(String username, int gameID, Session session) throws IOException, ResponseException {

        ChessGame game = Server.gameDataAccess.getGameData(gameID).game();
        LoadGameMessage loadGame = new LoadGameMessage()
        connections.

                connections.add(username, session);
        var message = String.format("%s joined the game", username);
        NotificationMessage notification = new NotificationMessage(message);
        connections.broadcast(username, notification);

    }

    private void makeMove(String authToken, Session session) throws IOException {

    }

    private void leave(String authToken, Session session) throws IOException {

    }

    private void resign(String authToken, Session session) throws IOException {

    }


//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}