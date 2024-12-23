package server;

import clearservicerecords.ClearRequest;
import clearservicerecords.ClearResult;
import com.google.gson.Gson;
import exception.ResponseException;
import gameservicerecords.*;
import userservicerecords.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        return makeRequest("POST", path, request, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        var path = "/session";
        return makeRequest("POST", path, request, LoginResult.class);
    }

    public LogoutResult logout(LogoutRequest request) throws ResponseException {
        var path = "/session";
        return makeRequest("DELETE", path, request, LogoutResult.class);
    }

    public ListGamesResult listGames(ListGamesRequest request) throws ResponseException {
        var path = "/game";
        return makeRequest("GET", path, request, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest request) throws ResponseException {
        var path = "/game";
        return makeRequest("POST", path, request, CreateGameResult.class);
    }

    public JoinGameResult joinGame(JoinGameRequest request) throws ResponseException {
        var path = "/game";
        return makeRequest("PUT", path, request, JoinGameResult.class);
    }

    public ClearResult clearDB(ClearRequest request) throws ResponseException {
        var path = "/db";
        return makeRequest("DELETE", path, request, ClearResult.class);
    }



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);

            setAuthTokenHeader(request, http);

            if (!method.equals("GET")) {
                http.setDoOutput(true);
                writeBody(request, http);
            }

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException e) {
            throw new ResponseException(e.statusCode(), e.getMessage());
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void setAuthTokenHeader(Object request, HttpURLConnection http) throws IllegalAccessException {
        try {
            Field authTokenField = request.getClass().getDeclaredField("authToken");
            authTokenField.setAccessible(true);
            String authToken = (String) authTokenField.get(request);
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }
        } catch (NoSuchFieldException e) {

        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {

            Object sanitizedAuthRequest = removeAuthToken(request);

            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(sanitizedAuthRequest);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static Object removeAuthToken(Object request) {
        try {
            Field authTokenField = request.getClass().getDeclaredField("authToken");
            authTokenField.setAccessible(true);
            authTokenField.set(request, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {

        }
        return request;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
