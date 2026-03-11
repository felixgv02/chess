package server.handlers;

import com.google.gson.Gson;
import server.dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.GameService;
import service.request.JoinGameRequest;
import org.jetbrains.annotations.NotNull;

/**
 * Handles HTTP requests for joining an existing string (/game).
 */
public class JoinGameHandler implements Handler {
    private final GameService gameService;
    private final Gson gson;

    public JoinGameHandler(GameService gameService, Gson gson) {
        this.gameService = gameService;
        this.gson = gson;
    }

    @Override
    public void handle(@NotNull Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        JoinGameRequest request = gson.fromJson(ctx.body(), JoinGameRequest.class);
        gameService.joinGame(authToken, request);
        ctx.status(200);
        ctx.result("{}");
    }
}
