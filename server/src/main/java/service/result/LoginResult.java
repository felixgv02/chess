package service.result;

/**
 * Data returned after a successful user login.
 *
 * @param username  the logged in username
 * @param authToken the authentication token for the session
 */
public record LoginResult(String username, String authToken) {
}
