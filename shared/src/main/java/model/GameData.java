package model;

import chess.ChessGame;

/**
 * Represents a single chess game.
 * Note: gameID is kept capitalized to match the required API JSON
 * specifications.
 *
 * @param gameID        the unique identifier for the game
 * @param whiteUsername the username of the white player
 * @param blackUsername the username of the black player
 * @param gameName      the user-defined name for the game
 * @param game          the actual internally-tracked ChessGame state
 */
public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
