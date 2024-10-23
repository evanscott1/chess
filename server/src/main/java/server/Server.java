package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import service.ClearService;
import service.clearservicerecords.ClearRequest;
import service.clearservicerecords.ClearResult;
import service.GameService;
import service.gameservicerecords.*;
import service.UserService;
import service.userservicerecords.*;
import spark.*;

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

    private Object registerUser(Request req, Response res) throws ResponseException, ResponseException, DataAccessException {
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
        CreateGameRequest createGameRequest = new CreateGameRequest(req.headers("Authorization"), req.body());
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
