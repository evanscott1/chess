package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static ui.EscapeSequences.*;

public class ChessBoardMaker {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;

    // Padded characters.
    private static final String EMPTY = "   ";

    private static SquareType startSquareType;

//    private static final GameData gameData = null;


    public static void boardMaker(GameData gameData, String teamColor) {
        ChessBoard board = gameData.game().getBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        boolean isWhite = Objects.equals(teamColor, "WHITE");

        ArrayList<String> squareCharacters = new ArrayList<>();
        out.print(ERASE_SCREEN);


        ArrayList<String> headers = new ArrayList<>(List.of(" ", "a", "b", "c", "d", "e", "f", "g", "h", " "));


        /*
        Header
         */
        printHeader(out, headers, isWhite);

        /*
        Body
         */
        startSquareType = SquareType.LIGHT;

        printBody(out, board, isWhite);


        /*
        Footer
         */
        printHeader(out, headers, isWhite);


        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println();
        out.println();
    }

    private static void printBody(PrintStream out, ChessBoard board, boolean isWhite) {

        SquareType squareType;

        int startRow = isWhite ? board.getBoardWidth() : 1;
        int endRow = isWhite ? 0 : board.getBoardWidth() + 1;
        int stepRow = isWhite ? -1 : 1;

        int startCol = isWhite ? 1 : board.getBoardWidth();
        int endCol = isWhite ? board.getBoardWidth() + 1:  0;
        int stepCol = isWhite ? 1 : -1;

        for (int i = startRow; i != endRow; i += stepRow) {

            /*
            Row: Part 1
             */
            printBoardLine(out, startSquareType);

            /*
            Row: Part 2
             */
            squareType = startSquareType;

            setBorder(out);
            printSquare(out, Integer.toString(i));

            for (int j = startCol; j != endCol; j += stepCol) {
                String character;
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (squareType == SquareType.LIGHT) {
                    setLightBackground(out);
                    squareType = SquareType.DARK;
                } else {
                    setDarkBackground(out);
                    squareType = SquareType.LIGHT;
                }


                if (piece == null) {
                    character = " ";
                    printSquare(out, character);
                } else {
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        setLightTeam(out);
                    } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        setDarkTeam(out);
                    }
                    if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                        character = "N";
                    } else {
                        character = String.valueOf(piece.getPieceType().name().charAt(0));
                    }
                    printSquare(out, character);
                }


            }


            setBorder(out);
            printSquare(out, Integer.toString(i));
            printNewLine(out);

            printBoardLine(out, startSquareType);
            if (startSquareType == SquareType.LIGHT) {
                setLightBackground(out);
                startSquareType = SquareType.DARK;
            } else {
                setDarkBackground(out);
                startSquareType = SquareType.LIGHT;
            }



        }
    }



    private static void printSquare(PrintStream out, String character) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        out.print(character);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderEmptyLine(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++){
            printSquare(out, " ");
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }

    private static void printBoardLine(PrintStream out, SquareType sq) {
        setBorder(out);
        printEmptySquare(out);

        for (int i = 0; i < BOARD_SIZE_IN_SQUARES - 2; i++) {
            if (sq == SquareType.LIGHT) {
                setLightBackground(out);
                sq = SquareType.DARK;
            } else if (sq == SquareType.DARK) {
                setDarkBackground(out);
                sq = SquareType.LIGHT;
            }
            printEmptySquare(out);
        }
        setBorder(out);
        printEmptySquare(out);
        printNewLine(out);
    }

    private static void printHeader(PrintStream out, ArrayList<String> headers, boolean isWhite) {
        int start = isWhite ? 0 : headers.size() - 1;
        int end = isWhite ? headers.size() : -1;
        int step = isWhite ? 1 : -1;


        printHeaderEmptyLine(out);
        setBorder(out);
        for (int i = start; i != end; i += step) {
            printSquare(out, headers.get(i));
        }
        printNewLine(out);
        printHeaderEmptyLine(out);
    }

    private static void printEmptySquare(PrintStream out) {
        printSquare(out, " ");
    }

    private static void printNewLine(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }

    private static void setBorder(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setLightBackground(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void setDarkBackground(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }
    private static void setLightTeam(PrintStream out) {
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setDarkTeam(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLUE);
    }

}