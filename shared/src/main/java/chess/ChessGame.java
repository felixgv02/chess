package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

        private ChessBoard board;
        private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }

        HashSet<ChessMove> legalMoves = new HashSet<>();
        Collection<ChessMove> pseudoLegalMoves = piece.pieceMoves(board, startPosition);

        for (ChessMove move : pseudoLegalMoves) {
            ChessBoard tempBoard = board.deepCoopy();
            applyMove(move, tempBoard);

            if(!isInCheck(piece.getTeamColor(), tempBoard)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * #@throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("Invalid move: No piece at starting position.");
        }
        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException(String.format("Invalid move. It is currently %s's turn.", getTeamTurn()));
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move: The proposed move is not legal.");
        }

        applyMove(move, this.board);
        setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, this.board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return hasNoValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return hasNoValidMoves (teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private boolean isInCheck(TeamColor teamColor, ChessBoard currentBoard) {
        ChessPosition kingPosition = findKingPosition (teamColor, currentBoard);
        if (kingPosition == null) return false;
        TeamColor opponentColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        for (int r = 1;r <= 8; r++) {
            for (int c = 1;c <= 8;c++) {
                ChessPiece piece = currentBoard.getPiece(new ChessPosition(r, c));
                if (piece != null && piece.getTeamColor() == opponentColor) {
                    for (ChessMove move : piece.pieceMoves(currentBoard, new ChessPosition(r, c))) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition findKingPosition(TeamColor teamColor, ChessBoard currentBoard) {
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPiece piece = currentBoard.getPiece(new ChessPosition(r, c));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return new ChessPosition(r, c);
                }
            }
        }
        return null;
    }

    private void applyMove(ChessMove move, ChessBoard boardToChange) {
        ChessPiece piece = boardToChange.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() != null) {
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        boardToChange.addPiece(move.getEndPosition(), piece);
        boardToChange.addPiece(move.getStartPosition(), null);
    }

    private boolean hasNoValidMoves(TeamColor teamColor) {
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPiece piece = board.getPiece(new ChessPosition(r, c));
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(new ChessPosition(r, c)).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return board.equals(chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}


