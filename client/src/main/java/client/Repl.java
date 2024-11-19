package client;


import client.websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient clientChess;

    public Repl(String serverUrl) {
        clientChess = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("♕ Welcome to chess ♕");
        System.out.print(clientChess.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = clientChess.eval(line);
                if (result.equals("quit")) {
                    System.out.print(SET_TEXT_COLOR_BLUE + "quitting...");
                } else {
                    System.out.print(SET_TEXT_COLOR_BLUE + result);
                }
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

    @Override
    public void notify(ServerMessage notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.getMessage());
        printPrompt();
    }
}
