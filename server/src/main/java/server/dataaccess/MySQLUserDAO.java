package server.dataaccess;

import dataaccess.DatabaseManager;
import org.mindrot.jbcrypt.BCrypt;

import model.UserData;

public class MySQLUserDAO extends BaseDAO implements UserDAO {
    //Initializes the DAO and creates the users table if it does not exist.
    public MySQLUserDAO() throws DataAccessException {
        configureDatabase(createStatements);
    }
    //Creates a new user in the database. Hashes the password prior to saving.
    @Override
    public void createUser(UserData u) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdateNoReturn(statement, u.username(), u.password(), u.email());
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
        executeUpdateNoReturn(statement);
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


}
