package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import service.UserServiceRecords.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {

    private static UserData existingUser;

    private static UserData newUser;

    MemoryUserDAO userTable;

    MemoryAuthDAO authTable;

    UserService service;

    @AfterAll

    @BeforeAll
    public static void init() {


        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");

        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");


    }

    @BeforeEach
    public void setup() throws Exception {
        userTable = new MemoryUserDAO();
        authTable = new MemoryAuthDAO();
        service = new UserService(userTable, authTable);
        userTable.addUserData(existingUser);
    }

    @Test
    @Order(1)
    @DisplayName("Normal User Login")
    public void loginSuccess() throws Exception {


        LoginResult loginResult = service.login(new LoginRequest(existingUser.username(), existingUser.password()));

        Assertions.assertEquals(loginResult.authToken(), authTable.getAuthDataByUsername(existingUser.username()).authToken(), "The logged in user should have an AuthData that matches the returned AuthData");


    }

    @Test
    @Order(2)
    @DisplayName("User Login With Invalid User")
    public void loginInvalidUser() throws Exception {

        Assertions.assertThrows(UnauthorizedException.class, () -> service.login(new LoginRequest(newUser.username(), newUser.password())), "Invalid user login should throw UnathorizedException");

    }
//Decommissioned
//    @Test
//    @Order(3)
//    @DisplayName("User Login Already Logged In")
//    public void loginAlreadyLoggedIn() throws Exception {
//
//        service.login(new LoginRequest(existingUser.username(), existingUser.password()));
//        Assertions.assertThrows(UnauthorizedException.class, () -> service.login(new LoginRequest(existingUser.username(), existingUser.password())), "Already logged in user should throw UnathorizedException");
//    }

    @Test
    @Order(4)
    @DisplayName("User Login With Wrong Password")
    public void loginInvalidPassword() throws Exception {

        Assertions.assertThrows(UnauthorizedException.class, () -> service.login(new LoginRequest(existingUser.username(), "NotThePassword")), "Invalid password should throw UnathorizedException");
    }


    @Test
    @Order(5)
    @DisplayName("Normal User Register")
    public void registerSuccess() throws Exception {


        RegisterResult registerResult = service.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));

        Assertions.assertEquals(newUser, userTable.getUserData(newUser.username()), "The registered userData should be the same as the input userData");

        Assertions.assertEquals(newUser.username(), authTable.getAuthData(registerResult.authToken()).username(), "The registered user should have an authData");

    }

    @Test
    @Order(6)
    @DisplayName("User Register With Invalid User")
    public void registerInvalidUser() throws Exception {

        Assertions.assertThrows(ForbiddenException.class, () -> service.register(new RegisterRequest(existingUser.username(), existingUser.password(), existingUser.email())), "Invalid username should throw UnathorizedException");
    }

    @Test
    @Order(7)
    @DisplayName("Normal User Logout")
    public void logoutSuccess() throws Exception {

        LoginResult loginResult = service.login(new LoginRequest(existingUser.username(), existingUser.password()));
        service.logout(new LogoutRequest(loginResult.authToken()));

        Assertions.assertNull(authTable.getAuthDataByUsername(loginResult.username()), "The logged in user should not have an AuthData");
    }

    @Test
    @Order(8)
    @DisplayName("User Logout not Logged In")
    public void logoutNotLoggedIn() throws Exception {

        Assertions.assertThrows(UnauthorizedException.class, () -> service.logout(new LogoutRequest("NotTheAuthToken")), "Already logged out user should throw UnathorizedException");
    }

}
