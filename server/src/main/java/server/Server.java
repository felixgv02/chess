package server;

import com.google.gson.Gson;
import server.dataaccess.*;
import server.websocket.WebSocketHandler;
import service.*;
import server.handlers.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Main HTTP Server class. Maps endpoints to Handlers.
 */
public class Server {
    private final Javalin javalin;

    private final Gson gson = new Gson();

        public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

            try {

                UserDAO userDAO = new MySQLUserDAO();
                AuthDAO authDAO = new MySQLAuthDAO();
                GameDAO gameDAO = new MySQLGameDAO();

                UserService userService = new UserService(userDAO, authDAO);
                GameService gameService = new GameService(gameDAO, authDAO);
                ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);

                WebSocketHandler webSocketHandler = new WebSocketHandler(gameService, authDAO);
                javalin.ws("/ws", webSocketHandler::configure);

                javalin.exception(DataAccessException.class, this::exceptionHandler);

                // Register handlers
                javalin.delete("/db", new ClearHandler(clearService, gson));
                javalin.post("/user", new RegisterHandler(userService, gson));
                javalin.post("/session", new LoginHandler(userService, gson));
                javalin.delete("/session", new LogoutHandler(userService));
                javalin.get("/game", new ListGamesHandler(gameService, gson));
                javalin.post("/game", new CreateGameHandler(gameService, gson));
                javalin.put("/game", new JoinGameHandler(gameService, gson));
            } catch (DataAccessException e) {
                System.err.println(("Unable to configure Database: " + e.getMessage()));
            }
        }

        public int run ( int desiredPort){
        javalin.start(desiredPort);
        return javalin.port();
    }

        public void stop () {
        javalin.stop();
    }

        /**
         * Translates thrown DataAccessExceptions into HTTP Error Responses.
         */
        private void exceptionHandler (DataAccessException e, Context ctx){
            if (e.getMessage().equals("Error: bad request")) {
                ctx.status(400);
            } else if (e.getMessage().equals("Error: unauthorized")) {
                ctx.status(401);
            } else if (e.getMessage().equals("Error: already taken")) {
                ctx.status(403);
            } else {
                ctx.status(500);
            }
            ctx.result(new Gson().toJson(new ErrorResult(e.getMessage())));
        }

        private record ErrorResult(String message) {
        }
}
