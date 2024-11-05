package ui;

import clearservicerecords.ClearRequest;
import exception.ResponseException;
import gameservicerecords.*;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import userservicerecords.RegisterRequest;
import userservicerecords.RegisterResult;

import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChesBoardMakerTests {
    private static Server server;
    static ServerFacade facade;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);

    }

//    @BeforeEach
//    public void setup() {
//        try {
//            facade.clearDB(new ClearRequest());
//        } catch (ResponseException e) {
//            System.out.println("Database clear was unsuccessful");
//        }
//
//    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @Order(1)
    void makeChessBoardTest() throws ResponseException{
        ListGamesResult gamesResult = facade.listGames(new ListGamesRequest("e4d5b410-8e54-429c-8768-077e54619135"));
        GameData gameData = new ArrayList<>(gamesResult.games()).getFirst();
        ChessBoardMaker.boardMaker(gameData, "WHITE");
        ChessBoardMaker.boardMaker(gameData, "BLACK");


    }
}
