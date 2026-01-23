package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMoveCalculator implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();
        int startRow = position.getRow();
        int startCol = position.getColumn();

        int[][] directions = {
                {1,0}, {-1,0}, {0,1}, {0, -1}
                ,{1,1}, {1,-1}, {-1,1}, {-1,-1}
        };

        for(int[] dir: directions) {
            int endRow = startRow + dir[0];
            int endCol = startCol + dir[1];

            if(endRow >= 1 && endRow <= 8 && endCol >= 1 && endCol <= 8) {
                ChessPosition endPosition = new ChessPosition(endRow, endCol);
                ChessPiece pieceAtPos = board.getPiece(endPosition);

                if(pieceAtPos == null || pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
            }
        }
        return moves;
    }
}
