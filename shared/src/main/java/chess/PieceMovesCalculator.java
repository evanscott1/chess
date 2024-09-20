package chess;

import java.util.ArrayList;
import java.util.ArrayList;

public class PieceMovesCalculator {
    private final ArrayList<ChessMove> moves = new ArrayList<>();
    public PieceMovesCalculator(){}

    public void addMove(ChessPosition start, ChessPosition end, ChessPiece.PieceType type) {

        ChessMove move = new ChessMove(start, end, type);
        moves.add(move);
    }
    public ArrayList<ChessMove> getMoves() {
        return moves;
    }


    public ArrayList<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
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
                if (color == ChessGame.TeamColor.WHITE) {
                    whitePawnMoves(board, myPosition);
                } else if (color == ChessGame.TeamColor.BLACK) {
                    blackPawnMoves(board, myPosition);
                }

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
        ChessPosition forwardLeft = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
        ChessPosition forwardRight = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);



        if(board.getPiece(forwardOne) == null) {
            //Pawn promotion.
            if(myPosition.getRow() == 7) {
                for (ChessPiece.PieceType type : ChessPiece.PieceType.values()){
                    if(type != ChessPiece.PieceType.KING && type != ChessPiece.PieceType.PAWN){
                        addMove(myPosition, forwardOne, type);
                    }
                }
            } else {
                addMove(myPosition, forwardOne, null);
                //Pawn starts.
                if (myPosition.getRow() == 2 && board.getPiece((forwardTwo)) == null) {
                    addMove(myPosition, forwardTwo, null);
                }

            }

        }
        //Pawn takes left diagonal.
        if (board.getPiece(forwardLeft)!= null && board.getPiece(forwardLeft).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
            addMove(myPosition, forwardLeft, null);
        }
        //Pawn takes right diagonal.
        if(board.getPiece(forwardRight) != null && board.getPiece(forwardRight).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
            addMove(myPosition, forwardRight, null);
        }


    }

    public void blackPawnMoves(ChessBoard board, ChessPosition myPosition){

        ChessPosition forwardOne = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
        ChessPosition forwardTwo = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
        ChessPosition forwardLeft = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
        ChessPosition forwardRight = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);



        if(board.getPiece(forwardOne) == null) {
            //Pawn promotion.
            if(myPosition.getRow() == 2) {
                for (ChessPiece.PieceType type : ChessPiece.PieceType.values()){
                    if(type != ChessPiece.PieceType.KING && type != ChessPiece.PieceType.PAWN){
                        addMove(myPosition, forwardOne, type);
                    }
                }
            } else {
                addMove(myPosition, forwardOne, null);
                //Pawn starts.
                if (myPosition.getRow() == 7 && board.getPiece((forwardTwo)) == null) {
                    addMove(myPosition, forwardTwo, null);
                }

            }

        }
        //Pawn takes left diagonal.
        if (board.getPiece(forwardLeft)!= null && board.getPiece(forwardLeft).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
            addMove(myPosition, forwardLeft, null);
        }
        //Pawn takes right diagonal.
        if(board.getPiece(forwardRight) != null && board.getPiece(forwardRight).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
            addMove(myPosition, forwardRight, null);
        }


    }
}

