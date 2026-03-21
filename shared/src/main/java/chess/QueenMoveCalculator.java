package chess;

import java.util.Collection;


public class QueenMoveCalculator implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new BishopMoveCalculator().pieceMoves(board, position);
        moves.addAll(new RookMoveCalculator().pieceMoves(board, position));
        return moves;
    }
}
