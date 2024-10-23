package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import org.eclipse.jetty.client.HttpResponseException;
import service.ClearService;
import service.GameService;
import service.UserService;
import service.UserServiceRecords.RegisterRequest;
import service.UserServiceRecords.RegisterResult;
import spark.*;

public class Server {
//    private final UserService userService;
//    private final GameService gameService;
//    private final ClearService clearService;

    private final UserDataAccess userDataAccess = new MemoryUserDAO();
    private final AuthDataAccess authDataAccess = new MemoryAuthDAO();
    private final GameDataAccess gameDataAccess = new MemoryGameDAO();

    private final UserService userService = new UserService(userDataAccess, authDataAccess);
    private final GameService gameService = new GameService(userDataAccess, authDataAccess, gameDataAccess);
    private final ClearService clearService = new ClearService(userDataAccess, authDataAccess, gameDataAccess);

//    public Server() {}

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.exception(ResponseException.class, this::exceptionHandler);




        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }

    private Object registerUser(Request req, Response res) throws ResponseException, Exception {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(registerRequest);
        return new Gson().toJson(registerResult);
    }
}
