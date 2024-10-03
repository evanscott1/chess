package chess;

public class Placement {
    private ChessPiece piece;
    private ChessPosition pos;

    public Placement(ChessPiece piece, ChessPosition pos) {
        this.piece = piece;
        this.pos = pos;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

    public ChessPosition getPos() {
        return pos;
    }

    public void setPos(ChessPosition pos) {
        this.pos = pos;
    }
}
