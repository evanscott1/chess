package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.gameServiceRecords.*;
import service.userServiceRecords.LoginResult;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {

    private static UserData existingUser;

    private static UserData newUser;

    private static AuthData existingAuth;

    private static GameData existingGame;

    private static GameData existingGameWhiteTeam;

    private static GameData newGame;

    MemoryUserDAO userTable;

    MemoryAuthDAO authTable;

    MemoryGameDAO gameTable;

    MemoryGameDAO updatedGamesTable;

    UserService userService;

    GameService gameService;

    LoginResult loginResult;

    @AfterAll

    @BeforeAll
    public static void initGame() {


        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");

        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");

        existingAuth = new AuthData("existingAuth", "ExistingUser");

        existingGame = new GameData(1, null, null, "existingGame", new ChessGame());

        existingGameWhiteTeam = new GameData(2, existingUser.username(), null, "existingGameWhiteTeam", new ChessGame());

        newGame = new GameData(3, null, null, "newGame", new ChessGame());
    }

    @BeforeEach
    public void setup() throws Exception {
        userTable = new MemoryUserDAO();
        authTable = new MemoryAuthDAO();

        gameTable = new MemoryGameDAO();
        updatedGamesTable = new MemoryGameDAO();

        userService = new UserService(userTable, authTable);
        gameService = new GameService(userTable, authTable, gameTable);
        userTable.addUserData(existingUser);

        authTable.addAuthData(existingAuth);

        gameTable.addGameData(existingGame);
        gameTable.addGameData(existingGameWhiteTeam);


        updatedGamesTable.addGameData(existingGame);
        updatedGamesTable.addGameData(existingGameWhiteTeam);
        updatedGamesTable.addGameData(newGame);
    }

    @Test
    @Order(1)
    @DisplayName("Normal Create Game")
    public void createGameSuccess() throws Exception {

        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(existingAuth.authToken(), newGame.gameName()));

        Assertions.assertEquals(newGame.gameID(), createGameResult.gameID(), "The newGame should have a GameData that matches the returned GameID");

    }

    @Test
    @Order(2)
    @DisplayName("Create Game With Invalid Auth")
    public void createGameInvalidAuth() throws Exception {

        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.createGame(new CreateGameRequest("NotTheAuth", newGame.gameName())),
                "Logged out user should throw UnathorizedException");

    }

    @Test
    @Order(3)
    @DisplayName("Create Game With Invalid Name")
    public void createGameInvalidName() throws Exception {

        Assertions.assertThrows(BadRequestException.class, () -> gameService.createGame(new CreateGameRequest(existingAuth.authToken(), "")),
                "Empty game name should throw BadRequestException");

    }


    @Test
    @Order(4)
    @DisplayName("Normal Join White Team")
    public void joinWhiteTeamSuccess() throws Exception {

        JoinGameResult joinGameResult = gameService.joinGame(new JoinGameRequest(existingAuth.authToken(), "WHITE", existingGame.gameID()));

        GameData updatedGame = new GameData(existingGame.gameID(), existingUser.username(), null, existingGame.gameName(), new ChessGame());

        Assertions.assertEquals(updatedGame.gameID(), existingGame.gameID(), "The newGame should have a GameData that matches the returned GameID");

    }


    @Test
    @Order(5)
    @DisplayName("Join Game With Invalid Auth")
    public void joinGameInvalidAuth() throws Exception {

        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.joinGame(new JoinGameRequest("NotTheAuth", "WHITE",
                existingGame.gameID())), "Logged out user should throw UnathorizedException");

    }

    @Test
    @Order(6)
    @DisplayName("Join Game With Claimed Color")
    public void joinGameClaimedColor() throws Exception {


        Assertions.assertThrows(ForbiddenException.class, () -> gameService.joinGame(new JoinGameRequest(existingAuth.authToken(), "WHITE",
                existingGameWhiteTeam.gameID())), "Claimed color should throw ForbiddenException");

    }


    @Test
    @Order(7)
    @DisplayName("Join Game With Empty Color")
    public void joinGameEmptyColor() throws Exception {

        Assertions.assertThrows(BadRequestException.class, () -> gameService.joinGame(new JoinGameRequest(existingAuth.authToken(), "",
                existingGameWhiteTeam.gameID())), "Empty team color should throw BadRequestException");

    }

    @Test
    @Order(8)
    @DisplayName("Join Game With Invalid Color")
    public void joinGameInvalidColor() throws Exception {

        Assertions.assertThrows(BadRequestException.class, () -> gameService.joinGame(new JoinGameRequest(existingAuth.authToken(), "PURPLE",
                existingGameWhiteTeam.gameID())), "Invalid team color should throw BadRequestException");

    }


    @Test
    @Order(9)
    @DisplayName("Normal List Games")
    public void listGamesSuccess() throws Exception {

        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(existingAuth.authToken(), newGame.gameName()));

        ListGamesResult listGamesResult = gameService.listGames(new ListGamesRequest(existingAuth.authToken()));


        Assertions.assertEquals(updatedGamesTable.listGameDatas().size(), listGamesResult.games().size(), "The listGamesResult should have the same" +
                " values as the updatedGamesTable");

    }

    @Test
    @Order(10)
    @DisplayName("List Games With Invalid Auth")
    public void listGamesInvalidAuth() throws Exception {

        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.listGames(new ListGamesRequest("NotTheAuth")), "Logged out user " +
                "should throw UnathorizedException");

    }
}
