package service;
import server.dataaccess.*;
import model.AuthData;
import model.GameData;
import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public record CreateGameRequest(String gameName) {}
    public record CreateGameResult(int gameID) {}

    public CreateGameResult createGame(String authToken, CreateGameRequest req) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) throw new DataAccessException("Error: unauthorized");
        if (req.gameName() == null || req.gameName().isEmpty()) throw new DataAccessException("Error: bad request");
        int gameID = gameDAO.createGame(req.gameName());
        return new CreateGameResult(gameID);
    }
    public record ListGamesResult(Collection<GameData> games) {}

    public ListGamesResult listGames(String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) throw new DataAccessException("Error: unauthorized");
        return new ListGamesResult(gameDAO.listGames());
    }
    public record JoinGameRequest(String playerColor, int gameID) {}

    public void joinGame(String authToken, JoinGameRequest req) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) throw new DataAccessException("Error: unauthorized");
        GameData game = gameDAO.getGame(req.gameID());
        if (game == null) throw new DataAccessException("Error: bad request");
        if (req.playerColor() == null || (!req.playerColor().equals("WHITE") && !req.playerColor().equals("BLACK"))) {
            throw new DataAccessException("Error: bad request");
        }
        String whiteUser = game.whiteUsername();
        String blackUser = game.blackUsername();
        if (req.playerColor().equals("WHITE")) {
            if (whiteUser != null) throw new DataAccessException("Error: already taken");
            whiteUser = auth.username();
        } else if (req.playerColor().equals("BLACK")) {
            if (blackUser != null) throw new DataAccessException("Error: already taken");
            blackUser = auth.username();
        }
        GameData updatedGame = new GameData(game.gameID(), whiteUser, blackUser, game.gameName(), game.game());
        gameDAO.updateGame(updatedGame);
    }
}
