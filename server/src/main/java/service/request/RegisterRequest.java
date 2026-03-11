package service.request;

/**
 * Data provided by a user to register a new account.
 *
 * @param username the desired username
 * @param password the desired password
 * @param email    the desired email address
 */
public record RegisterRequest(String username, String password, String email) {
}
