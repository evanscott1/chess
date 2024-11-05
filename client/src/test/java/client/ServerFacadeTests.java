package client;

import chess.ChessGame;
import clearservicerecords.ClearRequest;
import dataaccess.GameDataAccess;
import exception.ResponseException;
import gameservicerecords.*;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import ui.ChessBoardMaker;
import userservicerecords.*;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);

    }

    @BeforeEach
    public void setup() {
        try {
            facade.clearDB(new ClearRequest());
        } catch (ResponseException e) {
            System.out.println("Database clear was unsuccessful");
        }

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    void registerSuccess() throws Exception {
        RegisterResult result = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        Assertions.assertTrue(result.authToken().length() > 10);
    }

    @Test
    @Order(2)
    void registerFailure() throws Exception {
        RegisterResult result = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        Assertions.assertThrows(ResponseException.class, () -> facade.register(new RegisterRequest("player1", "password", "p1@email.com")),
        "Register the same user should throw an exception");
    }

    @Test
    @Order(3)
    void loginSuccess() throws Exception {
        RegisterResult registerResult = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        LoginResult loginResult = facade.login(new LoginRequest("player1", "password"));
        Assertions.assertTrue(registerResult.authToken().length() > 10);
    }

    @Test
    @Order(4)
    void loginFailure() throws Exception {
        RegisterResult registerResult = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        LogoutResult logoutResult = facade.logout(new LogoutRequest(registerResult.authToken()));
        Assertions.assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("player1", "password1")),
                "Login wrong password should throw an exception");
    }

    @Test
    @Order(5)
    void logoutSuccess() throws Exception {
        RegisterResult result = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        LogoutResult logoutResult = facade.logout(new LogoutRequest(result.authToken()));

        Assertions.assertThrows(ResponseException.class, () -> facade.listGames(new ListGamesRequest(result.authToken())));
    }

    @Test
    @Order(6)
    void logoutFailure() throws Exception {
        RegisterResult registerResult = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));

        Assertions.assertThrows(ResponseException.class, () -> facade.logout(new LogoutRequest("")));
    }

    @Test
    @Order(7)
    void createGameSuccess() throws Exception {
        RegisterResult result = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        CreateGameResult gameResult = facade.createGame(new CreateGameRequest(result.authToken(), "game1"));
        ListGamesResult gamesResult = facade.listGames(new ListGamesRequest(result.authToken()));
        Assertions.assertEquals(1, gamesResult.games().size());
    }

    @Test
    @Order(8)
    void createGameFailure() throws Exception {
        RegisterResult result = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));

        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(new CreateGameRequest(result.authToken(), "")));
    }

    @Test
    @Order(9)
    void listGamesSuccess() throws Exception {
        RegisterResult result = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        facade.createGame(new CreateGameRequest(result.authToken(), "game1"));
        facade.createGame(new CreateGameRequest(result.authToken(), "game2"));
        facade.createGame(new CreateGameRequest(result.authToken(), "game3"));
        ListGamesResult listGamesResult = facade.listGames(new ListGamesRequest(result.authToken()));

        Assertions.assertEquals(3, listGamesResult.games().size());
    }

    @Test
    @Order(10)
    void listGamesFailure() throws Exception {
        RegisterResult result = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        facade.createGame(new CreateGameRequest(result.authToken(), "game1"));
        facade.createGame(new CreateGameRequest(result.authToken(), "game2"));
        facade.createGame(new CreateGameRequest(result.authToken(), "game3"));

        Assertions.assertThrows(ResponseException.class, () -> facade.listGames(new ListGamesRequest("")));
    }

    @Test
    @Order(11)
    void joinGameSuccess() throws Exception {
        RegisterResult result = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        CreateGameResult gameResult = facade.createGame(new CreateGameRequest(result.authToken(), "game1"));
        JoinGameResult joinGameResult = facade.joinGame(new JoinGameRequest(result.authToken(), "WHITE", 1));
        ListGamesResult gamesResult = facade.listGames(new ListGamesRequest(result.authToken()));
        GameData gameData = new ArrayList<>(gamesResult.games()).getFirst();
        Assertions.assertEquals("game1", gameData.gameName());
    }

    @Test
    @Order(12)
    void joinGameFailure() throws Exception {
        RegisterResult result = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        CreateGameResult gameResult = facade.createGame(new CreateGameRequest(result.authToken(), "game1"));
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(new JoinGameRequest(result.authToken(), "WHITE", 2)));
    }

    @Test
    void makeChessBoardTest() throws ResponseException{
        ListGamesResult gamesResult = facade.listGames(new ListGamesRequest("f9be96ce-effc-4d26-b7f2-f1332f543895"));
        GameData gameData = new ArrayList<>(gamesResult.games()).getFirst();
        ChessBoardMaker.boardMaker(gameData, "WHITE");
        ChessBoardMaker.boardMaker(gameData, "BLACK");


    }

}
