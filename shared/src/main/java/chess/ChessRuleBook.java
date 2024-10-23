package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessRuleBook {
    public ChessRuleBook() {
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board) {
        Collection<ChessMove> moves = board.getPiece(start).pieceMoves(board, start);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : moves) {
            ChessBoard newBoard =  new ChessBoard(board);
            newBoard.movePiece(move.getStartPosition(), move.getEndPosition());
            if(!isInCheck(newBoard, newBoard.getPiece(move.getEndPosition()).getTeamColor())) {
                validMoves.add(move);
            }
        }
        return validMoves;
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
        return moveIntoCheck(board, color);

    }

    private boolean moveIntoCheck(ChessBoard board, ChessGame.TeamColor color) {
        Collection<ChessMove> teamMoves = otherTeamMoves(board, otherTeamColor(color));
        for (ChessMove move : teamMoves) {
            ChessBoard newBoard =  new ChessBoard(board);
            newBoard.movePiece(move.getStartPosition(), move.getEndPosition());
            if(!isInCheck(newBoard, color)) {
                return false;
            }
        }
        return true;
    }

    public boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color) {
        if(isInCheck(board, color)) {
            return false;
        }

        return moveIntoCheck(board, color);
    }
}
