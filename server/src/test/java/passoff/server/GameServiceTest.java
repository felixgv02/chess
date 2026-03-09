package passoff.server;

import server.dataaccess.*;
import service.*;
import org.junit.jupiter.api.*;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import service.request.RegisterRequest;
import service.result.CreateGameResult;
import service.result.ListGamesResult;
import service.result.RegisterResult;

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
        RegisterResult reg = userService.register(new RegisterRequest("test", "pass", "email"));
        CreateGameResult res = gameService.createGame(reg.authToken(), new CreateGameRequest("MyGame"));
        Assertions.assertTrue(res.gameID() > 0);
    }

    @Test
    public void createGameFailUnauthorized() {
        Assertions.assertThrows(DataAccessException.class,
                () -> gameService.createGame("invalidToken", new CreateGameRequest("MyGame")));
    }

    @Test
    public void joinGameSuccess() throws DataAccessException {
        RegisterResult reg = userService.register(new RegisterRequest("test", "pass", "email"));
        CreateGameResult game = gameService.createGame(reg.authToken(), new CreateGameRequest("MyGame"));

        Assertions.assertDoesNotThrow(
                () -> gameService.joinGame(reg.authToken(), new JoinGameRequest("WHITE", game.gameID())));
    }

    @Test
    public void joinGameFailTaken() throws DataAccessException {
        RegisterResult reg1 = userService.register(new RegisterRequest("test1", "pass", "email"));
        RegisterResult reg2 = userService.register(new RegisterRequest("test2", "pass", "email"));
        CreateGameResult game = gameService.createGame(reg1.authToken(), new CreateGameRequest("MyGame"));

        gameService.joinGame(reg1.authToken(), new JoinGameRequest("WHITE", game.gameID()));

        Assertions.assertThrows(DataAccessException.class,
                () -> gameService.joinGame(reg2.authToken(), new JoinGameRequest("WHITE", game.gameID())));
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        RegisterResult reg = userService.register(new RegisterRequest("test", "pass", "email"));
        gameService.createGame(reg.authToken(), new CreateGameRequest("MyGame"));

        ListGamesResult list = gameService.listGames(reg.authToken());
        Assertions.assertEquals(1, list.games().size());
    }

    @Test
    public void listGamesFailUnauthorized() {
        Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames("invalid-token"));
    }
}
