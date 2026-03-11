package server.handlers;

import com.google.gson.Gson;
import server.dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.GameService;
import service.result.ListGamesResult;
import org.jetbrains.annotations.NotNull;

/**
 * Handles HTTP requests for listing all games (/game).
 */
public class ListGamesHandler implements Handler {
    private final GameService gameService;
    private final Gson gson;

    public ListGamesHandler(GameService gameService, Gson gson) {
        this.gameService = gameService;
        this.gson = gson;
    }

    @Override
    public void handle(@NotNull Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        ListGamesResult result = gameService.listGames(authToken);
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }
}
