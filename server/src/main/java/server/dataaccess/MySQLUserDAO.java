package server.dataaccess;

import dataaccess.DatabaseManager;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class MySQLUserDAO {
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
}
