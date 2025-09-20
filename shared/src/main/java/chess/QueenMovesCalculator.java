package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> legalMoves = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        for (int[] dir : directions) {
            calculateLineMoves(board, myPosition, myColor, legalMoves, dir[0], dir[1]);
        }

        return legalMoves;
    }

    private void calculateLineMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor myColor, Collection<ChessMove> legalMoves, int rowIncrement, int colIncrement) {
        int currentRow = startPosition.getRow();
        int currentCol = startPosition.getColumn();

        while (true) {
            currentRow += rowIncrement;
            currentCol += colIncrement;

            if (currentRow < 1 || currentRow > 8 || currentCol < 1 || currentCol > 8) {
                break;
            }

            ChessPosition scannedPos = new ChessPosition(currentRow, currentCol);
            ChessPiece pieceAtScannedPos = board.getPiece(scannedPos);

            if (pieceAtScannedPos == null) {
                legalMoves.add(new ChessMove(startPosition, scannedPos, null));
            } else {
                if (pieceAtScannedPos.getTeamColor() != myColor) {
                    legalMoves.add(new ChessMove(startPosition, scannedPos, null)); // Capture
                }
                break;
            }
        }
    }
}

