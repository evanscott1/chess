package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.clearServiceRecords.ClearRequest;
import service.userServiceRecords.LoginResult;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearServiceTests {

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

    ClearService clearService;


    @AfterAll

    @BeforeAll
    public static void initClear() {

        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.co");

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

        userService = new UserService(userTable, authTable);
        gameService = new GameService(userTable, authTable, gameTable);
        clearService = new ClearService(userTable, authTable, gameTable);

        userTable.addUserData(existingUser);
        userTable.addUserData(newUser);

        authTable.addAuthData(existingAuth);
        authTable.addAuthData(new AuthData("newAuthData", newUser.username()));

        gameTable.addGameData(existingGame);
        gameTable.addGameData(existingGameWhiteTeam);
        gameTable.addGameData(newGame);

    }

    @Test
    @Order(1)
    @DisplayName("Clear Database")
    public void clearSuccess() throws Exception {
        clearService.clear(new ClearRequest());

        Assertions.assertEquals(0, authTable.listAuthDatas().size(), "The gameTable should be empty");
        Assertions.assertEquals(0, gameTable.listGameDatas().size(), "The gameTable should be empty");
        Assertions.assertEquals(0, userTable.listUserDatas().size(), "The gameTable should be empty");
    }

}
