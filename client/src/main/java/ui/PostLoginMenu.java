package ui;

import client.facade.*;
import model.AuthData;
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
}
