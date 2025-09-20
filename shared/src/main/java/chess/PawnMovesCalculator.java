package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> legalMoves = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();

        if (myColor == ChessGame.TeamColor.WHITE) {
            handleWhitePawnMoves(board, myPosition, legalMoves);
        } else { // BLACK
            handleBlackPawnMoves(board, myPosition, legalMoves);
        }

        return legalMoves;
    }

    private void handleWhitePawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> legalMoves) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        boolean isStartingRow = (startRow == 2);
        boolean isPromotionRow = (startRow == 7);

        ChessPosition oneStepForward = new ChessPosition(startRow + 1, startCol);
        if (board.getPiece(oneStepForward) == null) {
            if (isPromotionRow) {
                addPromotionMoves(myPosition, oneStepForward, legalMoves);
            } else {
                legalMoves.add(new ChessMove(myPosition, oneStepForward, null));
            }

            if (isStartingRow) {
                ChessPosition twoStepsForward = new ChessPosition(startRow + 2, startCol);
                if (board.getPiece(twoStepsForward) == null) {
                    legalMoves.add(new ChessMove(myPosition, twoStepsForward, null));
                }
            }
        }

        checkWhitePawnCaptures(board, myPosition, legalMoves, isPromotionRow);
    }

    private void checkWhitePawnCaptures(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> legalMoves, boolean isPromotionRow) {
        int[] captureCols = {myPosition.getColumn() - 1, myPosition.getColumn() + 1};
        for (int col : captureCols) {
            if (col >= 1 && col <= 8) {
                ChessPosition capturePos = new ChessPosition(myPosition.getRow() + 1, col);
                ChessPiece pieceAtCapture = board.getPiece(capturePos);
                if (pieceAtCapture != null && pieceAtCapture.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (isPromotionRow) {
                        addPromotionMoves(myPosition, capturePos, legalMoves);
                    } else {
                        legalMoves.add(new ChessMove(myPosition, capturePos, null));
                    }
                }
            }
        }
    }


    private void handleBlackPawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> legalMoves) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        boolean isStartingRow = (startRow == 7);
        boolean isPromotionRow = (startRow == 2);

        ChessPosition oneStepForward = new ChessPosition(startRow - 1, startCol);
        if (board.getPiece(oneStepForward) == null) {
            if (isPromotionRow) {
                addPromotionMoves(myPosition, oneStepForward, legalMoves);
            } else {
                legalMoves.add(new ChessMove(myPosition, oneStepForward, null));
            }

            if (isStartingRow) {
                ChessPosition twoStepsForward = new ChessPosition(startRow - 2, startCol);
                if (board.getPiece(twoStepsForward) == null) {
                    legalMoves.add(new ChessMove(myPosition, twoStepsForward, null));
                }
            }
        }

        checkBlackPawnCaptures(board, myPosition, legalMoves, isPromotionRow);
    }

    private void checkBlackPawnCaptures(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> legalMoves, boolean isPromotionRow) {
        int[] captureCols = {myPosition.getColumn() - 1, myPosition.getColumn() + 1};
        for (int col : captureCols) {
            if (col >= 1 && col <= 8) {
                ChessPosition capturePos = new ChessPosition(myPosition.getRow() - 1, col);
                ChessPiece pieceAtCapture = board.getPiece(capturePos);
                if (pieceAtCapture != null && pieceAtCapture.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (isPromotionRow) {
                        addPromotionMoves(myPosition, capturePos, legalMoves);
                    } else {
                        legalMoves.add(new ChessMove(myPosition, capturePos, null));
                    }
                }
            }
        }
    }

    private void addPromotionMoves(ChessPosition start, ChessPosition end, Collection<ChessMove> moves) {
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
    }
}

