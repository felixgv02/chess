package server.handlers;

import com.google.gson.Gson;
import server.dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.GameService;
import service.request.CreateGameRequest;
import service.result.CreateGameResult;
import org.jetbrains.annotations.NotNull;

/**
 * Handles HTTP requests for creating a new game (/game).
 */
public class CreateGameHandler implements Handler {
    private final GameService gameService;
    private final Gson gson;

    public CreateGameHandler(GameService gameService, Gson gson) {
        this.gameService = gameService;
        this.gson = gson;
    }

    @Override
    public void handle(@NotNull Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameResult result = gameService.createGame(authToken, request);
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }
}
