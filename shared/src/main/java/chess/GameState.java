package chess;

public class GameState {
    private ChessGame.TeamColor turn = ChessGame.TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();
    private ChessRuleBook rules;
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
