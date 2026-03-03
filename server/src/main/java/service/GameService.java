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
}
