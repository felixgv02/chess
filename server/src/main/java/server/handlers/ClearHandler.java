package server.handlers;

import com.google.gson.Gson;
import server.dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearService;
import org.jetbrains.annotations.NotNull;

/**
 * Handles HTTP requests for clearing the database (/db).
 */
public class ClearHandler implements Handler {
    private final ClearService clearService;

    public ClearHandler(ClearService clearService, Gson gson) {
        this.clearService = clearService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws DataAccessException {
        clearService.clear();
        ctx.status(200);
        ctx.result("{}");
    }
}
