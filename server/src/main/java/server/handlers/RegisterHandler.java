package server.handlers;

import com.google.gson.Gson;
import server.dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.UserService;
import service.request.RegisterRequest;
import service.result.RegisterResult;
import org.jetbrains.annotations.NotNull;

/**
 * Handles HTTP requests for registering a new user (/user).
 */
public class RegisterHandler implements Handler {
    private final UserService userService;
    private final Gson gson;

    public RegisterHandler(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    public void handle(@NotNull Context ctx) throws DataAccessException {
        RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(request);
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }
}
