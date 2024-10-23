package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.GameServiceRecords.CreateGameRequest;
import service.GameServiceRecords.CreateGameResult;
import service.GameServiceRecords.JoinGameRequest;
import service.GameServiceRecords.JoinGameResult;
import service.UserServiceRecords.LoginRequest;
import service.UserServiceRecords.LoginResult;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {

        private static UserData existingUser;

        private static UserData newUser;

        private static AuthData existingAuth;

        private static GameData existingGame;

        private static GameData newGame;

        MemoryUserDAO userTable;

        MemoryAuthDAO authTable;

        MemoryGameDAO gameTable;

        UserService userService;

        GameService gameService;

        LoginResult loginResult;

        @AfterAll

        @BeforeAll
        public static void init() {


            existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");

            newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");

            existingAuth = new AuthData("existingAuth", "ExistingUser");

            existingGame = new GameData(200, null, null, "existingGame", new ChessGame());

            newGame = new GameData(2, null, null, "newGame", new ChessGame());


        }

        @BeforeEach
        public void setup() throws Exception{
            userTable = new MemoryUserDAO();
            authTable = new MemoryAuthDAO();
            gameTable = new MemoryGameDAO();
            userService = new UserService(userTable, authTable);
            gameService = new GameService(userTable, authTable, gameTable);
            userTable.addUserData(existingUser);
            authTable.addAuthData(existingAuth);
            gameTable.addGameData(existingGame);
        }

        @Test
        @Order(1)
        @DisplayName("Normal Create Game")
        public void createGameSuccess() throws Exception {

            CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(existingAuth.authToken(), newGame.gameName()));

            Assertions.assertEquals(newGame, gameTable.getGameData(createGameResult.gameID()), "The newGame should have a GameData that matches the returned GameID");

        }

    @Test
    @Order(2)
    @DisplayName("Create Game With Invalid Auth")
    public void createGameInvalidAuth() throws Exception {

        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(existingAuth.authToken(), newGame.gameName()));

        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.createGame(new CreateGameRequest("NotTheAuth", newGame.gameName())), "Logged out user should throw UnathorizedException");

    }

    @Test
    @Order(3)
    @DisplayName("Create Game With Invalid Auth")
    public void createGameInvalidName() throws Exception {

        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(existingAuth.authToken(), newGame.gameName()));

        Assertions.assertThrows(BadRequestException.class, () -> gameService.createGame(new CreateGameRequest(existingAuth.authToken(), "")), "Empty game name should throw BadRequestException");

    }



    @Test
    @Order(4)
    @DisplayName("Normal Join White Team")
    public void joinWhiteTeamSuccess() throws Exception {

        JoinGameResult joinGameResult = gameService.joinGame(new JoinGameRequest(existingAuth.authToken(), "WHITE", existingGame.gameID()));

        GameData updatedGame = new GameData(200, existingUser.username(), null, "existingGame", new ChessGame());

        Assertions.assertEquals(newGame, gameTable.getGameData(200), "The newGame should have a GameData that matches the returned GameID");

    }
//
//    @Test
//    @Order(1)
//    @DisplayName("Normal Create Game")
//    public void createGameSuccess() throws Exception {
//
//        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(existingAuth.authToken(), newGame.gameName()));
//
//        Assertions.assertEquals(newGame, gameTable.getGameData(createGameResult.gameID()), "The newGame should have a GameData that matches the returned GameID");
//
//    }
}
