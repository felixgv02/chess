package service.result;

/**
 * Data returned after a successful user registration.
 *
 * @param username  the registered username
 * @param authToken the authentication token for the session
 */
public record RegisterResult(String username, String authToken) {
}
