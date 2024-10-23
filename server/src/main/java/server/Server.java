package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import org.eclipse.jetty.client.HttpResponseException;
import service.ClearService;
import service.ClearServiceRecords.ClearRequest;
import service.ClearServiceRecords.ClearResult;
import service.GameService;
import service.GameServiceRecords.*;
import service.UserService;
import service.UserServiceRecords.*;
import spark.*;

import java.util.function.Function;

public class Server {


    private final UserDataAccess userDataAccess = new MemoryUserDAO();
    private final AuthDataAccess authDataAccess = new MemoryAuthDAO();
    private final GameDataAccess gameDataAccess = new MemoryGameDAO();

    private final UserService userService = new UserService(userDataAccess, authDataAccess);
    private final GameService gameService = new GameService(userDataAccess, authDataAccess, gameDataAccess);
    private final ClearService clearService = new ClearService(userDataAccess, authDataAccess, gameDataAccess);


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearApplication);

        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
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

    private <T, R> Object handleNoAuthRequest(Request req, Class<T> requestClass, ServiceFunction<T, R> service) throws Exception {
        T request = new Gson().fromJson(req.body(), requestClass);
        R result = service.apply(request);
        return new Gson().toJson(result);
    }

    private <T, R> Object handleAuthNoBodyRequest(Request req, Class<T> requestClass, ServiceFunction<T, R> service) throws Exception {
        T request = requestClass.getConstructor(String.class).newInstance(req.headers("Authorization"));
        R result = service.apply(request);
        return new Gson().toJson(result);
    }

    private <T, R> Object handleAuthBodyRequest(Request req, Class<T> requestClass, ServiceFunction<T, R> service) throws Exception {
        T request = new Gson().fromJson(req.body(), requestClass);
        R result = service.apply(request);
        return new Gson().toJson(result);
    }

    @FunctionalInterface
    public interface ServiceFunction<T, R> {
        R apply(T t) throws Exception;
    }

    private Object clearApplication(Request req, Response res) throws Exception {
        return handleNoAuthRequest(req, ClearRequest.class, clearService::clear);
    }
    private Object registerUser(Request req, Response res) throws ResponseException, Exception {
        return handleNoAuthRequest(req, RegisterRequest.class, userService::register);
    }
    private Object loginUser(Request req, Response res) throws  Exception {
        return handleNoAuthRequest(req, LoginRequest.class, userService::login);
    }
    private Object logoutUser(Request req, Response res) throws Exception {
        return handleAuthNoBodyRequest(req, LogoutRequest.class, userService::logout);
    }
    private Object listGames(Request req, Response res) throws Exception {
        return handleAuthNoBodyRequest(req, ListGamesRequest.class, gameService::listGames);
    }
    private Object createGame(Request req, Response res) throws Exception {
        return handleAuthBodyRequest(req, CreateGameRequest.class, gameService::createGame);
    }
    private Object joinGame(Request req, Response res) throws  Exception {
        return handleAuthBodyRequest(req, JoinGameRequest.class, gameService::joinGame);
    }

}
