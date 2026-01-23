package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveCalculator implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();

        if(myColor == ChessGame.TeamColor.WHITE) {
            handleWhitePawnMoves(board, position, moves);
        } else {
            handelBlackPawnMoves(board, position, moves);
        }
        return moves;
    }

    private void handleWhitePawnMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int startRow = position.getRow();
        int startCol = position.getColumn();

        boolean isStartingRow = (startRow == 2);
        boolean isPromotionRow = (startRow == 7);

        ChessPosition oneStepForward = new ChessPosition(startRow + 1, startCol);
        if(board.getPiece(oneStepForward) == null) {
            if(isPromotionRow) {
                addPromotionMoves(position, oneStepForward, moves);
            } else {
                moves.add(new ChessMove(position, oneStepForward, null));
            }
            if(isStartingRow) {
                ChessPosition twoStepsForward = new ChessPosition(startRow + 2, startCol);
                if(board.getPiece(twoStepsForward) == null) {
                    moves.add(new ChessMove(position, twoStepsForward, null));
                }
            }
        }
        checkWhitePawnCaptures(board, position, moves, isPromotionRow);
    }

    private void checkWhitePawnCaptures(ChessBoard board, ChessPosition position,Collection<ChessMove> moves,boolean isPromotionRow) {
        int[] captureCols = {position.getColumn() - 1, position.getColumn() + 1};
        for (int col: captureCols) {
            if(col >= 1 && col <= 8) {
                ChessPosition capturesPos = new ChessPosition(position.getRow() + 1, col);
                ChessPiece pieceAtCapture = board.getPiece(capturesPos);
                if(pieceAtCapture != null && pieceAtCapture.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if(isPromotionRow) {
                        addPromotionMoves(position, capturesPos, moves);
                    } else {
                        moves.add(new ChessMove(position, capturesPos, null));
                    }
                }
            }
        }
    }

    private void handelBlackPawnMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int[] capturesCols = {position.getColumn() - 1, position.getColumn() + 1};
        int startRow = position.getRow();
        int startCol = position.getColumn();

        boolean isStartingRow = (startRow == 7);
        boolean isPromotionRow = (startRow == 2);

        ChessPosition oneStepForward = new ChessPosition(startRow - 1, startCol);
        if(board.getPiece(oneStepForward) == null) {
            if(isPromotionRow) {
                addPromotionMoves(position, oneStepForward, moves);
            } else {
                moves.add(new ChessMove(position, oneStepForward, null));
            }
            if(isStartingRow) {
                ChessPosition twoStepsForward = new ChessPosition(startRow - 2, startCol);
                if(board.getPiece(twoStepsForward) == null) {
                    moves.add(new ChessMove(position, twoStepsForward, null));
                }
            }
        }
        checkBlackPawnCaptures(board, position, moves, isPromotionRow);
    }

    private void checkBlackPawnCaptures(ChessBoard board, ChessPosition position, Collection<ChessMove> moves, boolean isPromotionRow) {
        int[] captureCols = {position.getColumn() - 1, position.getColumn() + 1};
        for(int col: captureCols) {
            if(col >= 1 && col <= 8) {
                ChessPosition capturePos = new ChessPosition(position.getRow() -1, col);
                ChessPiece pieceAtCapture = board.getPiece(capturePos);
                if(pieceAtCapture != null && pieceAtCapture.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if(isPromotionRow) {
                        addPromotionMoves(position, capturePos, moves);
                    } else {
                        moves.add(new ChessMove(position, capturePos, null));
                    }
                }
            }
        }
    }

    private void addPromotionMoves(ChessPosition start, ChessPosition end, Collection<ChessMove> moves) {
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
    }
}
