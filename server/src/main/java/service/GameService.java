package service;

import server.dataaccess.*;
import model.AuthData;
import model.GameData;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import service.result.CreateGameResult;
import service.result.ListGamesResult;

/**
 * Service for handling game creation, listing, and joining.
 */

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    /**
     * Creates a new game.
     *
     * @param authToken the user's session token
     * @param req       the game name requested
     * @return the new game's ID
     * @throws DataAccessException if token is invalid or request is missing name
     */

    public CreateGameResult createGame(String authToken, CreateGameRequest req) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (req.gameName() == null || req.gameName().isEmpty()) {
            throw new DataAccessException("Error: bad request");
        }
        int gameID = gameDAO.createGame(req.gameName());
        return new CreateGameResult(gameID);
    }

    /**
     * Lists all games in the system.
     *
     * @param authToken the user's session token
     * @return a collection of all game data
     * @throws DataAccessException if token is invalid
     */

    public ListGamesResult listGames(String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return new ListGamesResult(gameDAO.listGames());
    }

    public GameData getGame(int gameID) throws server.dataaccess.DataAccessException {
        return gameDAO.getGame(gameID);
    }

    /**
     * Joins a user to a specific game.
     *
     * @param authToken the user's session token
     * @param req       the desired game and team color
     * @throws DataAccessException if token is invalid, game is full, or color/id is
     *                             invalid
     */

    public void joinGame(String authToken, JoinGameRequest req) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        GameData game = gameDAO.getGame(req.gameID());
        if (game == null) {
            throw new DataAccessException("Error: bad request");
        }
        if (req.playerColor() == null || (!req.playerColor().equals("WHITE") && !req.playerColor().equals("BLACK"))) {
            throw new DataAccessException("Error: bad request");
        }
        String whiteUser = game.whiteUsername();
        String blackUser = game.blackUsername();
        if (req.playerColor().equals("WHITE")) {
            if (whiteUser != null) {
                throw new DataAccessException("Error: already taken");
            }
            whiteUser = auth.username();
        } else if (req.playerColor().equals("BLACK")) {
            if (blackUser != null) {
                throw new DataAccessException("Error: already taken");
            }
            blackUser = auth.username();
        }
        GameData updatedGame = new GameData(game.gameID(), whiteUser, blackUser, game.gameName(), game.game());
        gameDAO.updateGame(updatedGame);
    }

    public void updateGame(GameData game) throws server.dataaccess.DataAccessException {
        gameDAO.updateGame(game);
    }
}
