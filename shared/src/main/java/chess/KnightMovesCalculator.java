package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> legalMoves = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();

        int[][] moves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] move : moves) {
            int endRow = startRow + move[0];
            int endCol = startCol + move[1];

            if (endRow >= 1 && endRow <= 8 && endCol >= 1 && endCol <= 8) {
                ChessPosition endPosition = new ChessPosition(endRow, endCol);
                ChessPiece pieceAtEnd = board.getPiece(endPosition);

                if (pieceAtEnd == null || pieceAtEnd.getTeamColor() != myColor) {
                    legalMoves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
        return legalMoves;
    }
}

