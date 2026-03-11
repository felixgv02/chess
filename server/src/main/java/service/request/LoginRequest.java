package service.request;

/**
 * Data provided by a user to log in to an existing account.
 *
 * @param username the username of the account
 * @param password the password for the account
 */
public record LoginRequest(String username, String password) {
}
