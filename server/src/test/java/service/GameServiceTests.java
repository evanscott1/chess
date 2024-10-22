package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import service.UserServiceRecords.LoginRequest;
import service.UserServiceRecords.LoginResult;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {

        private static UserData existingUser;

        private static UserData newUser;

        private String existingAuth;

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
        public void setup() throws Exception{
            userTable = new MemoryUserDAO();
            authTable = new MemoryAuthDAO();
            service = new UserService(userTable, authTable);
            userTable.addT(existingUser);
        }

        @Test
        @Order(1)
        @DisplayName("Normal User Login")
        public void loginSuccess() throws Exception {


            LoginResult loginResult = service.login(new LoginRequest(existingUser.username(), existingUser.password()));

            Assertions.assertEquals(loginResult.authToken(), authTable.getAuthDataByUsername(existingUser.username()).authToken(), "The logged in user should have an AuthData that matches the returned AuthData");



        }
}
