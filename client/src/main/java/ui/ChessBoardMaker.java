package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ChessBoardMaker {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";

    private static Random rand = new Random();

//    private static final GameData gameData = null;


    public static void boardMaker(GameData gameData) {
        ChessBoard board = gameData.game().getBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        ArrayList<String> squareCharacters = new ArrayList<>();
        out.print(ERASE_SCREEN);


        ArrayList<String> headers = new ArrayList<>(List.of(" ", "a", "b", "c", "d", "e", "f", "g", "h", " "));

        squareCharacters.addAll(headers);

        printLine(out);
        for (String header : headers) {
            printSquare(out, header);
        }
        printLine(out);

        for (int i = 1; i < BOARD_SIZE_IN_SQUARES - 1; i++) {
            squareCharacters.add(Integer.toString(i));
            printLine(out);
            printSquare(out, Integer.toString(i));

            for (int j = 1; j < BOARD_SIZE_IN_SQUARES - 1; j++) {
                String character;
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    character = " ";
                    printSquare(out, character);
                } else {
                    character = String.valueOf(piece.getPieceType().name().charAt(0));
                    printSquare(out, character);
                }

                squareCharacters.add(character);
            }

            squareCharacters.add(Integer.toString(i));
            printSquare(out, Integer.toString(i));
            printLine(out);
        }
        printLine(out);
        for (String header : headers) {
            printSquare(out, header);
        }
        printLine(out);
        squareCharacters.addAll(headers);


        for (int rows = 0; rows < BOARD_SIZE_IN_SQUARES; rows++) {

        }



        for (int square = 0; square < BOARD_SIZE_IN_SQUARES * BOARD_SIZE_IN_SQUARES; square++) {

        }

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private static void printSquare(PrintStream out, String character) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        out.print(character);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printLine(PrintStream out) {
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS * BOARD_SIZE_IN_SQUARES));
        out.println();
    }


}
