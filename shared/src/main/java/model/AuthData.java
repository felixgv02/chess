package model;

/**
 * Represents an authentication session for a user.
 *
 * @param authToken the unique session token
 * @param username  the authenticated user's name
 */
public record AuthData(String authToken, String username) {
}
