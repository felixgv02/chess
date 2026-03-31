package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.dataaccess.DataAccessException;
import server.dataaccess.MySQLAuthDAO;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLAuthDAOTest {
    private static MySQLAuthDAO authDAO;

    @BeforeAll
    static void setUp() throws DataAccessException {
        authDAO = new MySQLAuthDAO();
    }

    @BeforeEach
    void clear() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    void createAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("testToken", "testUser");
        assertDoesNotThrow(() -> authDAO.createAuth(auth));

        AuthData retrieved = authDAO.getAuth("testToken");
        assertNotNull(retrieved);
        assertEquals("testUser", retrieved.username());
    }

    @Test
    void createAuthFailDuplicate() throws DataAccessException {
        AuthData auth = new AuthData("testToken", "testUser");
        authDAO.createAuth(auth);

        // Attempting to insert the exact same authToken (Primary Key) should fail
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(auth));
    }

    @Test
    void getAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("testToken", "testUser");
        authDAO.createAuth(auth);

        AuthData retrieved = authDAO.getAuth("testToken");
        assertNotNull(retrieved);
        assertEquals("testToken", retrieved.authToken());
    }

    @Test
    void getAuthFailNotFound() throws DataAccessException {
        AuthData retrieved = authDAO.getAuth("nonExistentToken");
        assertNull(retrieved); // Should return null when not found
    }

    @Test
    void deleteAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("testToken", "testUser");
        authDAO.createAuth(auth);

        assertDoesNotThrow(() -> authDAO.deleteAuth("testToken"));
        assertNull(authDAO.getAuth("testToken"));
    }

    @Test
    void deleteAuthFail() {
        // Attempting to delete something that doesn't exist.
        // SQL DELETE doesn't throw on 0 rows affected natively, but ensure it doesn't break.
        assertDoesNotThrow(() -> authDAO.deleteAuth("nonexistentToken"));
    }

    @Test
    void clearAuthSuccess() throws DataAccessException {
        authDAO.createAuth(new AuthData("testToken", "testUser"));
        assertDoesNotThrow(() -> authDAO.clear());
        assertNull(authDAO.getAuth("testToken"));
    }
}

