package model;

/**
 * Core data representing a registered user.
 *
 * @param username the user's chosen name
 * @param password the user's password
 * @param email    the user's email address
 */
public record UserData(String username, String password, String email) {
}
