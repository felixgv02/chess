package ui;

import client.facade.*;
import model.GameData;
import chess.ChessGame;
import java.util.*;
import static ui.EscapeSequences.*;

public class PostLoginMenu {
    private final ServerFacade facade;
    private final Repl repl;
    private static List<GameData> cachedGames = new ArrayList<>();
    public PostLoginMenu(ServerFacade facade, Repl repl) {
        this.facade = facade;
        this.repl = repl;
    }

    public boolean handleCommand(String cmd, String[] args) throws ResponseException {
        switch (cmd) {
            case "help":
                System.out.println(SET_TEXT_COLOR_BLUE + "create <NAME>" + SET_TEXT_COLOR_MAGENTA + " - a game");
                System.out.println(SET_TEXT_COLOR_BLUE + "list" + SET_TEXT_COLOR_MAGENTA + " - games");
                System.out.println(SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]" + SET_TEXT_COLOR_MAGENTA + " - a game");
                System.out.println(SET_TEXT_COLOR_BLUE + "observe <ID>" + SET_TEXT_COLOR_MAGENTA + " - a game");
                System.out.println(SET_TEXT_COLOR_BLUE + "logout" + SET_TEXT_COLOR_MAGENTA + " - when you are done");
                System.out.println(SET_TEXT_COLOR_BLUE + "quit" + SET_TEXT_COLOR_MAGENTA + " - playing chess");
                System.out.println(SET_TEXT_COLOR_BLUE + "help" + SET_TEXT_COLOR_MAGENTA + " - with possible commands");
                break;
            case "logout":
                facade.logout(repl.getAuth().authToken());
                repl.setAuth(null);
                System.out.println("Logged out successfully.");
                break;
            case "create":
                if (args.length == 2) {
                    facade.createGame(repl.getAuth().authToken(), args[1]);
                    System.out.println("Created game: " + args[1]);
                } else {
                    System.out.println(SET_TEXT_COLOR_RED + "Expected: create <NAME>");
                }
                break;
            case "list":
                cachedGames = new ArrayList<>(facade.listGames(repl.getAuth().authToken()));
                if (cachedGames.isEmpty()) {
                    System.out.println("No games available.");
                } else {
                    for (int i = 0; i < cachedGames.size(); i++) {
                        GameData g = cachedGames.get(i);
                        System.out.printf("%d. %s (White: %s, Black: %s)\n", i + 1, g.gameName(),
                                (g.whiteUsername() != null ? g.whiteUsername() : "none"),
                                (g.blackUsername() != null ? g.blackUsername() : "none"));
                    }
                }
                break;
            case "join":
                if (args.length == 3) {
                    try {
                        int listId = Integer.parseInt(args[1]);
                        if (listId < 1 || listId > cachedGames.size()) {
                            throw new Exception();
                        }
                        GameData g = cachedGames.get(listId - 1);
                        String color = args[2].toUpperCase();
                        facade.joinGame(repl.getAuth().authToken(), color, g.gameID());
                        System.out.println("Successfully joined game as " + color);

                        // Draw board from perspective
                        boolean isWhite = color.equals("WHITE");
                        ChessBoardPrinter.printBoard(new ChessGame().getBoard(), isWhite);
                    } catch (ResponseException e) {
                        System.out.println(SET_TEXT_COLOR_RED + e.getMessage());
                    } catch (Exception e) {
                        System.out.println(SET_TEXT_COLOR_RED + "Invalid physical game ID. Did you `list` first?");
                    }
                } else {
                    System.out.println(SET_TEXT_COLOR_RED + "Expected: join <ID> [WHITE|BLACK]");
                }
                break;
            case "observe":
                if (args.length == 2) {
                    try {
                        int listId = Integer.parseInt(args[1]);
                        if (listId < 1 || listId > cachedGames.size()) {
                            throw new Exception();
                        }
                        GameData g = cachedGames.get(listId - 1);
                        System.out.println("Observing game: " + g.gameName());

                        // Draw board from White's perspective
                        ChessBoardPrinter.printBoard(new ChessGame().getBoard(), true);
                    } catch (Exception e) {
                        System.out.println(SET_TEXT_COLOR_RED + "Invalid physical game ID. Did you `list` first?");
                    }
                } else {
                    System.out.println(SET_TEXT_COLOR_RED + "Expected: observe <ID>");
                }
                break;
            case "quit":
                return false;
            default:
                System.out.println(SET_TEXT_COLOR_RED + "Unknown command: " + cmd);
                break;
        }
        return true;
    }
}
