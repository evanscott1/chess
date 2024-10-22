package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import service.UserServiceRecords.RegisterRequest;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterServiceTests {

    private static UserData existingUser;

    private static UserData newUser;

    private String existingAuth;

    @AfterAll

    @BeforeAll
    public static void init() {


        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");

        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");


    }

    @BeforeEach
    public void setup() {



    }
    @Test
    @Order(1)
    @DisplayName("Normal User Register")
    public void registerSuccess() throws Exception {
        MemoryUserDAO userTable = new MemoryUserDAO();
        MemoryAuthDAO authTable = new MemoryAuthDAO();
        UserService service = new UserService(userTable, authTable);

        service.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));

        Assertions.assertEquals(newUser, userTable.getUserData(newUser.username()));


    }

}
