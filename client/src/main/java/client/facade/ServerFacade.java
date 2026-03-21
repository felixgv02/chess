package client.facade;

import com.google.gson.Gson;
import model.*;
import java.io.*;
import java.net.*;
import java.util.Collection;

public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }
    public AuthData register(String username, String password, String email) throws ResponseException {
        var req = new RegisterRequest(username, password, email);
        return this.makeRequest("POST", "/user", req, AuthData.class, null);
    }
    public AuthData login(String username, String password) throws ResponseException {
        var req = new LoginRequest(username, password);
        return this.makeRequest("POST", "/session", req, AuthData.class, null);
    }
    public void logout(String authToken) throws ResponseException {
        this.makeRequest("DELETE", "/session", null, null, authToken);
    }
    public CreateGameResult createGame(String authToken, String gameName) throws ResponseException {
        var req = new CreateGameRequest(gameName);
        return this.makeRequest("POST", "/game", req, CreateGameResult.class, authToken);
    }
    public Collection<GameData> listGames(String authToken) throws ResponseException {
        return this.makeRequest("GET", "/game", null, ListGamesResult.class, authToken).games();
    }
    public void joinGame(String authToken, String playerColor, int gameID) throws ResponseException {
        var req = new JoinGameRequest(playerColor, gameID);
        this.makeRequest("PUT", "/game", req, null, authToken);
    }
    public void clearDatabase() throws ResponseException {
        this.makeRequest("DELETE", "/db", null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken)
            throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (authToken != null) {
                http.setRequestProperty("authorization", authToken);
            }
            if (request != null) {
                http.setDoOutput(true);
                http.addRequestProperty("Content-Type", "application/json");
                try (var os = http.getOutputStream()) {
                    os.write(new Gson().toJson(request).getBytes());
                }
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    private static void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream errorStream = http.getErrorStream()) {
                if (errorStream != null) {
                    var errorMap = new Gson().fromJson(new InputStreamReader(errorStream), java.util.Map.class);
                    throw new ResponseException(status, (String) errorMap.get("message"));
                }
            }
            throw new ResponseException(status, "other error");
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
    private static boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public record RegisterRequest(String username, String password, String email) {}
    public record LoginRequest(String username, String password) {}
    public record CreateGameRequest(String gameName) {}
    public record CreateGameResult(int gameID) {}
    public record JoinGameRequest(String playerColor, int gameID) {}
    public record ListGamesResult(Collection<GameData> games) {}
}
