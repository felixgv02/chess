package service;

import server.dataaccess.*;
import model.UserData;
import model.AuthData;
import service.request.LoginRequest;
import service.request.RegisterRequest;
import service.result.LoginResult;
import service.result.RegisterResult;
import java.util.UUID;

/**
 * Service for handling user registration, authentication, and logout.
 */

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    /**
     * Registers a new user.
     *
     * @param req the user details
     * @return the username and session token
     * @throws DataAccessException if fields are missing or user exists
     */
    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        if (req.username() == null || req.password() == null || req.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        if (userDAO.getUser(req.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        userDAO.createUser(new UserData(req.username(), req.password(), req.email()));
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, req.username()));
        return new RegisterResult(req.username(), authToken);
    }

    /**
     * Authenticates an existing user.
     *
     * @param req the login details
     * @return the username and session token
     * @throws DataAccessException if fields are missing or incorrect
     */
    public LoginResult login(LoginRequest req) throws DataAccessException {
        if (req.username() == null || req.password() == null) {
            throw new DataAccessException("Error: bad request");
        }

        UserData user = userDAO.getUser(req.username());
        if (user == null || !user.password().equals(req.password())) {
            throw new DataAccessException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, req.username()));
        return new LoginResult(req.username(), authToken);
    }

    /**
     * Terminates a user session.
     *
     * @param authToken the active session token
     * @throws DataAccessException if auth token is invalid
     */
    public void logout(String authToken) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}
