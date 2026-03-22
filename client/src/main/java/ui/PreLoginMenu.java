package ui;

import client.facade.*;

public class PreLoginMenu {
    private final ServerFacade facade;
    private final Repl repl;

    public PreLoginMenu(ServerFacade facade, Repl repl) {
        this.facade = facade;
        this.repl = repl;
    }
}
