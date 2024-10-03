package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessRuleBook {
    public ChessRuleBook() {
    }

    public Collection<ChessMove> validMoves() {

        throw new RuntimeException("Not implemented");
    }

    public boolean isBoardValid(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor color) {
        ChessPosition kingPos = findKing(board, color);
        return otherTeamEndPositions(board, color).contains(kingPos);
    }

    private ChessPosition findKing(ChessBoard board, ChessGame.TeamColor color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition pos = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color ) {
                    return pos;
                }
            }
        }
        throw new RuntimeException("King is missing from board.");
    }

    private Collection<ChessMove> otherTeamMoves(ChessBoard board, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition pos = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() != color) {
                    moves.addAll(piece.pieceMoves(board, pos));
                }
            }
        }
        return moves;
    }
    private ChessGame.TeamColor otherTeamColor(ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            return ChessGame.TeamColor.BLACK;
        }
        return ChessGame.TeamColor.WHITE;
    }
    private Collection<ChessPosition> otherTeamEndPositions(ChessBoard board, ChessGame.TeamColor color) {
        Collection<ChessPosition> positions = new ArrayList<>();
        for (ChessMove move : otherTeamMoves(board, color)) {
            positions.add(move.getEndPosition());
        }
        return positions;
    }

    public boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color) {
        if (!isInCheck(board, color)) {
            return false;
        }
        ChessPosition kingPos = findKing(board, color);
        Collection<ChessMove> kingMoves = board.getPiece(kingPos).pieceMoves(board, kingPos);
        Collection<ChessPosition> teamPositions = otherTeamEndPositions(board, color);
        for (ChessMove kingMove : kingMoves) {
            if(!teamPositions.contains(kingMove.getEndPosition())) {
                return false;
            }
        }

        return true;

    }

    public boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color) {
        if(isInCheck(board, color)) {
            return false;
        }

        Collection<ChessPosition> teamMoves = otherTeamEndPositions(board, otherTeamColor(color));


        throw new RuntimeException("Not implemented");
    }
}
