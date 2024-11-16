package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import gameservicerecords.*;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import service.ClearService;
import service.GameService;
import service.UserService;
import clearservicerecords.ClearRequest;
import clearservicerecords.ClearResult;
import spark.Request;
import spark.Response;
import spark.Spark;
import userservicerecords.*;

public class Server {

    //Change server access type here.
    public static UserDataAccess userDataAccess = new MySQLUserDAO();
    public static AuthDataAccess authDataAccess = new MySQLAuthDAO();
    public static GameDataAccess gameDataAccess = new MySQLGameDAO();

    public static UserService userService = new UserService(userDataAccess, authDataAccess);
    public static GameService gameService = new GameService(userDataAccess, authDataAccess, gameDataAccess);
    public static ClearService clearService = new ClearService(userDataAccess, authDataAccess, gameDataAccess);

    private final WebSocketHandler webSocketHandler;

    public Server() {
        webSocketHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory webSocketServletFactory) {

            }
        };
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        //Register web socket here.
        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearApplication);

        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.exception(ResponseException.class, this::exceptionHandler);
        Spark.exception(DataAccessException.class, this::dataAccessExceptionHandler);


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
        res.status(ex.statusCode());
        ResponseExceptionBody responseExceptionBody = new ResponseExceptionBody(ex.getMessage());
        res.body(new Gson().toJson(responseExceptionBody));
    }

    private void dataAccessExceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(500);
        ResponseExceptionBody responseExceptionBody = new ResponseExceptionBody(ex.getMessage());
        res.body(new Gson().toJson(responseExceptionBody));
    }

    private record ResponseExceptionBody(String message) {
    }

    private Object clearApplication(Request req, Response res) throws ResponseException, DataAccessException {
        ClearRequest clearRequest = new Gson().fromJson(req.body(), ClearRequest.class);
        ClearResult clearResult = clearService.clear(clearRequest);
        return new Gson().toJson(clearResult);
    }

    private Object registerUser(Request req, Response res) throws ResponseException, DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(registerRequest);
        return new Gson().toJson(registerResult);
    }

    private Object loginUser(Request req, Response res) throws ResponseException, DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult loginResult = userService.login(loginRequest);
        return new Gson().toJson(loginResult);
    }

    private Object logoutUser(Request req, Response res) throws ResponseException, DataAccessException {
        LogoutRequest logoutRequest = new LogoutRequest(req.headers("Authorization"));
        LogoutResult logoutResult = userService.logout(logoutRequest);
        return new Gson().toJson(logoutResult);
    }

    private Object listGames(Request req, Response res) throws ResponseException, DataAccessException {
        ListGamesRequest listGamesRequest = new ListGamesRequest(req.headers("Authorization"));
        ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
        return new Gson().toJson(listGamesResult);
    }

    private Object createGame(Request req, Response res) throws ResponseException, DataAccessException {
        CreateGameRequest body = new Gson().fromJson(req.body(), CreateGameRequest.class);
        CreateGameRequest createGameRequest = new CreateGameRequest(req.headers("Authorization"), body.gameName());
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        return new Gson().toJson(createGameResult);
    }

    private Object joinGame(Request req, Response res) throws ResponseException, DataAccessException {

        JoinGameRequest body = new Gson().fromJson(req.body(), JoinGameRequest.class);
        JoinGameRequest joinGameRequest = new JoinGameRequest(req.headers("Authorization"), body.playerColor(), body.gameID());
        JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
        return new Gson().toJson(joinGameResult);
    }

}
