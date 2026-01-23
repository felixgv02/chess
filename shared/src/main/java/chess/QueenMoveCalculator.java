package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoveCalculator implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();

        int[][] directions = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        for (int[] dir : directions) {
            calculateMoves(board, position, myColor, moves, dir[0], dir[1]);
        }
        return moves;
    }

    private void calculateMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor myColor, Collection<ChessMove> moves, int row, int col) {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();

        while (true) {
            currRow += row;
            currCol += col;

            if(currRow < 1 || currRow > 8 || currCol < 1 || currCol > 8) {
                break;
            }
            ChessPosition newPosition = new ChessPosition(currRow, currCol);
            ChessPiece pieceAtPos = board.getPiece(newPosition);

            if (pieceAtPos == null) {
                moves.add(new ChessMove(startPosition, newPosition, null));
            } else {
                if(pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(startPosition, newPosition, null));
                }
                break;
            }
        }
    }
}
