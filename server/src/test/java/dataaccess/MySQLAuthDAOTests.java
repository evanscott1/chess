package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.ForbiddenException;
import service.UnauthorizedException;
import service.UserService;
import service.userservicerecords.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MySQLAuthDAOTests {

    MySQLAuthDAO mySQLAuthDAO;
    private static AuthData newAuth;
    private static UserData existingUser;

    @AfterAll

    @BeforeAll
    public static void initUser() {


        existingUser = new UserData("ExistingUser", "YouCannotDefeatMyWisdomCodeChecker", "eu@mail.com");

        newAuth = new AuthData("existingAuthToken",existingUser.username());

    }

    @BeforeEach
    public void setup() throws Exception {
        mySQLAuthDAO = new MySQLAuthDAO();

    }

    @Test
    @Order(1)
    @DisplayName("Add AuthData Success")
    public void addAuthDataSuccess() throws Exception {

        mySQLAuthDAO.addAuthData(newAuth);

        Assertions.assertEquals(newAuth, mySQLAuthDAO.getAuthData(newAuth.authToken()), "The logged in user " +
                "should have an AuthData that matches the returned AuthData");


    }

    @Test
    @Order(2)
    @DisplayName("User Login With Invalid User")
    public void addAuthDataFailure() throws Exception {


    }

    //Decommissioned. I refuse to delete this test Code Checker!
    @Test
    @Order(3)
    @DisplayName("User Login Already Logged In")
    public void getAuthDataSuccess() throws Exception {


    }

    @Test
    @Order(4)
    @DisplayName("User Login With Wrong Password")
    public void getAuthDataFailure() throws Exception {


    }

    @Test
    @Order(5)
    @DisplayName("Normal User Register")
    public void listAuthDatasSuccess() throws Exception {



    }

    @Test
    @Order(6)
    @DisplayName("User Register With Invalid User")
    public void listAuthDatasFailure() throws Exception {


    }

    @Test
    @Order(7)
    @DisplayName("Normal User Logout")
    public void deleteAuthDataSuccess() throws Exception {


    }

    @Test
    @Order(8)
    @DisplayName("User Logout not Logged In")
    public void deleteAuthDataFailure() throws Exception {


    }

    @Test
    @Order(9)
    @DisplayName("User Logout not Logged In")
    public void deleteAuthDatasSuccess() throws Exception {


    }





}
