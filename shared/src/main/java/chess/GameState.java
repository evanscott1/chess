package chess;

import java.util.Objects;

public class GameState {
    private ChessGame.TeamColor turn = ChessGame.TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();
    private ChessRuleBook rules = new ChessRuleBook();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameState gameState = (GameState) o;
        return turn == gameState.turn && Objects.equals(board, gameState.board) && Objects.equals(rules, gameState.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board, rules);
    }

    public GameState() {
        board.resetBoard();
    }

    public void setTurn(ChessGame.TeamColor turn) {
        this.turn = turn;
    }

    public ChessGame.TeamColor turn() {
        return turn;
    }

    public void status() {

    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public ChessBoard board() {
        return board;
    }

    public ChessRuleBook rules() {
        return rules;
    }


}
