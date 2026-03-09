package server.dataaccess;

import model.UserData;
import java.util.HashMap;

/**
 * In-memory implementation of the UserDAO for storing user accounts.
 */
public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> users = new HashMap<>();

    /**
     * Creates a new user in the database.
     *
     * @param u the UserData to store
     * @throws DataAccessException if creation fails
     */
    @Override
    public void createUser(UserData u) throws DataAccessException {
        users.put(u.username(), u);
    }

    /**
     * Retrieves an existing user by username.
     *
     * @param username the name of the user to find
     * @return the matching UserData, or null if not found
     * @throws DataAccessException if retrieval fails
     */
    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    /**
     * Clears all users from the database. Used for testing.
     *
     * @throws DataAccessException if clearing fails
     */
    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }
}
