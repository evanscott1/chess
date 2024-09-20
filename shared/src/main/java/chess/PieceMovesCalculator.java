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

    public boolean oppTeam(ChessBoard board, ChessPosition start, ChessPosition end) {
        return board.getPiece(start).getTeamColor() != board.getPiece(end).getTeamColor();
    }
    public void isValidMove(ChessBoard board, ChessPosition start, ChessPosition end) {
        if(end.getRow() - 1 < board.getWidth() && end.getRow() > 0 && end.getColumn() - 1 < board.getWidth() && end.getColumn() > 0) {
            if(board.getPiece(end) == null) {
                addMove(start, end, null);
            } else if (oppTeam(board, start, end)) {
                addMove(start, end, null);
            }
        }

    }

    public void calculateMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();

        switch (type) {
            case KING -> {
                kingMoves(board, myPosition);
            }
            case QUEEN -> {
                queenMoves(board, myPosition);
            }
            case BISHOP -> {
                bishopMoves(board, myPosition);
            }
            case KNIGHT -> {
                knightMoves(board, myPosition);
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

    public void knightMoves(ChessBoard board, ChessPosition start) {
        ArrayList<int[]> pairs = new ArrayList<>();
        pairs.add(new int[]{1, 2});
        pairs.add(new int[]{2, 1});
        pairs.add(new int[]{2, -1});
        pairs.add(new int[]{1, -2});
        pairs.add(new int[]{-1, -2});
        pairs.add(new int[]{-2, -1});
        pairs.add(new int[]{-2, 1});
        pairs.add(new int[]{-1, 2});
        for (int[] pair: pairs) {
            ChessPosition end = new ChessPosition(start.getRow() + pair[0], start.getColumn() + pair[1]);
            isValidMove(board, start, end);
        }
    }

    public void queenMoves(ChessBoard board, ChessPosition start) {
        bishopMoves(board, start);
        rookMoves(board, start);
    }

    public void bishopMoves(ChessBoard board, ChessPosition start) {
        //topRight
        int j = start.getColumn();
        iLoop:
        for(int i = start.getRow(); i < board.getWidth(); i++) {
            if(j < board.getWidth()){
                ChessPosition end = new ChessPosition(i + 1, j + 1);
                if(board.getPiece(end) == null) {
                    addMove(start, end, null);
                } else if (oppTeam(board, start, end)) {
                    addMove(start, end, null);
                    {break iLoop;}
                } else {break iLoop;}
                j++;
            }
        }
        //bottomRight
        j = start.getColumn();
        iLoop:
        for(int i = start.getRow(); i > 1; i--) {
            if(j < board.getWidth()){
                ChessPosition end = new ChessPosition(i - 1, j + 1);
                if(board.getPiece(end) == null) {
                    addMove(start, end, null);
                } else if (oppTeam(board, start, end)) {
                    addMove(start, end, null);
                    {break iLoop;}
                } else {break iLoop;}
                j++;
            }
        }
        //topLeft
        j = start.getColumn();
        iLoop:
        for(int i = start.getRow(); i < board.getWidth(); i++) {
            if(j > 1){
                ChessPosition end = new ChessPosition(i + 1, j - 1);
                if(board.getPiece(end) == null) {
                    addMove(start, end, null);
                } else if (oppTeam(board, start, end)) {
                    addMove(start, end, null);
                    {break iLoop;}
                } else {break iLoop;}
                j--;
            }
        }
        //bottomLeft
        j = start.getColumn();
        iLoop:
        for(int i = start.getRow(); i > 1; i--) {
            if(j > 1){
                ChessPosition end = new ChessPosition(i - 1, j - 1);
                if(board.getPiece(end) == null) {
                    addMove(start, end, null);
                } else if (oppTeam(board, start, end)) {
                    addMove(start, end, null);
                    {break iLoop;}
                } else {break iLoop;}
                j--;
            }
        }
    }

    public void kingMoves(ChessBoard board, ChessPosition start) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                ChessPosition end = new ChessPosition(start.getRow() + i - 1, start.getColumn() + j - 1);
                if((end.getRow() > 0 && end.getColumn() > 0) && (end.getRow() <= board.getWidth() && end.getColumn() <= board.getWidth())){
                    if (board.getPiece(end) == null){
                        addMove(start, end, null);
                    } else if (oppTeam(board, start, end)) {
                        addMove(start, end, null);
                    }
                }
            }
        }
    }

//    public ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
//
//    }
    public void rookMoves(ChessBoard board, ChessPosition start) {
        //TODO: find a way to reduce code dup
        //rookRight
        for(int i = start.getColumn(); i < board.getWidth(); i++) {
            ChessPosition end = new ChessPosition(start.getRow(), i + 1);
            if(board.getPiece(end) == null) {
                addMove(start, end, null);
            } else if (oppTeam(board, start, end)) {
                addMove(start, end, null);
                {break;}
            } else {break;}
        }
        //rookUp
        for(int i = start.getRow(); i < board.getWidth(); i++) {
            ChessPosition end = new ChessPosition(i + 1, start.getColumn());
            if(board.getPiece(end) == null) {
                addMove(start, end, null);
            } else if (oppTeam(board, start, end)) {
                addMove(start, end, null);
                {break;}
            } else {break;}
        }
        //rookLeft
        for(int i = start.getColumn(); i > 1; i--) {
            ChessPosition end = new ChessPosition(start.getRow(), i - 1);
            if(board.getPiece(end) == null) {
                addMove(start, end, null);
            } else if (oppTeam(board, start, end)) {
                addMove(start, end, null);
                {break;}
            } else {break;}
        }
        //rookDown
        for(int i = start.getRow(); i > 1; i--) {
            ChessPosition end = new ChessPosition(i - 1, start.getColumn());
            if(board.getPiece(end) == null) {
                addMove(start, end, null);
            } else if (oppTeam(board, start, end)) {
                addMove(start, end, null);
                {break;}
            } else {break;}
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
        if(board.getPiece(end) != null && oppTeam(board, start, end)) {
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

