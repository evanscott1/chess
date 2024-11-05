package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import exception.BadRequestException;

import java.util.ArrayList;
import java.util.Collection;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameDAOTests {


    private static GameDataAccess serverGameDAO;
    private static GameData newGame;

    private static Collection<GameData> gameDataList = new ArrayList<>();


    @BeforeAll
    public static void initGameDAO() {
        serverGameDAO = Server.gameDataAccess;
        UserData existingUser = new UserData("ExistingUser", "YouCannotDefeatMyWisdomCodeChecker", "eu@mail.com");

        newGame = new GameData(0, null, null, "game1", new ChessGame());

        gameDataList.add(new GameData(1, null, null, "game1", new ChessGame()));
        gameDataList.add(new GameData(2, null, null, "game2", new ChessGame()));
        gameDataList.add(new GameData(3, null, null, "game3", new ChessGame()));
        gameDataList.add(new GameData(4, null, null, "game4", new ChessGame()));
    }

    @BeforeEach
    public void setup() throws Exception {

        serverGameDAO.deleteAllGameDatas();
    }


    @Test
    @Order(1)
    @DisplayName("Add GameData Success")
    public void addGameDataSuccess() throws Exception {

        serverGameDAO.addGameData(newGame);

        Assertions.assertEquals(1, serverGameDAO.getGameData(1).gameID(), "The logged in user " +
                "should have an GameData that matches the returned GameData");
    }

    @Test
    @Order(2)
    @DisplayName("Add GameData Bad Request")
    public void addGameDataFailure() throws Exception {
        GameData gameData = new GameData(0, null, null, "", new ChessGame());

        Assertions.assertThrows(BadRequestException.class, () -> serverGameDAO.addGameData(gameData));

    }

    @Test
    @Order(3)
    @DisplayName("Get GameData Success")
    public void getGameDataSuccess() throws Exception {

        serverGameDAO.addGameData(newGame);

        Assertions.assertEquals(1, serverGameDAO.getGameData(1).gameID(), "The created game " +
                "should have a gameID that matches the returned gameID");
    }

    @Test
    @Order(4)
    @DisplayName("Add GameData Bad Request")
    public void getGameDataFailure() throws Exception {

        Assertions.assertThrows(BadRequestException.class, () -> serverGameDAO.getGameData(-20));

    }

    @Test
    @Order(5)
    @DisplayName("Update Game Data Success")
    public void updateGameDataSuccess() throws Exception {

        serverGameDAO.addGameData(newGame);


        serverGameDAO.updateGameData(new GameData(1, "player1", null,
                newGame.gameName(), newGame.game()));


        Assertions.assertEquals("player1", serverGameDAO.getGameData(1).whiteUsername(),
                "Game whiteUsername should have been returned.");
    }

    @Test
    @Order(5)
    @DisplayName("Update Game Data Success")
    public void updateGameDataFailure() throws Exception {

        serverGameDAO.addGameData(newGame);


        Assertions.assertThrows(BadRequestException.class, () -> serverGameDAO.updateGameData(new GameData(1, "player1", null,
                        "", newGame.game())),
                "Game whiteUsername should have been returned.");
    }

    @Test
    @Order(7)
    @DisplayName("List Game Datas Success")
    public void listGameDatasSuccess() throws Exception {


        for (GameData gameData : gameDataList) {
            serverGameDAO.addGameData(gameData);
        }


        Assertions.assertEquals(gameDataList.size(), serverGameDAO.listGameDatas().size(), "All game datas should " +
                "have been returned."
        );
    }


    @Test
    @Order(8)
    @DisplayName("User Logout not Logged In")
    public void deleteGameDatasSuccess() throws Exception {

        for (GameData gameData : gameDataList) {
            serverGameDAO.addGameData(gameData);
        }
        serverGameDAO.deleteAllGameDatas();

        Assertions.assertEquals(0, serverGameDAO.listGameDatas().size(), "All game datas should " +
                "have been returned."
        );
    }


}
