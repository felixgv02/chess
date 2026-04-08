package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import io.javalin.websocket.WsConfig;
import server.dataaccess.AuthDAO;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ErrorMessage;

public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService;
    private final AuthDAO authDAO;
    private final Gson gson = new Gson();

    public WebSocketHandler(GameService gameService, AuthDAO authDAO) {
        this.gameService = gameService;
        this.authDAO = authDAO;
    }

    public void configure(WsConfig ws) {
        ws.onMessage(ctx -> {
            try {
                onMessage(ctx.session, ctx.message());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

        try {
            var auth = authDAO.getAuth(command.getAuthToken());
            if (auth == null) {
                session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: unauthorized")));
                return;
            }

            switch (command.getCommandType()) {
                case CONNECT -> connect(auth.username(), command.getGameID(), session);
                case MAKE_MOVE -> {
                    MakeMoveCommand moveCmd = gson.fromJson(message, MakeMoveCommand.class);
                    makeMove(auth.username(), moveCmd.getGameID(), moveCmd, session);
                }
                case LEAVE -> leave(auth.username(), command.getGameID(), session);
                case RESIGN -> resign(auth.username(), command.getGameID(), session);
            }
        } catch (Exception e) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: " + e.getMessage())));
        }
    }

    private void connect(String username, int gameID, Session session) throws Exception {
        connections.add(gameID, session);
        var game = gameService.getGame(gameID);

        var loadMsg = new LoadGameMessage(game);
        session.getRemote().sendString(gson.toJson(loadMsg));

        var notif = new NotificationMessage(username + " joined the game.");
        connections.broadcast(gameID, gson.toJson(notif), session);
    }

    private void leave(String username, int gameID, Session session) throws Exception {
        connections.remove(gameID, session);
        var notif = new NotificationMessage(username + " left the game.");
        connections.broadcast(gameID, gson.toJson(notif), session);

        // TODO: Check if user was white/black and securely update the GameData inside SQL DB to release their spot!
    }

    private void resign(String username, int gameID, Session session) throws Exception {
        // TODO: Validate game isn't already resigned, update game status logically.
        var notif = new NotificationMessage(username + " resigned from the game. Game over.");
        connections.broadcast(gameID, gson.toJson(notif), null);
    }

    private void makeMove(String username, int gameID, MakeMoveCommand cmd, Session session) throws Exception {
        // TODO: Validate move dynamically via gameService, update board explicitly, and save board back to SQL DB.

        var game = gameService.getGame(gameID);
        var loadMsg = new LoadGameMessage(game);
        connections.broadcast(gameID, gson.toJson(loadMsg), null);

        var notif = new NotificationMessage(username + " made a move.");
        connections.broadcast(gameID, gson.toJson(notif), session);
    }
}
