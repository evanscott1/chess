package client;


import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final ChessClient clientChess;

    public Repl(String serverUrl) {
        clientChess = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to chess.");
        System.out.print(clientChess.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = clientChess.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_BOLD + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}