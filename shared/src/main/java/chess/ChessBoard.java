package chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    private final ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][ position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        placePawns(ChessGame.TeamColor.BLACK, 7);
        placePawns(ChessGame.TeamColor.WHITE, 2);
        royalPieces(ChessGame.TeamColor.BLACK, 8);
        royalPieces(ChessGame.TeamColor.WHITE, 1);


    }
    public void placePawns(ChessGame.TeamColor color, int row) {
        for(int i = 1; i <= getWidth(); i++) {
            ChessPosition pawnPos = new ChessPosition(row, i);
            ChessPiece pawn = new ChessPiece(color, ChessPiece.PieceType.PAWN);
            addPiece(pawnPos, pawn);
        }

    }
    public void royalPieces(ChessGame.TeamColor color, int row) {
        ArrayList<ChessPiece.PieceType> types = new ArrayList<>();
        types.add(ChessPiece.PieceType.ROOK);
        types.add(ChessPiece.PieceType.KNIGHT);
        types.add(ChessPiece.PieceType.BISHOP);
        types.add(ChessPiece.PieceType.QUEEN);
        types.add(ChessPiece.PieceType.KING);
        types.add(ChessPiece.PieceType.BISHOP);
        types.add(ChessPiece.PieceType.KNIGHT);
        types.add(ChessPiece.PieceType.ROOK);

        int i = 1;
        for(ChessPiece.PieceType type : types) {
            ChessPosition pos = new ChessPosition(row, i);
            ChessPiece piece = new ChessPiece(color, type);
            addPiece(pos, piece);
            i++;
        }
    }

    public int getWidth() {
        return squares.length;
    }

    public void printBoard(ChessBoard board) {
        for (int i = 1; i <= getWidth(); i++) {
            for (int j = 1; j <= getWidth(); j++) {
                ChessPosition pos = new ChessPosition(i, j);
                char c;
                switch (board.getPiece(pos).getPieceType()) {
                    case KING -> {
                        c = 'K';
                    }
                    case PAWN -> {
                        c = 'P';
                    }
                    case ROOK -> {
                        c = 'R';
                    }
                    case QUEEN -> {
                        c = 'Q';
                    }
                    case BISHOP ->  {
                        c = 'B';
                    }
                    case KNIGHT -> {
                        c = 'N';
                    }
                    case null -> {
                        c = ' ';
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + board.getPiece(pos));
                }
                System.out.print("|" + c);
            }
            System.out.println('|');
        }
    }
}

