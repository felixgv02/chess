package passoff.server;
import server.dataaccess.*;
import service.*;
import org.junit.jupiter.api.*;
public class GameServiceTest {
    private GameService gameService;
    private UserService userService;
    @BeforeEach
    public void setup() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
    }
    @Test
    public void createGameSuccess() throws DataAccessException {
        UserService.RegisterResult reg = userService.register(new UserService.RegisterRequest("test", "pass", "email"));
        GameService.CreateGameResult res = gameService.createGame(reg.authToken(), new GameService.CreateGameRequest("MyGame"));
        Assertions.assertTrue(res.gameID() > 0);
    }
    @Test
    public void createGameFailUnauthorized() {
        Assertions.assertThrows(DataAccessException.class, () ->
                gameService.createGame("invalidToken", new GameService.CreateGameRequest("MyGame")));
    }

    @Test
    public void joinGameSuccess() throws DataAccessException {
        UserService.RegisterResult reg = userService.register(new UserService.RegisterRequest("test", "pass", "email"));
        GameService.CreateGameResult game = gameService.createGame(reg.authToken(), new GameService.CreateGameRequest("MyGame"));

        Assertions.assertDoesNotThrow(() ->
                gameService.joinGame(reg.authToken(), new GameService.JoinGameRequest("WHITE", game.gameID())));
    }
    @Test
    public void joinGameFailTaken() throws DataAccessException {
        UserService.RegisterResult reg1 = userService.register(new UserService.RegisterRequest("test1", "pass", "email"));
        UserService.RegisterResult reg2 = userService.register(new UserService.RegisterRequest("test2", "pass", "email"));
        GameService.CreateGameResult game = gameService.createGame(reg1.authToken(), new GameService.CreateGameRequest("MyGame"));

        gameService.joinGame(reg1.authToken(), new GameService.JoinGameRequest("WHITE", game.gameID()));

        Assertions.assertThrows(DataAccessException.class, () ->
                gameService.joinGame(reg2.authToken(), new GameService.JoinGameRequest("WHITE", game.gameID())));
    }
    @Test
    public void listGamesSuccess() throws DataAccessException {
        UserService.RegisterResult reg = userService.register(new UserService.RegisterRequest("test", "pass", "email"));
        gameService.createGame(reg.authToken(), new GameService.CreateGameRequest("MyGame"));

        GameService.ListGamesResult list = gameService.listGames(reg.authToken());
        Assertions.assertEquals(1, list.games().size());
    }
    @Test
    public void listGamesFailUnauthorized() {
        Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames("invalid-token"));
    }
}
