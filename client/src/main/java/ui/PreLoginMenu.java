package ui;

import client.facade.*;
import static ui.EscapeSequences.*;

public class PreLoginMenu {
    private final ServerFacade facade;
    private final Repl repl;

    public PreLoginMenu(ServerFacade facade, Repl repl) {
        this.facade = facade;
        this.repl = repl;
    }

    public boolean handleCommand(String cmd, String[] args) throws ResponseException {
        switch (cmd) {
            case "help":
                System.out.println(SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>"
                        + SET_TEXT_COLOR_MAGENTA + " - to create an account");
                System.out.println(SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + SET_TEXT_COLOR_MAGENTA + " - to play chess");
                System.out.println(SET_TEXT_COLOR_BLUE + "quit" + SET_TEXT_COLOR_MAGENTA + " - playing chess");
                System.out.println(SET_TEXT_COLOR_BLUE + "help" + SET_TEXT_COLOR_MAGENTA + " - with possible commands");
                break;
            case "register":
                if (args.length == 4) {
                    repl.setAuth(facade.register(args[1], args[2], args[3]));
                    System.out.println("Registered and logged in as " + args[1]);
                } else {
                    System.out.println(SET_TEXT_COLOR_RED + "Expected: register <USERNAME> <PASSWORD> <EMAIL>");
                }
                break;
            case "login":
                if (args.length == 3) {
                    repl.setAuth(facade.login(args[1], args[2]));
                    System.out.println("Logged in as " + args[1]);
                } else {
                    System.out.println(SET_TEXT_COLOR_RED + "Expected: login <USERNAME> <PASSWORD>");
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
