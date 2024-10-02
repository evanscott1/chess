package chess;

import java.util.ArrayList;
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
        ChessPosition kingPos = findKing(board, color);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition pos = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() != color) {
                    Collection<ChessMove> moves = piece.pieceMoves(board, pos);
                    for (ChessMove move : moves) {
                        if( move.getEndPosition() == kingPos) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private ChessPosition findKing(ChessBoard board, ChessGame.TeamColor color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition pos = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(pos);
                if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color ) {
                    return pos;
                }
            }
        }
    }

    public boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

    public boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }
}
