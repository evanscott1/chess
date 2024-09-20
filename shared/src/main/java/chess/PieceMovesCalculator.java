package chess;

import java.util.ArrayList;
import java.util.ArrayList;

public class PieceMovesCalculator {
    private final ArrayList<ChessMove> moves = new ArrayList<>();
    public PieceMovesCalculator(){}

    public void addMove(ChessPosition start, ChessPosition end, ChessPiece.PieceType type) {

        ChessMove moveT = new ChessMove(start, end, type);
        moves.add(moveT);
    }
    public ArrayList<ChessMove> getMoves() {
        return moves;
    }


    public ArrayList<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();
        switch (type) {
            case KING -> {
            }
            case QUEEN -> {
            }
            case BISHOP -> {

            }
            case KNIGHT -> {
            }
            case ROOK -> {
            }
            case PAWN -> {
                whitePawnMoves(board, myPosition);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return moves;
    }


//    public ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
//
//    }

    public void whitePawnMoves(ChessBoard board, ChessPosition myPosition){

        ChessPosition forwardOne = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
        ChessPosition forwardTwo = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
        if (myPosition.getRow() == 2) {

            if(board.getPiece(forwardOne) == null) {
                addMove(myPosition, forwardOne, null);
                if(board.getPiece((forwardTwo)) == null) {
                    addMove(myPosition, forwardTwo, null);
                }
            }
        }
    }
}
