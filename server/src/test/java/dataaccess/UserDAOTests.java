package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import exception.BadRequestException;

import java.util.ArrayList;
import java.util.Collection;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTests {


    private static AuthDataAccess serverAuthDAO;
    private static UserDataAccess serverUserDAO;
    private static UserData newUser;

    private static Collection<UserData> userDataList = new ArrayList<>();


    @BeforeAll
    public static void initAuthDAO() {
        serverAuthDAO = Server.authDataAccess;
        serverUserDAO = Server.userDataAccess;
        UserData existingUser = new UserData("ExistingUser", "YouCannotDefeatMyWisdomCodeChecker", "eu@mail.com");

        newUser = new UserData("newUser", "newUserPassword", "nu@email.com");

        userDataList.add(new UserData("username1", "password1", "nu1@email.com"));
        userDataList.add(new UserData("username2", "password2", "nu2@email.com"));
        userDataList.add(new UserData("username3", "password3", "nu3@email.com"));
        userDataList.add(new UserData("username4", "password4", "nu4@email.com"));
    }

    @BeforeEach
    public void setup() throws Exception {

        serverAuthDAO.deleteAllAuthDatas();
        serverUserDAO.deleteAllUserDatas();
    }


    @Test
    @Order(1)
    @DisplayName("Add UserData Success")
    public void addUserDataSuccess() throws Exception {

        serverUserDAO.addUserData(newUser);

        Assertions.assertEquals(newUser, serverUserDAO.getUserData(newUser.username()), "The registered in user " +
                "should have a UserData that matches the returned UserData");
    }

    @Test
    @Order(2)
    @DisplayName("Add UserData Bad Request")
    public void addUserDataFailure() throws Exception {
        UserData authData = new UserData("", newUser.password(), newUser.email());

        Assertions.assertThrows(BadRequestException.class, () -> serverUserDAO.addUserData(authData), "An empty" +
                "UserData field should throw a Bad Request Exception");

    }

    @Test
    @Order(3)
    @DisplayName("Get UserData Success")
    public void getUserDataSuccess() throws Exception {

        serverUserDAO.addUserData(newUser);

        Assertions.assertEquals(newUser, serverUserDAO.getUserData(newUser.username()), "The registered user " +
                "should have an UserData that matches the returned UserData");
    }

    @Test
    @Order(4)
    @DisplayName("Get UserData Bad Request")
    public void getUserDataFailure() throws Exception {

        Assertions.assertThrows(BadRequestException.class, () -> serverUserDAO.getUserData(""));

    }

    @Test
    @Order(5)
    @DisplayName("List User Datas Success")
    public void listUserDatasSuccess() throws Exception {


        for (UserData authData : userDataList) {
            serverUserDAO.addUserData(authData);
        }


        Assertions.assertIterableEquals(userDataList, serverUserDAO.listUserDatas(), "All auth datas should " +
                "have been returned."
        );
    }


    @Test
    @Order(6)
    @DisplayName("User Logout not Logged In")
    public void deleteUserDatasSuccess() throws Exception {

        for (UserData userData : userDataList) {
            serverUserDAO.addUserData(userData);
        }
        serverUserDAO.deleteAllUserDatas();

        Assertions.assertEquals(0, serverUserDAO.listUserDatas().size(), "All auth datas should " +
                "have been returned."
        );
    }


}
