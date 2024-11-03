package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import clearservicerecords.ClearRequest;
import gameservicerecords.CreateGameRequest;
import userservicerecords.RegisterRequest;
import userservicerecords.RegisterResult;

import java.util.ArrayList;
import java.util.Collection;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearServiceTests {

    private static AuthDataAccess serverAuthDAO;
    private static UserDataAccess serverUserDAO;
    private static GameDataAccess serverGameDAO;
    private static ClearService serverClearService;
    private static GameService serverGameService;
    private static UserService serverUserService;

    private static Collection<RegisterRequest> registerRequestsList = new ArrayList<>();
    private static Collection<CreateGameRequest> createGameRequestsList = new ArrayList<>();

    private static UserData existingUser;

    private static RegisterResult existingUserRegisterResult;

    @BeforeAll
    public static void initClear() {

        serverClearService = Server.clearService;
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

        createGameRequestsList.add(new CreateGameRequest(existingUserRegisterResult.authToken(), "game1"));
        createGameRequestsList.add(new CreateGameRequest(existingUserRegisterResult.authToken(), "game2"));
        createGameRequestsList.add(new CreateGameRequest(existingUserRegisterResult.authToken(), "game3"));
        createGameRequestsList.add(new CreateGameRequest(existingUserRegisterResult.authToken(), "game4"));

        registerRequestsList.add(new RegisterRequest("ExistingUser1", "existingUserPassword1", "eu1@mail.com"));
        registerRequestsList.add(new RegisterRequest("ExistingUser2", "existingUserPassword2", "eu2@mail.com"));
        registerRequestsList.add(new RegisterRequest("ExistingUser3", "existingUserPassword3", "eu3@mail.com"));
        registerRequestsList.add(new RegisterRequest("ExistingUser4", "existingUserPassword4", "eu4@mail.com"));


    }

    @Test
    @Order(1)
    @DisplayName("Clear Database")
    public void clearSuccess() throws Exception {

        for (RegisterRequest registerRequest : registerRequestsList) {
            serverUserService.register(registerRequest);
        }

        for (CreateGameRequest createGameRequest : createGameRequestsList) {
            serverGameService.createGame(createGameRequest);
        }


        serverClearService.clear(new ClearRequest());

        Assertions.assertEquals(0, serverAuthDAO.listAuthDatas().size(), "The gameTable should be empty");
        Assertions.assertEquals(0, serverGameDAO.listGameDatas().size(), "The gameTable should be empty");
        Assertions.assertEquals(0, serverUserDAO.listUserDatas().size(), "The gameTable should be empty");
    }

}
