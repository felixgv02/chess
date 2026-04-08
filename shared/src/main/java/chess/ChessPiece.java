package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    private final transient PieceMoveCalculator moveCalculator;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;

        switch(type) {
            case PAWN:
                this.moveCalculator = new PawnMoveCalculator();
                break;
            case ROOK:
                this.moveCalculator = new RookMoveCalculator();
                break;
            case KNIGHT:
                this.moveCalculator = new KnightMoveCalculator();
                break;
            case BISHOP:
                this.moveCalculator = new BishopMoveCalculator();
                break;
            case QUEEN:
                this.moveCalculator = new QueenMoveCalculator();
                break;
            case KING:
                this.moveCalculator = new KingMoveCalculator();
                break;
            default:
                this.moveCalculator = null;
                break;
        }
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return  type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (moveCalculator == null) {
            // Gson bypassed the constructor! Re-attach it on the fly:
            PieceMoveCalculator calc = null;
            switch(type) {
                case PAWN -> calc = new PawnMoveCalculator();
                case ROOK -> calc = new RookMoveCalculator();
                case KNIGHT -> calc = new KnightMoveCalculator();
                case BISHOP -> calc = new BishopMoveCalculator();
                case QUEEN -> calc = new QueenMoveCalculator();
                case KING -> calc = new KingMoveCalculator();
            }
            return calc.pieceMoves(board, myPosition);
        }
        return moveCalculator.pieceMoves(board, myPosition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece chessPiece = (ChessPiece) o;
        return pieceColor == chessPiece.pieceColor && type == chessPiece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
