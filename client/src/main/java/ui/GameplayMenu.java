package ui;

import client.websocket.ServerMessageObserver;
import client.websocket.WebSocketFacade;
import websocket.commands.UserGameCommand;
import chess.ChessGame;


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
        }
    }
}
