package ui;

import client.websocket.ServerMessageObserver;
import client.websocket.WebSocketFacade;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ServerMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ErrorMessage;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class GameplayMenu implements ServerMessageObserver{

    private final Repl repl;
    private WebSocketFacade ws;
    private final int gameID;
    private final String playerColor; // "WHITE", "BLACK", or "OBSERVER"
    private ChessGame currentGame;

    public GameplayMenu(Repl repl, int gameID, String playerColor) {
        this.repl = repl;
        this.gameID = gameID;
        this.playerColor = playerColor;
        try {
            this.ws = new WebSocketFacade("http://localhost:8081", this);
            ws.sendCommand(new UserGameCommand(UserGameCommand.CommandType.CONNECT, repl.getAuth().authToken(), gameID));
        } catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_RED + e.getMessage());
        }
    }
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(SET_TEXT_COLOR_GREEN + "[IN_GAME] >>> " + SET_TEXT_COLOR_WHITE);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] args = line.split(" ");
            String cmd = args[0].toLowerCase();

            try {
                if (cmd.equals("leave")) {
                    ws.sendCommand(new UserGameCommand(UserGameCommand.CommandType.LEAVE, repl.getAuth().authToken(), gameID));
                    break; // Exits back to PostLoginMenu
                } else if (cmd.equals("resign")) {
                    System.out.print("Are you sure you want to resign? (yes/no): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                        ws.sendCommand(new UserGameCommand(UserGameCommand.CommandType.RESIGN, repl.getAuth().authToken(), gameID));
                    }
                } else if (cmd.equals("redraw")) {
                    if (currentGame != null) {
                        ChessBoardPrinter.printBoard(currentGame.getBoard(), !playerColor.equals("BLACK"));
                    }
                } else if (cmd.equals("move") && args.length == 3) {
                    char startCol = args[1].charAt(0);
                    int startRow = Character.getNumericValue(args[1].charAt(1));
                    char endCol = args[2].charAt(0);
                    int endRow = Character.getNumericValue(args[2].charAt(1));

                    ChessPosition start = new ChessPosition(startRow, startCol - 'a' + 1);
                    ChessPosition end = new ChessPosition(endRow, endCol - 'a' + 1);
                    // Standard move without promotion for now
                    ChessMove move = new ChessMove(start, end, null);

                    ws.sendCommand(new MakeMoveCommand(repl.getAuth().authToken(), gameID, move));
                } else if (cmd.equals("help")) {
                    System.out.println(SET_TEXT_COLOR_BLUE + "redraw" + SET_TEXT_COLOR_MAGENTA + " - redraws the board");
                    System.out.println(SET_TEXT_COLOR_BLUE + "move <START> <END>" + SET_TEXT_COLOR_MAGENTA + " - make a move (e.g. move e2 e4)");
                    System.out.println(SET_TEXT_COLOR_BLUE + "resign" + SET_TEXT_COLOR_MAGENTA + " - forfeit the game");
                    System.out.println(SET_TEXT_COLOR_BLUE + "leave" + SET_TEXT_COLOR_MAGENTA + " - return to the post-login menu");
                    System.out.println(SET_TEXT_COLOR_BLUE + "help" + SET_TEXT_COLOR_MAGENTA + " - show this menu");
                } else {
                    System.out.println(SET_TEXT_COLOR_RED + "Unknown command.");
                }
            } catch (Exception e) {
                System.out.println(SET_TEXT_COLOR_RED + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> {
                this.currentGame = ((LoadGameMessage) message).getGame().game();
                System.out.println();
                ChessBoardPrinter.printBoard(currentGame.getBoard(), !playerColor.equals("BLACK"));
                System.out.print(SET_TEXT_COLOR_GREEN + "\n[IN_GAME] >>> " + SET_TEXT_COLOR_WHITE);
            }
            case ERROR -> {
                System.out.println(SET_TEXT_COLOR_RED + "\n" + ((ErrorMessage) message).getErrorMessage());
                System.out.print(SET_TEXT_COLOR_GREEN + "[IN_GAME] >>> " + SET_TEXT_COLOR_WHITE);
            }
            case NOTIFICATION -> {
                System.out.println(SET_TEXT_COLOR_BLUE + "\n" + ((NotificationMessage) message).getMessage());
                System.out.print(SET_TEXT_COLOR_GREEN + "[IN_GAME] >>> " + SET_TEXT_COLOR_WHITE);
            }
        }
    }
}
