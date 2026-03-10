package dataaccess;

import model.AuthData;
import server.dataaccess.DataAccessException;

import java.sql.SQLException;

public class MySQLAuthDAO implements AuthDAO {
    public MySqlAuthDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, auth.authToken(), auth.username());
    }
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auths WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }
}
