package chess;

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
    private ChessPiece[][] squares = new ChessPiece[8][8];
//    Understand the history code better so that it doesn't cause assertEquals issues
//    private ArrayList<ChessBoard> history = new ArrayList<>();

    public ChessBoard() {

    }

    public ChessBoard(ChessBoard newBoard) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece newPiece = newBoard.getPiece(new ChessPosition(i + 1, j + 1));
                if (newPiece != null) {
                    squares[i][j] = new ChessPiece(newPiece);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    public int getBoardWidth() {
        return squares.length;
    }

    public void movePiece(ChessPosition start, ChessPosition end) {


        ChessPiece piece = new ChessPiece(this.getPiece(start));
        squares[start.getRow() - 1][start.getColumn() - 1] = null;
        squares[end.getRow() - 1][end.getColumn() - 1] = piece;
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        addPawns(ChessGame.TeamColor.WHITE, 2);
        addPawns(ChessGame.TeamColor.BLACK, 7);
        addRoyalty(ChessGame.TeamColor.WHITE, 1);
        addRoyalty(ChessGame.TeamColor.BLACK, 8);
    }

    public void addPawns(ChessGame.TeamColor color, int row) {
        for (int i = 1; i <= getBoardWidth(); i++) {
            ChessPosition pos = new ChessPosition(row, i);
            ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.PAWN);
            addPiece(pos, piece);
        }
    }

    public void addRoyalty(ChessGame.TeamColor color, int row) {
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
        for (ChessPiece.PieceType type : types) {
            ChessPosition pos = new ChessPosition(row, i);
            ChessPiece piece = new ChessPiece(color, type);
            addPiece(pos, piece);
            i++;
        }
    }
}
