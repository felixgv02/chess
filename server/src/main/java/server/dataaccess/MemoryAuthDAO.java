package server.dataaccess;

import model.AuthData;
import java.util.HashMap;

/**
 * In-memory implementation of the AuthDAO for storing user sessions.
 */
public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> auths = new HashMap<>();

    /**
     * Creates a new authentication session.
     *
     * @param auth the AuthData to store
     * @throws DataAccessException if creation fails
     */
    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        auths.put(auth.authToken(), auth);
    }

    /**
     * Retrieves an existing authentication session by its token.
     *
     * @param authToken the unique session token
     * @return the matching AuthData, or null if not found
     * @throws DataAccessException if retrieval fails
     */
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return auths.get(authToken);
    }

    /**
     * Deletes an active session (e.g., during logout).
     *
     * @param authToken the unique session token to delete
     * @throws DataAccessException if deletion fails
     */
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        auths.remove(authToken);
    }

    /**
     * Clears all sessions from the database. Used for testing.
     *
     * @throws DataAccessException if clearing fails
     */
    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }
}
