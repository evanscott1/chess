package server.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.ErrorMessage;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final GameConnectionManager gameConnectionManager = new GameConnectionManager();
    private final MakeMoveService makeMoveService = new MakeMoveService(gameConnectionManager);
    private final ResignService resignService = new ResignService(gameConnectionManager);
    private final ConnectService connectService = new ConnectService(gameConnectionManager);
    private final LeaveService leaveService = new LeaveService(gameConnectionManager);

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> connect(message, session);
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(message, session);
            case UserGameCommand.CommandType.LEAVE -> leave(message, session);
            case UserGameCommand.CommandType.RESIGN -> resign(message, session);
        }
    }

    private void connect(String message, Session session) throws IOException {

        ConnectCommand connectCommand = new Gson().fromJson(message, ConnectCommand.class);
        try {
            connectService.connect(connectCommand, session);
        } catch (ResponseException e) {
            errorHandler(e.getMessage(), session);
        }

    }

    private void makeMove(String message, Session session) throws IOException {


        MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        try {
            makeMoveService.makeMove(makeMoveCommand, session);
        } catch (ResponseException e) {
            errorHandler(e.getMessage(), session);
        }
    }

    private void leave(String message, Session session) throws IOException {
        LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
        try {
            leaveService.leave(leaveCommand, session);
        } catch (ResponseException e) {
            errorHandler(e.getMessage(), session);
        }
    }

    private void resign(String message, Session session) throws IOException {
        ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
        try {
            resignService.resign(resignCommand, session);
        } catch (ResponseException e) {
            errorHandler(e.getMessage(), session);
        }
    }

    private void errorHandler(String message, Session session) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage(String.format("Error: %s", message));

        new Connection("", session).send(new Gson().toJson(errorMessage));
    }


}