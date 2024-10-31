package service;

import dataaccess.AuthDataAccess;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDataAccess;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.userservicerecords.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {

    private static UserData existingUser;

    private static UserData newUser;

    private static AuthDataAccess serverAuthDAO;
    private static UserDataAccess serverUserDAO;
    private static UserService serverUserService;



    @BeforeAll
    public static void initUser() {


        existingUser = new UserData("ExistingUser", "YouCannotDefeatMyWisdomCodeChecker", "eu@mail.com");

        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");


    }

    @BeforeEach
    public void setup() throws Exception {
        serverUserService = Server.userService;
        serverAuthDAO = Server.authDataAccess;
        serverUserDAO = Server.userDataAccess;
        serverUserDAO.deleteAllUserDatas();
        serverAuthDAO.deleteAllAuthDatas();

    }

    @Test
    @Order(1)
    @DisplayName("Normal User Login")
    public void loginSuccess() throws Exception {
        RegisterResult registerResult = serverUserService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));
        serverUserService.logout(new LogoutRequest(registerResult.authToken()));
        LoginResult loginResult = serverUserService.login(new LoginRequest(newUser.username(), newUser.password()));

        Assertions.assertEquals(loginResult.username(), serverAuthDAO.getAuthData(loginResult.authToken()).username(), "The logged in user " +
                "should have an AuthData that matches the returned AuthData");

    }

    @Test
    @Order(2)
    @DisplayName("User Login With Invalid User")
    public void loginInvalidUser() throws Exception {

        Assertions.assertThrows(UnauthorizedException.class, () -> serverUserService.login(new LoginRequest(newUser.username(), newUser.password())),
                "Invalid user login should throw UnathorizedException");
    }

    @Test
    @Order(4)
    @DisplayName("User Login With Wrong Password")
    public void loginInvalidPassword() throws Exception {

        Assertions.assertThrows(UnauthorizedException.class, () -> serverUserService.login(new LoginRequest(existingUser.username(),
                        "NotThePassword")), "Invalid password should throw UnathorizedException");
    }


    @Test
    @Order(5)
    @DisplayName("Normal User Register")
    public void registerSuccess() throws Exception {


        RegisterResult registerResult = serverUserService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));


        Assertions.assertEquals(newUser.username(), serverAuthDAO.getAuthData(registerResult.authToken()).username(), "The registered user should have " +
                "an authData");
        Assertions.assertNotNull(serverAuthDAO.getAuthData(registerResult.authToken()), "The registered user should have an authToken");

    }

    @Test
    @Order(6)
    @DisplayName("User Register With Invalid User")
    public void registerInvalidUser() throws Exception {

        RegisterResult registerResult = serverUserService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));

        Assertions.assertThrows(ForbiddenException.class, () -> serverUserService.register(new RegisterRequest(newUser.username(),
                newUser.password(), newUser.email())), "Invalid username should throw UnathorizedException");
    }

    @Test
    @Order(7)
    @DisplayName("Normal User Logout")
    public void logoutSuccess() throws Exception {
        RegisterResult registerResult = serverUserService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));

        serverUserService.logout(new LogoutRequest(registerResult.authToken()));

        Assertions.assertNull(serverAuthDAO.getAuthData(registerResult.authToken()), "The logged in user should not have an AuthData");
    }

    @Test
    @Order(8)
    @DisplayName("User Logout not Logged In")
    public void logoutNotLoggedIn() throws Exception {

        Assertions.assertThrows(UnauthorizedException.class, () -> serverUserService.logout(new LogoutRequest("NotTheAuthToken")), "Already logged out user " +
                "should throw UnathorizedException");
    }

}
