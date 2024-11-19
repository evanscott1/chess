package server.websocket;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import server.Server;
import websocket.commands.UserGameCommand;

public class BaseService {
    GameConnectionManager gameConnectionManager;

    public BaseService(GameConnectionManager gameConnectionManager) {
        this.gameConnectionManager = gameConnectionManager;
    }


    public static boolean isPlayerTurn(int gameID, String username) throws ResponseException {
        GameData gameData = Server.gameDataAccess.getGameData(gameID);
        ChessGame game = gameData.game();

        String turnColor = game.getTeamTurn().toString();
        String currentPlayer = "";
        if (turnColor.equals("WHITE")) {
            currentPlayer = gameData.whiteUsername();
        } else if (turnColor.equals("BLACK")) {
            currentPlayer = gameData.blackUsername();
        }

        return currentPlayer.equals(username);
    }

    public static boolean isPlayer(int gameID, String username) throws ResponseException {
        GameData gameData = Server.gameDataAccess.getGameData(gameID);
        if (gameData.whiteUsername() != null) {
            if (gameData.whiteUsername().equals(username)) {
                return true;
            }
        }

        if (gameData.blackUsername() != null) {
            if (gameData.blackUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void setUserGameCommandUsername(UserGameCommand command) throws ResponseException {
        AuthData authData = Server.authDataAccess.getAuthData(command.getAuthToken());
        command.setUsername(authData.username());
    }
}
