package ui;

import client.facade.ServerFacade;
import model.AuthData;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    private final ServerFacade facade;
    private AuthData currentAuth;

    public Repl(int port) {
        this.facade = new ServerFacade(port);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_WHITE + "\uD83D\uDC51 Welcome to 240 chess. Type Help to get started. \uD83D\uDC51\n");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(SET_TEXT_COLOR_GREEN + (currentAuth == null ? "[LOGGED_OUT]" : "[LOGGED_IN]") + " >>> " + SET_TEXT_COLOR_WHITE);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] args = line.split(" ");
            String cmd = args[0].toLowerCase();

            try {
                if (currentAuth == null) {
                    if (!new PreLoginMenu(facade, this).handleCommand(cmd, args)) break;
                } else {
                    if (!new PostLoginMenu(facade, this).handleCommand(cmd, args)) break;
                }
            } catch (Exception e) {
                System.out.println(SET_TEXT_COLOR_RED + e.getMessage() + SET_TEXT_COLOR_WHITE);
            }
        }
    }

    public void setAuth(AuthData auth) {
        this.currentAuth = auth;
    }
    public AuthData getAuth() {
        return currentAuth;
    }
}
