package service;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import service.gameservicerecords.*;

public class GameService {

    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;

    public GameService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess, GameDataAccess gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException, ResponseException {
        authorizeUser(createGameRequest.authToken());
        if (createGameRequest.gameName().isEmpty()) {
            throw new BadRequestException("Create game request has empty required fields");
        }
        GameData gameData = gameDataAccess.addGameData(new GameData(0, null, null, createGameRequest.gameName(), new ChessGame()));

        return new CreateGameResult(gameData.gameID());
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException, ResponseException {
        authorizeUser(joinGameRequest.authToken());

        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() == 0) {
            throw new BadRequestException("Join game request has empty required fields");
        }

        String username = authDataAccess.getAuthData(joinGameRequest.authToken()).username();

        GameData gameData = gameDataAccess.getGameData(joinGameRequest.gameID());


        if (joinGameRequest.playerColor().equals("WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new ForbiddenException("Game color already taken");
            }
            gameData = gameData.setWhiteUsername(username);
        } else if (joinGameRequest.playerColor().equals("BLACK")) {
            if (gameData.blackUsername() != null) {
                throw new ForbiddenException("Game color already taken");
            }
            gameData = gameData.setBlackUsername(username);
        } else {
            throw new BadRequestException("Game color not valid");
        }
        gameDataAccess.updateGameData(gameData);
        return new JoinGameResult();
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException, ResponseException {
        authorizeUser(listGamesRequest.authToken());
        return new ListGamesResult(gameDataAccess.listGameDatas());
    }

    private void authorizeUser(String authToken) throws DataAccessException, ResponseException {
        AuthData authData = authDataAccess.getAuthData(authToken);

        if (authData == null) {
            throw new UnauthorizedException("User not logged in");
        }
    }
}
