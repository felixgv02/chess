package server.dataaccess;

import dataaccess.DatabaseManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDAO {
    protected void configureDatabase(String[] createStatements) throws DataAccessException {
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

    protected int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof String) {
                    ps.setString(i + 1, (String) params[i]);
                } else if (params[i] instanceof Integer) {
                    ps.setInt(i + 1, (Integer) params[i]);
                } else if (params[i] == null) {
                    ps.setNull(i + 1, java.sql.Types.NULL);
                }
            }
            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    protected void executeUpdateNoReturn(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof String) {
                    ps.setString(i + 1, (String) params[i]);
                } else if (params[i] instanceof Integer) {
                    ps.setInt(i + 1, (Integer) params[i]);
                } else if (params[i] == null) {
                    ps.setNull(i + 1, java.sql.Types.NULL);
                }
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}
