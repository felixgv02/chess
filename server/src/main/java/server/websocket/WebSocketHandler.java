package server.websocket;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.dataaccess.AuthDAO;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ErrorMessage;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService;
    private final AuthDAO authDAO;
    private final Gson gson = new Gson();

    public WebSocketHandler(GameService gameService, AuthDAO authDAO) {
        this.gameService = gameService;
        this.authDAO = authDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

        try {
            // Authenticate token dynamically against DB
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

}
