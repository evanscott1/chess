package chess;

import java.util.ArrayList;

public class PieceMovesCalculator {

    private ChessBoard board;
    private ChessPosition start;
    private ChessPosition end = new ChessPosition(0,0);
    private ArrayList<ChessMove> moves = new ArrayList<>();
    private ChessPiece.PieceType startType;
    private ChessGame.TeamColor startColor;
    private ChessPiece.PieceType promotion = null;
    private boolean endIsOppTeam = false;
    PieceMovesCalculator(ChessBoard board, ChessPosition start) {
        this.board = board;
        this.start = start;
    }

    public ArrayList<ChessMove> calculateMoves() {
        startType = board.getPiece(start).getPieceType();
        startColor = board.getPiece(start).getTeamColor();
        switch (startType) {
            case PAWN -> {
                addPawnMoves();
            }
            case ROOK -> {
                addRookMoves();
            }
            case KNIGHT -> {
                addKnightMoves();
            }
            case BISHOP -> {
                addBishopMoves();
            }
            case QUEEN -> {
                addQueenMoves();
            }
            case KING -> {
                addKingMoves();
            }
        }
        return moves;
    }

    public boolean isOnBoardEnd() {
        return end.getRow() > 0 && end.getRow() <= board.getBoardWidth() && end.getColumn() > 0 && end.getColumn() <= board.getBoardWidth();
    }

    public boolean isNullEnd() {
        return board.getPiece(end) == null;
    }

    public boolean isOppTeamEnd() {
        return startColor != board.getPiece(end).getTeamColor();
    }

    public void addMove() {
        ChessPosition newEnd = new ChessPosition(end.getRow(), end.getColumn());
        ChessPiece.PieceType newPromotion = promotion;
        ChessMove move = new ChessMove(start, newEnd, newPromotion);
        moves.add(move);
    }

    public void addNullAddOpp () {
        if(isOnBoardEnd()) {
            if(isNullEnd()) {
                addMove();
            } else if (isOppTeamEnd()) {
                addMove();
                endIsOppTeam = true;
            } else{
                endIsOppTeam = true;
            }

        }

    }

    public void addPawnMoves() {
        int forward = -1;
        if(startColor == ChessGame.TeamColor.WHITE) {
            forward = 1;
        }
        end.setCol(start.getColumn());
        end.setRow(start.getRow() + forward);
        if(isNullEnd()) {
            pawnPromotion();
            if(startColor == ChessGame.TeamColor.WHITE && start.getRow() == 2) {
                end.setRow(start.getRow() + 2 * forward);
                if(isNullEnd()) {
                    addMove();
                }
            } else if (startColor == ChessGame.TeamColor.BLACK && start.getRow() == 7) {
                end.setRow(start.getRow() + 2 * forward);
                if(isNullEnd()) {
                    addMove();
                }
            }
        }
        end.setRow(start.getRow() + forward);
        pawnCapture(-1);
        pawnCapture(1);

    }

    public void pawnCapture(int i) {
        end.setCol(start.getColumn() + i);
        if(isOnBoardEnd() && !isNullEnd() && isOppTeamEnd()) {
            pawnPromotion();
        }
    }

    public void pawnPromotion() {
        if(promotePawn()) {
            ArrayList<ChessPiece.PieceType> types = new ArrayList<>();
            types.add(ChessPiece.PieceType.ROOK);
            types.add(ChessPiece.PieceType.KNIGHT);
            types.add(ChessPiece.PieceType.BISHOP);
            types.add(ChessPiece.PieceType.QUEEN);

            for( ChessPiece.PieceType type : types) {
                promotion = type;
                addMove();
            }
            promotion = null;
        } else {
            addMove();
        }
    }

    public boolean promotePawn() {
        int i = 1;
        if(startColor == ChessGame.TeamColor.WHITE) {
            i = 8;
        }
        return end.getRow() == i;
    }
    public void addRookMoves() {
        int i = 0;
        //right
        endIsOppTeam = false;
        end.setRow(start.getRow());
        for(i = start.getColumn() + 1; i <= board.getBoardWidth(); i++) {
            end.setCol(i);
            addNullAddOpp();
            if(endIsOppTeam) {break;}
        }
        //left
        endIsOppTeam = false;
        end.setRow(start.getRow());
        for(i = start.getColumn() - 1; i > 0; i--) {
            end.setCol(i);
            addNullAddOpp();
            if(endIsOppTeam) {break;}
        }
        //up
        endIsOppTeam = false;
        end.setCol(start.getColumn());
        for(i = start.getRow() + 1; i <= board.getBoardWidth(); i++) {
            end.setRow(i);
            addNullAddOpp();
            if(endIsOppTeam) {break;}
        }
        //down
        endIsOppTeam = false;
        end.setCol(start.getColumn());
        for(i = start.getRow() - 1; i > 0; i--) {
            end.setRow(i);
            addNullAddOpp();
            if(endIsOppTeam) {break;}
        }
    }
    public void addKnightMoves() {
        int[][] pairs = {{1,2}, {2,1}, {2,-1}, {1,-2}, {-1,-2}, {-2,-1}, {-2,1}, {-1,2}};

        for (int[] pair : pairs) {
            endIsOppTeam = false;
            end.setRow(start.getRow() + pair[0]);
            end.setCol(start.getColumn() + pair[1]);
            addNullAddOpp();
        }
    }
    public void addBishopMoves() {
        int i = 0;
        int j = 0;

        //topRight
        endIsOppTeam = false;
        j = start.getColumn() + 1;
        for(i = start.getRow() + 1; i <= board.getBoardWidth(); i++) {
            end.setRow(i);
            end.setCol(j);
            addNullAddOpp();
            j++;
            if(endIsOppTeam) {break;}
        }

        //topLeft
        endIsOppTeam = false;
        j = start.getColumn() - 1;
        for(i = start.getRow() + 1; i <= board.getBoardWidth(); i++) {
            end.setRow(i);
            end.setCol(j);
            addNullAddOpp();
            j--;
            if(endIsOppTeam) {break;}
        }

        //bottomRight
        endIsOppTeam = false;
        j = start.getColumn() + 1;
        for(i = start.getRow() - 1; i > 0; i--) {
            end.setRow(i);
            end.setCol(j);
            addNullAddOpp();
            j++;
            if(endIsOppTeam) {break;}
        }
        //bottomLeft
        endIsOppTeam = false;
        j = start.getColumn() - 1;
        for(i = start.getRow() - 1; i > 0; i--) {
            end.setRow(i);
            end.setCol(j);
            addNullAddOpp();
            j--;
            if(endIsOppTeam) {break;}
        }

    }
    public void addQueenMoves () {
        addRookMoves();
        addBishopMoves();
    }
    public void addKingMoves() {
        int i = 0;
        int j = 0;
        for(i = start.getRow() - 1; i < start.getRow() + 2; i++) {
            for(j = start.getColumn() - 1; j < start.getColumn() + 2; j++) {
                end.setRow(i);
                end.setCol(j);
                addNullAddOpp();
            }
        }
    }
}
