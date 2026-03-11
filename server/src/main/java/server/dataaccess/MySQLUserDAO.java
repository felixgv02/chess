package server.dataaccess;

import dataaccess.DatabaseManager;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import model.UserData;

public class MySQLUserDAO  implements UserDAO {
    //Initializes the DAO and creates the users table if it does not exist.
    public MySQLUserDAO() throws DataAccessException {
        configureDatabase();
    }
    //Creates a new user in the database. Hashes the password prior to saving.
    @Override
    public void createUser(UserData u) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(u.password(), BCrypt.gensalt());
        executeUpdate(statement, u.username(), hashedPassword, u.email());
    }

    //Retrieves an existing user by username.
    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    //Clears all users from the database. Used for testing.
    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE users";
        executeUpdate(statement);
    }

    //Helper method to execute updates, centralized to avoid duplicated code (Code Decomposition).
    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof String) ps.setString(i + 1, (String) params[i]);
                else if (params[i] instanceof Integer) ps.setInt(i + 1, (Integer) params[i]);
                else if (params[i] == null) ps.setNull(i + 1, java.sql.Types.NULL);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    //Database table creation statements.
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    //Configures the database and creates necessary tables automatically.
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }
}
