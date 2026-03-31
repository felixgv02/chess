package dataaccess;

import model.GameData;
import org.junit.jupiter.api.*;
import server.dataaccess.DataAccessException;
import server.dataaccess.MySQLGameDAO;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLGameDAOTest {
    private static MySQLGameDAO gameDAO;

    @BeforeAll
    static void setUp() throws DataAccessException {
        gameDAO = new MySQLGameDAO();
    }

    @BeforeEach
    void clear() throws DataAccessException {
        gameDAO.clear();
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        int gameId = gameDAO.createGame("MyAwesomeGame");
        assertTrue(gameId > 0);
    }

    @Test
    void createGameFailNullName() {
        //passing null should violate the constraint.
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    void getGameSuccess() throws DataAccessException {
        int id = gameDAO.createGame("TestGame");
        GameData game = gameDAO.getGame(id);

        assertNotNull(game);
        assertEquals("TestGame", game.gameName());
        assertNotNull(game.game()); // ensure the chess board states deserialized properly
    }

    @Test
    void getGameFailNotFound() throws DataAccessException {
        GameData game = gameDAO.getGame(9999);
        assertNull(game);
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        gameDAO.createGame("Game1");
        gameDAO.createGame("Game2");

        Collection<GameData> games = gameDAO.listGames();
        assertEquals(2, games.size());
    }

    @Test
    void listGamesEmpty() throws DataAccessException {
        Collection<GameData> games = gameDAO.listGames();
        assertTrue(games.isEmpty());
    }

    @Test
    void updateGameSuccess() throws DataAccessException {
        int id = gameDAO.createGame("TestGame");
        GameData game = gameDAO.getGame(id);

        GameData updatedGame = new GameData(id, "whitePlayer", "blackPlayer", game.gameName(), game.game());
        assertDoesNotThrow(() -> gameDAO.updateGame(updatedGame));

        GameData retrieved = gameDAO.getGame(id);
        assertEquals("whitePlayer", retrieved.whiteUsername());
        assertEquals("blackPlayer", retrieved.blackUsername());
    }

    @Test
    void updateGameFail() throws DataAccessException {
        int id = gameDAO.createGame("TestGame");
        // Attempting to update an existing game's 'NOT NULL' field to null should throw DataAccessException
        GameData badGame = new GameData(id, null, null, null, null);
        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(badGame));
    }

    @Test
    void clearGameSuccess() throws DataAccessException {
        gameDAO.createGame("TestGame");
        assertDoesNotThrow(() -> gameDAO.clear());

        Collection<GameData> games = gameDAO.listGames();
        assertTrue(games.isEmpty());
    }
}
