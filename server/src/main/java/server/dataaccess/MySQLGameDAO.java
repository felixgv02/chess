package server.dataaccess;

import dataaccess.DatabaseManager;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySQLGameDAO extends BaseDAO implements GameDAO{
    /**
     * Initializes the DAO and creates the games table if it does not exist.
     */
    public MySQLGameDAO() throws DataAccessException {
        configureDatabase(createStatements);
    }
    /**
     * Creates a new game and stores it. Serializes the ChessGame to JSON.
     */
    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        var json = new Gson().toJson(new ChessGame());
        return executeUpdate(statement, gameName, json);
    }

    //Retrieves a game by its ID. Deserializes the JSON back into a ChessGame.
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    //Lists all games currently stored.
    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    //Updates an existing game with new data (e.g., a new player joined).
    @Override
    public void updateGame(GameData game) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
        var json = new Gson().toJson(game.game());
        executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), json, game.gameID());
    }

    //Clears all games from the database. Used for testing.
    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        executeUpdateNoReturn(statement);
    }

    //Centralized helper mapping a ResultSet to a GameData object (Code Decomposition).
    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("game");
        var game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    //Helper method to execute updates with no returned keys (e.g., TRUNCATE).

    //Database table creation statements.
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


}
