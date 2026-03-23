package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import static ui.EscapeSequences.*;

public class ChessBoardPrinter {

    public static void printBoard(ChessBoard board, boolean whitePerspective) {
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);

        // Print header letters
        printHeader(whitePerspective);

        int startRow = whitePerspective ? 8 : 1;
        int endRow = whitePerspective ? 1 : 8;
        int rowStep = whitePerspective ? -1 : 1;

        for (int row = startRow; row != endRow + rowStep; row += rowStep) {
            // Print left number
            System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_WHITE + " " + row + " ");

            int startCol = whitePerspective ? 1 : 8;
            int endCol = whitePerspective ? 8 : 1;
            int colStep = whitePerspective ? 1 : -1;

            for (int col = startCol; col != endCol + colStep; col += colStep) {
                boolean isLightSquare = (row + col) % 2 != 0;
                if (isLightSquare) {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    System.out.print(SET_BG_COLOR_BLACK);
                }

                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                printPiece(piece);
            }

            // Print right number
            System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_WHITE + " " + row + " " + RESET_BG_COLOR + "\n");
        }

        // Print footer letters
        printHeader(whitePerspective);
        System.out.println();
    }

    private static void printHeader(boolean whitePerspective) {
        System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_WHITE + "   ");
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        if (!whitePerspective) {
            for (int i = 0; i < letters.length / 2; i++) {
                String temp = letters[i];
                letters[i] = letters[letters.length - 1 - i];
                letters[letters.length - 1 - i] = temp;
            }
        }
        for (String letter : letters) {
            System.out.print(" " + letter + " \u2003");
        }
        System.out.println("   " + RESET_BG_COLOR);
    }

    private static void printPiece(ChessPiece piece) {
        if (piece == null) {
            System.out.print(EMPTY);
            return;
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            System.out.print(SET_TEXT_COLOR_RED);
            switch (piece.getPieceType()) {
                case KING -> System.out.print(WHITE_KING);
                case QUEEN -> System.out.print(WHITE_QUEEN);
                case BISHOP -> System.out.print(WHITE_BISHOP);
                case KNIGHT -> System.out.print(WHITE_KNIGHT);
                case ROOK -> System.out.print(WHITE_ROOK);
                case PAWN -> System.out.print(WHITE_PAWN);
            }
        } else {
            System.out.print(SET_TEXT_COLOR_BLUE);
            switch (piece.getPieceType()) {
                case KING -> System.out.print(BLACK_KING);
                case QUEEN -> System.out.print(BLACK_QUEEN);
                case BISHOP -> System.out.print(BLACK_BISHOP);
                case KNIGHT -> System.out.print(BLACK_KNIGHT);
                case ROOK -> System.out.print(BLACK_ROOK);
                case PAWN -> System.out.print(BLACK_PAWN);
            }
        }
    }
}
