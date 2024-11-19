package client.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import gameservicerecords.ListGamesRequest;
import gameservicerecords.ListGamesResult;
import model.GameData;
import ui.ChessBoardMaker;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class ServerMessageHandler {

    public static void serverMessageHandler(String message, String teamColor) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

        switch (serverMessage.getServerMessageType()) {
            case ServerMessage.ServerMessageType.NOTIFICATION -> notification(message);
            case ServerMessage.ServerMessageType.LOAD_GAME -> loadGame(message, teamColor);
            case ServerMessage.ServerMessageType.ERROR -> error(message);
        }

    }

    private static void notification(String message) {
        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
        notify(notificationMessage.getMessage());
    }

    private static void loadGame(String message, String teamColor) {
        LoadGameMessage loadGameMessage = new Gson().fromJson(message,  LoadGameMessage.class);
        ChessBoardMaker.boardMaker(loadGameMessage.getGame(), teamColor);
    }

    private static void error(String message) {
        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
        notify(errorMessage.getErrorMessage());
    }

    private static void printPrompt() {
        System.out.print("\n" + SET_TEXT_BOLD + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    private static void notify(String message) {
        System.out.println(SET_TEXT_COLOR_RED + message);
        printPrompt();
    }
}
