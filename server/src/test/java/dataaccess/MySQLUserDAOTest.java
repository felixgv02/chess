package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import server.dataaccess.DataAccessException;
import server.dataaccess.MySQLUserDAO;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLUserDAOTest {
    private static MySQLUserDAO userDAO;

    @BeforeAll
    static void setUp() throws DataAccessException {
        userDAO = new MySQLUserDAO();
    }

    @BeforeEach
    void clear() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    void createUserSuccess() throws DataAccessException {
        String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
        UserData user = new UserData("testUser", hashedPassword, "email@mail.com");
        assertDoesNotThrow(() -> userDAO.createUser(user));

        UserData retrieved = userDAO.getUser("testUser");
        assertNotNull(retrieved);
        assertEquals("testUser", retrieved.username());
        assertTrue(BCrypt.checkpw("password", retrieved.password())); // Ensure it was hashed
    }
    @Test
    void createUserFailDuplicate() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@mail.com");
        userDAO.createUser(user);

        // Cannot insert same username (Primary Key constraint violation)
        assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    void getUserSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@mail.com");
        userDAO.createUser(user);

        UserData retrieved = userDAO.getUser("testUser");
        assertNotNull(retrieved);
        assertEquals("testUser", retrieved.username());
    }

    @Test
    void getUserFailNotFound() throws DataAccessException {
        UserData retrieved = userDAO.getUser("nonExistentUser");
        assertNull(retrieved);
    }

    @Test
    void clearUserSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("testUser", "pass", "email"));
        assertDoesNotThrow(() -> userDAO.clear());
        assertNull(userDAO.getUser("testUser"));
    }
}

