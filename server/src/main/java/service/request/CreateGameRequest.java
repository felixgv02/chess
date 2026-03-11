package service.request;

/**
 * Data provided by a user to create a new game.
 *
 * @param gameName the name of the new game
 */
public record CreateGameRequest(String gameName) {
}
