package service.result;

/**
 * Data returned after a successful game creation.
 *
 * @param gameID the ID of the newly created game
 */
public record CreateGameResult(int gameID) {
}
