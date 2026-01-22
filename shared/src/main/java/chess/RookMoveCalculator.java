package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookMoveCalculator implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        CalculateMoves(board, position, moves, 1, 0);
        CalculateMoves(board, position, moves, -1, 0);
        CalculateMoves(board, position, moves, 0, -1);
        CalculateMoves(board, position, moves, 0, 1);

        return moves;
    }

    private void CalculateMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves, int row, int col) {
        ChessPiece startingPiece = board.getPiece(position);
        if(startingPiece == null) return;
        ChessGame.TeamColor  myColor = startingPiece.getTeamColor();

        int currRow = position.getRow();
        int currCol = position.getColumn();

        while(true) {
            currRow += row;
            currCol += col;

            if(currRow < 1 || currCol < 1 || currRow > 8 || currCol > 8) {
                break;
            }
            ChessPosition newPosition = new ChessPosition(currRow, currCol);
            ChessPiece pieceAtPos = board.getPiece(newPosition);

            if(pieceAtPos == null) {
                moves.add(new ChessMove(position, newPosition, null));
            } else {
                if(pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, newPosition, null));
                }
                break;
            }
        }
    }
}
