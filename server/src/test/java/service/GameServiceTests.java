package service;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.gameservicerecords.*;
import service.userservicerecords.LoginResult;
import service.userservicerecords.RegisterRequest;
import service.userservicerecords.RegisterResult;

import java.util.ArrayList;
import java.util.Collection;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {

    private static AuthDataAccess serverAuthDAO;
    private static UserDataAccess serverUserDAO;
    private static GameDataAccess serverGameDAO;
    private static GameService serverGameService;
    private static UserService serverUserService;

    private static UserData existingUser;


    private static RegisterResult existingUserRegisterResult;

    private static Collection<CreateGameRequest> createGameRequestsList = new ArrayList<>();


    LoginResult loginResult;

    @AfterAll

    @BeforeAll
    public static void initGame() {

        serverUserService = Server.userService;
        serverGameService = Server.gameService;
        serverAuthDAO = Server.authDataAccess;
        serverUserDAO = Server.userDataAccess;
        serverGameDAO = Server.gameDataAccess;


    }

    @BeforeEach
    public void setup() throws Exception {

        serverGameDAO.deleteAllGameDatas();
        serverUserDAO.deleteAllUserDatas();
        serverAuthDAO.deleteAllAuthDatas();

        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");

        existingUserRegisterResult = serverUserService.register(new RegisterRequest(existingUser.username(),
                existingUser.password(), existingUser.email()));
        createGameRequestsList.clear();
        createGameRequestsList.add(new CreateGameRequest(existingUserRegisterResult.authToken(), "game1"));
        createGameRequestsList.add(new CreateGameRequest(existingUserRegisterResult.authToken(), "game2"));
        createGameRequestsList.add(new CreateGameRequest(existingUserRegisterResult.authToken(), "game3"));
        createGameRequestsList.add(new CreateGameRequest(existingUserRegisterResult.authToken(), "game4"));


    }


    @Test
    @Order(1)
    @DisplayName("Normal Create Game")
    public void createGameSuccess() throws Exception {

        CreateGameResult createGameResult = serverGameService.createGame(
                new CreateGameRequest(existingUserRegisterResult.authToken(), "game1"));

        Assertions.assertEquals(1, createGameResult.gameID(),
                "The newGame should have a GameData that matches the returned GameID");

    }

    @Test
    @Order(2)
    @DisplayName("Create Game With Invalid Auth")
    public void createGameInvalidAuth() throws Exception {

        Assertions.assertThrows(UnauthorizedException.class, () -> serverGameService.createGame(
                new CreateGameRequest("NotTheAuth", "game1")),
                "Logged out user should throw UnathorizedException");

    }

    @Test
    @Order(3)
    @DisplayName("Create Game With Invalid Name")
    public void createGameInvalidName() throws Exception {

        Assertions.assertThrows(BadRequestException.class, () -> serverGameService.createGame(
                new CreateGameRequest(existingUserRegisterResult.authToken(), "")),
                "Empty game name should throw BadRequestException");

    }


    @Test
    @Order(4)
    @DisplayName("Normal Join White Team")
    public void joinWhiteTeamSuccess() throws Exception {

        serverGameService.createGame(new CreateGameRequest(existingUserRegisterResult.authToken(), "game1"));

        JoinGameResult joinGameResult = serverGameService.joinGame(new JoinGameRequest(existingUserRegisterResult.authToken(),
                "WHITE", 1));

        GameData updatedGame = new GameData(1, existingUser.username(), null, "game1", new ChessGame());

        Assertions.assertNotNull(serverGameDAO.getGameData(1).whiteUsername(),
                "The newGame should have a GameData that matches the returned GameID");

    }


    @Test
    @Order(5)
    @DisplayName("Join Game With Invalid Auth")
    public void joinGameInvalidAuth() throws Exception {
        serverGameService.createGame(new CreateGameRequest(existingUserRegisterResult.authToken(), "game1"));


        Assertions.assertThrows(UnauthorizedException.class, () -> serverGameService.joinGame(
                new JoinGameRequest("NotTheAuth", "WHITE",
                1)), "Logged out user should throw UnathorizedException");

    }

    @Test
    @Order(6)
    @DisplayName("Join Game With Claimed Color")
    public void joinGameClaimedColor() throws Exception {

        serverGameService.createGame(new CreateGameRequest(existingUserRegisterResult.authToken(), "game1"));

        JoinGameResult joinGameResult = serverGameService.joinGame(
                new JoinGameRequest(existingUserRegisterResult.authToken(), "WHITE", 1));

        Assertions.assertThrows(ForbiddenException.class, () -> serverGameService.joinGame(
                new JoinGameRequest(existingUserRegisterResult.authToken(), "WHITE",
                1)), "Claimed color should throw ForbiddenException");

    }


    @Test
    @Order(7)
    @DisplayName("Join Game With Empty Color")
    public void joinGameEmptyColor() throws Exception {
        serverGameService.createGame(new CreateGameRequest(existingUserRegisterResult.authToken(), "game1"));

        Assertions.assertThrows(BadRequestException.class, () -> serverGameService.joinGame(new
                        JoinGameRequest(existingUserRegisterResult.authToken(), "", 1)),
                "Empty team color should throw BadRequestException");

    }

    @Test
    @Order(8)
    @DisplayName("Join Game With Invalid Color")
    public void joinGameInvalidColor() throws Exception {

        serverGameService.createGame(new CreateGameRequest(existingUserRegisterResult.authToken(), "game1"));

        Assertions.assertThrows(BadRequestException.class, () -> serverGameService.joinGame(new
                        JoinGameRequest(existingUserRegisterResult.authToken(), "PURPLE", 1)),
                "Empty team color should throw BadRequestException");


    }


    @Test
    @Order(9)
    @DisplayName("Normal List Games")
    public void listGamesSuccess() throws Exception {

        for (CreateGameRequest createGameRequest : createGameRequestsList) {
            serverGameService.createGame(createGameRequest);
        }

        ListGamesResult listGamesResult = serverGameService.listGames(new ListGamesRequest(existingUserRegisterResult.authToken()));


        Assertions.assertEquals(createGameRequestsList.size(), listGamesResult.games().size(),
                "The listGamesResult should have the same values as the updatedGamesTable");

    }

    @Test
    @Order(10)
    @DisplayName("List Games With Invalid Auth")
    public void listGamesInvalidAuth() throws Exception {

        for (CreateGameRequest createGameRequest : createGameRequestsList) {
            serverGameService.createGame(createGameRequest);
        }

        Assertions.assertThrows(UnauthorizedException.class, () -> serverGameService.listGames(
                new ListGamesRequest("NotTheAuth")), "Logged out user should throw UnathorizedException");

    }
}
