package service.request;

/**
 * Data provided by a user to join an existing game.
 *
 * @param playerColor the chosen team color ("WHITE" or "BLACK")
 * @param gameID      the ID of the game to join
 */
public record JoinGameRequest(String playerColor, int gameID) {
}
