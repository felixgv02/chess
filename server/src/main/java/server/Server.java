package server;
import com.google.gson.Gson;
import server.dataaccess.*;
import service.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

import io.javalin.*;

public class Server {
    private final Javalin javalin;
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(gameDAO, authDAO);
    private final ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
    private final Gson gson = new Gson();
    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.exception(DataAccessException.class, this::exceptionHandler);
        javalin.delete("/db", this::clearHandler);
        javalin.post("/user", this::registerHandler);
        javalin.post("/session", this::loginHandler);
        javalin.delete("/session", this::logoutHandler);
        javalin.get("/game", this::listGamesHandler);
        javalin.post("/game", this::createGameHandler);
        javalin.put("/game", this::joinGameHandler);
    }
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }
    private void exceptionHandler(DataAccessException e, Context ctx) {
        if (e.getMessage().equals("Error: bad request")) ctx.status(400);
        else if (e.getMessage().equals("Error: unauthorized")) ctx.status(401);
        else if (e.getMessage().equals("Error: already taken")) ctx.status(403);
        else ctx.status(500);
        ctx.result(gson.toJson(new ErrorResult(e.getMessage())));
    }
    record ErrorResult(String message) {}
    private void clearHandler(Context ctx) throws DataAccessException {
        clearService.clear();
        ctx.status(200);
        ctx.result("{}");
    }
    private void registerHandler(Context ctx) throws DataAccessException {
        UserService.RegisterRequest request = gson.fromJson(ctx.body(), UserService.RegisterRequest.class);
        UserService.RegisterResult result = userService.register(request);
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }
    private void loginHandler(Context ctx) throws DataAccessException {
        UserService.LoginRequest request = gson.fromJson(ctx.body(), UserService.LoginRequest.class);
        UserService.LoginResult result = userService.login(request);
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }
    private void logoutHandler(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        userService.logout(authToken);
        ctx.status(200);
        ctx.result("{}");
    }
    private void listGamesHandler(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        GameService.ListGamesResult result = gameService.listGames(authToken);
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }
    private void createGameHandler(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        GameService.CreateGameRequest request = gson.fromJson(ctx.body(), GameService.CreateGameRequest.class);
        GameService.CreateGameResult result = gameService.createGame(authToken, request);
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }
    private void joinGameHandler(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        GameService.JoinGameRequest request = gson.fromJson(ctx.body(), GameService.JoinGameRequest.class);
        gameService.joinGame(authToken, request);
        ctx.status(200);
        ctx.result("{}");
    }
}
