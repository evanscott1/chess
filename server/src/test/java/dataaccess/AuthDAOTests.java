package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.BadRequestException;

import java.util.ArrayList;
import java.util.Collection;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthDAOTests {


    private static AuthDataAccess serverAuthDAO;
    private static AuthData newAuth;

    private static Collection<AuthData> authDataList = new ArrayList<>();


    @BeforeAll
    public static void initAuthDAO() {
        serverAuthDAO = Server.authDataAccess;
        UserData existingUser = new UserData("ExistingUser", "YouCannotDefeatMyWisdomCodeChecker", "eu@mail.com");

        newAuth = new AuthData("newAuthToken", existingUser.username());

        authDataList.add(new AuthData("authToken1", existingUser.username()));
        authDataList.add(new AuthData("authToken2", existingUser.username()));
        authDataList.add(new AuthData("authToken3", existingUser.username()));
        authDataList.add(new AuthData("authToken4", existingUser.username()));
    }

    @BeforeEach
    public void setup() throws Exception {

        serverAuthDAO.deleteAllAuthDatas();
    }


    @Test
    @Order(1)
    @DisplayName("Add AuthData Success")
    public void addAuthDataSuccess() throws Exception {

        serverAuthDAO.addAuthData(newAuth);

        Assertions.assertEquals(newAuth, serverAuthDAO.getAuthData(newAuth.authToken()), "The logged in user " +
                "should have an AuthData that matches the returned AuthData");
    }

    @Test
    @Order(2)
    @DisplayName("Add AuthData Bad Request")
    public void addAuthDataFailure() throws Exception {
        AuthData authData = new AuthData("ACoolAuthToken", null);

        Assertions.assertThrows(BadRequestException.class, () -> serverAuthDAO.addAuthData(authData));

    }

    @Test
    @Order(3)
    @DisplayName("Get AuthData Success")
    public void getAuthDataSuccess() throws Exception {

        serverAuthDAO.addAuthData(newAuth);

        Assertions.assertEquals(newAuth, serverAuthDAO.getAuthData(newAuth.authToken()), "The logged in user " +
                "should have an AuthData that matches the returned AuthData");
    }

    @Test
    @Order(4)
    @DisplayName("Add AuthData Bad Request")
    public void getAuthDataFailure() throws Exception {

        Assertions.assertThrows(BadRequestException.class, () -> serverAuthDAO.getAuthData(""));

    }

    @Test
    @Order(5)
    @DisplayName("List Auth Datas Success")
    public void listAuthDatasSuccess() throws Exception {


        for (AuthData authData : authDataList) {
            serverAuthDAO.addAuthData(authData);
        }


        Assertions.assertIterableEquals(authDataList, serverAuthDAO.listAuthDatas(), "All auth datas should " +
                "have been returned."
        );
    }


    @Test
    @Order(6)
    @DisplayName("Normal User Logout")
    public void deleteAuthDataSuccess() throws Exception {
        serverAuthDAO.addAuthData(newAuth);
        serverAuthDAO.deleteAuthData(newAuth.authToken());

        Assertions.assertNull(serverAuthDAO.getAuthData(newAuth.authToken()), "The logged out user " +
                "should not have an authToken");

    }

    @Test
    @Order(7)
    @DisplayName("User Logout not Logged In")
    public void deleteAuthDataFailure() throws Exception {
        Assertions.assertThrows(BadRequestException.class, () -> serverAuthDAO.getAuthData(""));

    }

    @Test
    @Order(8)
    @DisplayName("User Logout not Logged In")
    public void deleteAuthDatasSuccess() throws Exception {

        for (AuthData authData : authDataList) {
            serverAuthDAO.addAuthData(authData);
        }
        serverAuthDAO.deleteAllAuthDatas();

        Assertions.assertEquals(0, serverAuthDAO.listAuthDatas().size(), "All auth datas should " +
                "have been returned."
        );
    }


}
