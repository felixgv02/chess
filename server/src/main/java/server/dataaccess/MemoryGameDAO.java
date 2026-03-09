package server.dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;

/**
 * In-memory implementation of the GameDAO for storing game data in a HashMap.
 */
public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, GameData> games = new HashMap<>();
    private int nextId = 1;

    /**
     * Creates a new game and stores it.
     *
     * @param gameName the name of the new game
     * @return the new game's ID
     * @throws DataAccessException if creation fails
     */
    @Override
    public int createGame(String gameName) throws DataAccessException {
        int id = nextId++;
        games.put(id, new GameData(id, null, null, gameName, new ChessGame()));
        return id;
    }

    /**
     * Retrieves a game by its ID.
     *
     * @param gameID the ID of the game to retrieve
     * @return the matching GameData, or null if not found
     * @throws DataAccessException if retrieval fails
     */
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    /**
     * Lists all games currently stored.
     *
     * @return a collection of all GameData objects
     * @throws DataAccessException if retrieval fails
     */
    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    /**
     * Updates an existing game with new data (e.g., a new player joined).
     *
     * @param game the updated GameData object
     * @throws DataAccessException if the update fails
     */
    @Override
    public void updateGame(GameData game) throws DataAccessException {
        games.put(game.gameID(), game);
    }

    /**
     * Clears all games from the database. Used for testing.
     *
     * @throws DataAccessException if clearing fails
     */
    @Override
    public void clear() throws DataAccessException {
        games.clear();
        nextId = 1;
    }
}
