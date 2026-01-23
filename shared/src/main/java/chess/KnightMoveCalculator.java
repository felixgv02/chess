package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMoveCalculator implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();
        int startRow = position.getRow();
        int startCol = position.getColumn();

        int[][] posMoves = {
                {2,1}, {2,-1}, {-2,1}, {-2,-1},
                {1,2}, {1,-2}, {-1,2}, {-1,-2}
        };

        for (int[] move : posMoves) {
            int endRow = startRow + move[0];
            int endCol = startCol + move[1];

            if(endRow >= 1 && endRow <= 8 && endCol >= 1 && endCol <= 8) {
                ChessPosition endPosition = new ChessPosition(endRow, endCol);
                ChessPiece endPiece = board.getPiece(endPosition);

                if(endPiece == null || endPiece.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
            }
        }
        return moves;
    }
}
