import chess.*;
import dataaccess.*;
import server.Server;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) {

        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }

            UserDataAccess userDataAccess = new MemoryUserDAO();
            AuthDataAccess authDataAccess = new MemoryAuthDAO();
            GameDataAccess gameDataAccess = new MemoryGameDAO();

            UserService userService = new UserService(userDataAccess, authDataAccess);
            GameService gameService = new GameService(userDataAccess, authDataAccess, gameDataAccess);
            ClearService clearService = new ClearService(userDataAccess, authDataAccess, gameDataAccess);

            var server = new Server();
            server.run(port);

            System.out.printf("Server started on port %d", port);

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}