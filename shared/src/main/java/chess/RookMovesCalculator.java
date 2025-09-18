package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = new ArrayList<>();

        calculateLineMoves (board, myPosition, legalMoves, 1, 0); //UP
        calculateLineMoves (board, myPosition, legalMoves, -1, 0); //DOWN
        calculateLineMoves (board, myPosition, legalMoves, 0, 1); //RIGHT
        calculateLineMoves (board, myPosition, legalMoves, 0, -1); //LEFT

        return legalMoves;
    }

    private void calculateLineMoves (ChessBoard board, ChessPosition myPosition, Collection<ChessMove> legalMoves, int rowIncrement, int colIncrement) {
        ChessPiece startingPiece = board.getPiece(myPosition);
        if (startingPiece == null) return;
        ChessGame.TeamColor myColor = startingPiece.getTeamColor();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        while (true) {
            currentRow += rowIncrement;
            currentCol += colIncrement;

            if (currentRow < 1 || currentCol < 1 || currentRow > 8 || currentCol > 8) {
                break;
            }

            ChessPosition scannedPos = new ChessPosition(currentRow, currentCol);
            ChessPiece pieceAtScannedPos = board.getPiece(scannedPos);

            if (pieceAtScannedPos == null) {
                legalMoves.add(new ChessMove(myPosition, scannedPos, null));
            } else {
                if (pieceAtScannedPos.getTeamColor() != myColor) {
                    legalMoves.add(new ChessMove(myPosition, scannedPos, null));
                }
                break;
            }
        }
    }
}
