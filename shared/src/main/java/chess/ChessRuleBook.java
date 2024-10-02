package chess;

import java.util.Collection;

public class ChessRuleBook {
    private ChessBoard board;
    private ChessPosition start;

    public ChessRuleBook(ChessBoard board, ChessPosition start) {
        this.board = board;
        this.start = start;
    }

    public Collection<ChessMove> validMoves() {
        Collection<ChessMove> pieceMoves = board.getPiece(start).pieceMoves(board, start);

        throw new RuntimeException("Not implemented");
    }

    public boolean isBoardValid(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

    public boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

    public boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }
}
