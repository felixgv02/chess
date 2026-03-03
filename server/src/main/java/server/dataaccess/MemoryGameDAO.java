package server.dataaccess;
import chess.ChessGame;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int nextID = 1;

    @Override
    public int createGame(String gameName) throws DataAccessException {
        int id = nextID++;
        games.put(id, new GameData(id, null, null, gameName, new ChessGame()));
        return id;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        games.put(game.gameID(), game);
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
        nextID = 1;
    }
}
