package server.websocket;

import chess.ChessGame;
import exception.ForbiddenException;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import server.Server;
import websocket.commands.UserGameCommand;

public class ServiceHelper {

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
        return gameData.whiteUsername().equals(username) || gameData.blackUsername().equals(username);
    }

    public static void setUserGameCommandUsername(UserGameCommand command) throws ResponseException {
        AuthData authData = Server.authDataAccess.getAuthData(command.getAuthToken());
        command.setUsername(authData.username());
    }


}
