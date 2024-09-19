package chess;

import java.util.ArrayList;
import java.util.Set;

public class PieceMovesCalculator {
    private Set<ChessMove> moves;
    public PieceMovesCalculator(){}

    public Set<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();
        switch (type) {
            case KING -> {
            }
            case QUEEN -> {
            }
            case BISHOP -> {
                return bishopMoves(board, myPosition);
            }
            case KNIGHT -> {
            }
            case ROOK -> {
            }
            case PAWN -> {
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }


    public Set<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        
    }

    public Set<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        ChessPosition testPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
        if (board.getPiece(testPosition) != null) {}
    }
}
