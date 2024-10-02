package chess;

import java.util.Collection;

public class ChessRuleBook {
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
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
