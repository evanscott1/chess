package chess;

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


    public void calculateMoves(ChessBoard board, ChessPosition myPosition){
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
                rookMoves(board, myPosition);
            }
            case PAWN -> {
                pawnMoves(board, myPosition);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }


//    public ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
//
//    }
    public void rookMoves(ChessBoard board, ChessPosition start) {
        for (int i = 0; i < board.getWidth(); i++) {
            if(i + 1 != start.getColumn()){
                ChessPosition end = new ChessPosition(start.getRow(), i + 1);
                addMove(start, end, null);
            }
            if(i + 1 != start.getRow()){
                ChessPosition end = new ChessPosition(i + 1, start.getColumn());
                addMove(start, end, null);
            }

        }
    }

    public void pawnMoves(ChessBoard board, ChessPosition start) {
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        int offset = 0;
        int startRow = 0;
        int promoteRow = 0;
        if (color == ChessGame.TeamColor.WHITE) {
            offset = 1;
            startRow = 2;
            promoteRow = 7;
        } else if (color == ChessGame.TeamColor.BLACK) {
            offset = -1;
            startRow = 7;
            promoteRow = 2;
        }
        ChessPosition forwardOne = new ChessPosition(start.getRow() + offset, start.getColumn());
        ChessPosition forwardTwo = new ChessPosition(start.getRow() + offset * 2, start.getColumn());
        ChessPosition forwardLeft = new ChessPosition(start.getRow() + offset, start.getColumn() - offset);
        ChessPosition forwardRight = new ChessPosition(start.getRow() + offset, start.getColumn() + offset);

        boolean promote = false;
        if(start.getRow() == promoteRow) {
            promote = true;
        }

        if(board.getPiece(forwardOne) == null) {
            //Pawn promotion.
            if(promote) {
                pawnPromotion(start, forwardOne);
            } else {
                addMove(start, forwardOne, null);
                //Pawn starts.
                if (start.getRow() == startRow && board.getPiece((forwardTwo)) == null) {
                    addMove(start, forwardTwo, null);
                }

            }

        }
        //Pawn takes left diagonal.
        pawnCapture(board, start, forwardLeft, promote);
        //Pawn takes right diagonal.
        pawnCapture(board, start, forwardRight, promote);
    }

    public void pawnCapture(ChessBoard board, ChessPosition start, ChessPosition end, boolean promote) {
        if(board.getPiece(end) != null && board.getPiece(end).getTeamColor() != board.getPiece(start).getTeamColor()) {
            if(promote) {
                pawnPromotion(start, end);
            } else {
                addMove(start, end, null);
            }
        }
    }

    public void pawnPromotion(ChessPosition start, ChessPosition end) {
        for (ChessPiece.PieceType type : ChessPiece.PieceType.values()){
            if(type != ChessPiece.PieceType.KING && type != ChessPiece.PieceType.PAWN){
                addMove(start, end, type);
            }
        }
    }
}

