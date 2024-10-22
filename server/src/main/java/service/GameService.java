package service;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.GameServiceRecords.*;

import java.util.UUID;

public class GameService {

    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;

    public GameService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess, GameDataAccess gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException, UnauthorizedException {
        authorizeUser(createGameRequest.authToken());

        GameData gameData = gameDataAccess.addGameData(new GameData(0, null, null, createGameRequest.gameName(), new ChessGame()));

        return new CreateGameResult(gameData.gameID());
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException, UnauthorizedException {
        authorizeUser(joinGameRequest.authToken());
        String username = authDataAccess.getAuthData(joinGameRequest.authToken()).username();

        GameData gameData = gameDataAccess.getGameData(joinGameRequest.gameID());
        if (joinGameRequest.playerColor().equals("WHITE")) {
            gameData.setWhiteUsername(username);
        } else if(joinGameRequest.playerColor().equals("BLACK")) {
            gameData.setBlackUsername(username);
        }

        return new JoinGameResult();
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException, UnauthorizedException {
        authorizeUser(listGamesRequest.authToken());
        return new ListGamesResult(gameDataAccess.listGameDatas());
    }

    private void authorizeUser(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData authData = authDataAccess.getAuthData(authToken);

        if(authData == null) {
            throw new UnauthorizedException("User not logged in");
        }
    }
}
