package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import io.javalin.websocket.WsConfig;
import server.dataaccess.AuthDAO;
import service.GameService;
import model.GameData;
import chess.ChessGame;
import chess.ChessPiece;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ErrorMessage;

import java.util.Objects;

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
        ws.onConnect(ctx -> {
            ctx.session.setIdleTimeout(java.time.Duration.ofMinutes(10));
        });
        ws.onClose(ctx -> {
            System.out.println("SERVER WEBSOCKET CLOSED: " + ctx.status() + " -> " + ctx.reason());
        });
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
        GameData gameData = null;
        try {
            gameData = gameService.getGame(gameID);
        } catch (Exception ignored) {}

        if (gameData == null) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: Invalid game ID")));
            return;
        }

        connections.add(gameID, session);
        var loadMsg = new LoadGameMessage(gameData);
        session.getRemote().sendString(gson.toJson(loadMsg));

        var notif = new NotificationMessage(username + " joined the game.");
        connections.broadcast(gameID, gson.toJson(notif), session);
    }

    private void leave(String username, int gameID, Session session) throws Exception {
        connections.remove(gameID, session);

        GameData gameData = gameService.getGame(gameID);
        if (gameData != null) {
            // Un-claim the player's spot if they held one
            String newWhite = Objects.equals(gameData.whiteUsername(), username) ? null : gameData.whiteUsername();
            String newBlack = Objects.equals(gameData.blackUsername(), username) ? null : gameData.blackUsername();

            if (!Objects.equals(gameData.whiteUsername(), newWhite) || !Objects.equals(gameData.blackUsername(), newBlack)) {
                GameData updatedGame = new GameData(gameID, newWhite, newBlack, gameData.gameName(), gameData.game());
                gameService.updateGame(updatedGame);
            }
        }

        var notif = new NotificationMessage(username + " left the game.");
        connections.broadcast(gameID, gson.toJson(notif), session);
    }

    private void resign(String username, int gameID, Session session) throws Exception {
        GameData gameData = gameService.getGame(gameID);
        if (gameData == null) {
            return;
        }

        boolean isPlayer = Objects.equals(username, gameData.whiteUsername()) || Objects.equals(username, gameData.blackUsername());

        if (!isPlayer) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: observers cannot resign")));
            return;
        }

        if (gameData.game().isGameOver()) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: game is already over")));
            return;
        }

        gameData.game().setGameOver(true);
        gameService.updateGame(gameData);

        var notif = new NotificationMessage(username + " resigned from the game. Game over.");
        connections.broadcast(gameID, gson.toJson(notif), null);
    }

    private void makeMove(String username, int gameID, MakeMoveCommand cmd, Session session) throws Exception {
        GameData gameData = gameService.getGame(gameID);
        if (gameData == null) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: Invalid game ID")));
            return;
        }

        ChessGame game = gameData.game();

        if (game.isGameOver()) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: Game is already over")));
            return;
        }

        String playerColor = null;
        if (Objects.equals(username, gameData.whiteUsername())) {
            playerColor = "WHITE";
        } else if (Objects.equals(username, gameData.blackUsername())) {
            playerColor = "BLACK";
        }

        if (playerColor == null) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: Observers cannot move pieces")));
            return;
        }

        ChessPiece piece = game.getBoard().getPiece(cmd.getMove().getStartPosition());
        if (piece == null || !piece.getTeamColor().toString().equals(playerColor)) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: It's not your piece to move")));
            return;
        }

        if (!game.getTeamTurn().toString().equals(playerColor)) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: It's not your turn")));
            return;
        }

        try {
            game.makeMove(cmd.getMove());
        } catch (Exception e) {
            session.getRemote().sendString(gson.toJson(new ErrorMessage("Error: Invalid move")));
            return;
        }

        // Check for edge cases
        if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK) ||
                game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
            game.setGameOver(true);
        }

        gameService.updateGame(gameData);

        var loadMsg = new LoadGameMessage(gameData);
        connections.broadcast(gameID, gson.toJson(loadMsg), null);

        var notif = new NotificationMessage(username + " made a move.");
        connections.broadcast(gameID, gson.toJson(notif), session);

        // Send check/checkmate alerts securely across clients
        if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            connections.broadcast(gameID, gson.toJson(new NotificationMessage("Black is in checkmate!")), null);
        } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            connections.broadcast(gameID, gson.toJson(new NotificationMessage("Black is in check!")), null);
        }

        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            connections.broadcast(gameID, gson.toJson(new NotificationMessage("White is in checkmate!")), null);
        } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
            connections.broadcast(gameID, gson.toJson(new NotificationMessage("White is in check!")), null);
        }

        if (game.isInStalemate(ChessGame.TeamColor.BLACK) || game.isInStalemate(ChessGame.TeamColor.WHITE)) {
            connections.broadcast(gameID, gson.toJson(new NotificationMessage("Stalemate!")), null);
        }
    }
}
