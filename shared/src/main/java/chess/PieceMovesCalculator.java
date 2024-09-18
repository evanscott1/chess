package chess;

import java.util.ArrayList;
import java.util.Set;

public class PieceMovesCalculator {
    private ArrayList<>() moves;
    public PieceMovesCalculator(){}

    public Set<ChessMove> GetMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();
        switch (type) {
            case KING -> {
            }
            case QUEEN -> {
            }
            case BISHOP -> {
                return BishopMoves(board, myPosition);
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


    public Set<ChessMove> BishopMoves(ChessBoard board, ChessPosition myPosition){
        
    }
}
