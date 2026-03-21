package server.dataaccess;


import dataaccess.DatabaseManager;
import model.AuthData;

//MySQL implementation of the AuthDAO for storing user sessions in a database.
public class MySQLAuthDAO extends BaseDAO implements AuthDAO {
    //Initializes the DAO and creates the auths table if it does not exist.
    public MySQLAuthDAO() throws DataAccessException {
        configureDatabase(createStatements);
    }
    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        executeUpdateNoReturn(statement, auth.authToken(), auth.username());
    }
    //Retrieves an existing authentication session by its token.
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

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE authToken=?";
        executeUpdateNoReturn(statement, authToken);
    }
    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auths";
        executeUpdateNoReturn(statement);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auths (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


}
