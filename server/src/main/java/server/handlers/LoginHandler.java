package server.handlers;

import com.google.gson.Gson;
import server.dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.UserService;
import service.request.LoginRequest;
import service.result.LoginResult;
import org.jetbrains.annotations.NotNull;

/**
 * Handles HTTP requests for logging in an existing user (/session).
 */
public class LoginHandler implements Handler {
    private final UserService userService;
    private final Gson gson;

    public LoginHandler(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    public void handle(@NotNull Context ctx) throws DataAccessException {
        LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);
        LoginResult result = userService.login(request);
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }
}
